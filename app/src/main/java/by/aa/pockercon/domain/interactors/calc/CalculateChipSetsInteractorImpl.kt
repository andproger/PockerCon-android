package by.aa.pockercon.domain.interactors.calc

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import by.aa.pockercon.domain.gateways.repositories.PersonCountRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CalculateChipSetsInteractorImpl(
    private val chipsRepository: ChipsRepository,
    private val personCountRepository: PersonCountRepository
) : CalculateChipSetsInteractor {

    override fun calculate(): Observable<CalcResult> {
        return Observable.combineLatest(
            chipsRepository.getAllWithUpdates(),
            personCountRepository.getWithUpdates()
                .map { if (it < 2) 2 else it },
            BiFunction { chips: List<Chip>, personCount: Int ->
                chips to personCount
            }
        ).switchMap { chipsAndCount ->
            Observable.fromCallable {
                val (chips, personCount) = chipsAndCount

                if (!valid(chips, personCount)) error("")

                val totalSum = chips.sum()
                val (redundant, chipsRedundant) = calcRedundant(totalSum, personCount, chips)

                val chipsToDiv = chips.without(chipsRedundant)
                    .apply { if (isEmpty()) error("") }

                val (common, spec) = chipsToDiv.divForCommon(personCount)
                    .apply { if (first.isEmpty()) error("") }

                val actionsForSpec = actionsToDivSpec(spec, personCount, common)
                    .map { it.value }

                val d = actionsForSpec.asSequence().distinct()
                    .map { a -> actionsForSpec.count { it == a } to a }.toList()

                val items = mutableListOf(
                    ResultItem(common, personCount, false),
                    ResultItem(chipsRedundant, 0, true)
                )

                for (act in d) {
                    val (persons, actions) = act
                    items.add(ResultItem(actions, persons, false))
                }

                SuccessCalcResult(
                    items,
                    personCount,
                    totalSum
                )
            }
        }
    }

    private fun actionsToDivSpec(
        spec: List<Chip>,
        persons: Int,
        common: List<Chip>
    ): Map<Int, MutableList<Chip>> {
        if (spec.isEmpty()) return mutableMapOf()

        val divTarget = spec.sum() / persons

        val specToDiv = spec.desc()
            .associateBy { it.number }
            .toMutableMap()

        val actions: MutableMap<Int, MutableList<Chip>> = mutableMapOf()

        val specActionsManager = SpecActionsManager(
            actions,
            specToDiv
        )

        for (personI in 0 until persons) {
            var divT = divTarget
            for (chipEntry in specToDiv) {
                val (num, specChip) = chipEntry

                if (specChip.quantity == 0) continue
                if (num == divT) {
                    specActionsManager.giveOne(personI, num)
                    break
                }
                if (num > divT) {
                    specActionsManager.giveOne(personI, num)

                    var diff = num - divT

                    for (commonChip in common.desc()) {
                        if (commonChip.quantity == 0 || commonChip.number > diff) continue
                        if (commonChip.number == diff) {
                            specActionsManager.action(personI, commonChip.number, -1)
                            break
                        }
                        if (commonChip.number < diff) {
                            val q = (diff / commonChip.number).let { q ->
                                if (commonChip.quantity - q < 0) commonChip.quantity else q
                            }

                            specActionsManager.action(personI, commonChip.number, -q)

                            val newDiff = diff - q * num
                            if (newDiff == 0) {
                                break
                            } else {
                                diff = newDiff
                            }
                        }
                    }

                    break
                }
                if (num < divT) {
                    val q = (divT / num).let { q ->
                        if (specChip.quantity - q < 0) specChip.quantity else q
                    }

                    specActionsManager.action(personI, specChip.number, q)

                    val newDiff = divT - q * num
                    if (newDiff == 0) {
                        break
                    } else {
                        divT = newDiff
                    }
                }
            }
        }

        return actions
    }

    private fun calcRedundant(
        totalSum: Int,
        personCount: Int,
        chips: List<Chip>
    ): Pair<Int, List<Chip>> {
        val minNumber = chips.min()
        val redundant = totalSum - totalSum / personCount / minNumber * minNumber * personCount
        val chipsRedundant: MutableList<Chip> = mutableListOf()

        if (redundant > 0) {
            var o = redundant
            loop@ for (c in chips.desc()) {
                when {
                    c.number > o -> continue@loop
                    c.number == o -> {
                        chipsRedundant.add(Chip(c.number, 1))
                        break@loop
                    }
                    c.number < o -> {
                        val r = o / c.number
                        val d = r * c.number
                        chipsRedundant.add(Chip(c.number, r))

                        if (d == o) break@loop else o -= d
                    }
                }
            }
        }

        return redundant to chipsRedundant
    }

    private fun List<Chip>.divForCommon(personCount: Int): Pair<List<Chip>, List<Chip>> {
        val common: MutableList<Chip> = mutableListOf()
        val spec: MutableList<Chip> = mutableListOf()

        forEach { c ->
            val o = c.quantity / personCount
            if (o != 0) common.add(Chip(c.number, o))
            val b = c.quantity - o * personCount
            if (b != 0) spec.add(Chip(c.number, b))
        }

        return common to spec
    }

    private fun valid(chips: List<Chip>, personCount: Int) = chips.isNotEmpty() && personCount > 1

    private fun List<Chip>.desc() = sortedByDescending { it.number }

    private fun List<Chip>.sum() = sumBy { it.number * it.quantity }

    private fun List<Chip>.min() = minBy { it.number }?.number ?: error("")

    private fun List<Chip>.without(chipsRedundant: List<Chip>): List<Chip> {
        if (chipsRedundant.isEmpty()) return this.map { it.copy() }

        val chipsMap = associateBy { it.number }.toMutableMap()
        chipsRedundant.forEach { c ->
            chipsMap[c.number] = c.copy(quantity = (chipsMap[c.number]?.quantity ?: 0) - c.quantity)
        }
        return chipsMap.toList().map { it.second }
    }

    private class SpecActionsManager(
        val actions: MutableMap<Int, MutableList<Chip>>,
        val spec: MutableMap<Int, Chip>
    ) {
        fun action(personI: Int, number: Int, q: Int) {
            val chip = spec[number] ?: if (q < 0) Chip(number, 0) else error("")
            actions.insOrUpdate(personI, Chip(number, q))
            spec[number] = Chip(number, chip.quantity - q)
        }

        fun giveOne(personI: Int, number: Int) {
            action(personI, number, 1)
        }
    }
}

fun MutableMap<Int, MutableList<Chip>>.insOrUpdate(i: Int, c: Chip) {
    this[i] = this[i]?.apply { add(c) } ?: mutableListOf(c)
}
