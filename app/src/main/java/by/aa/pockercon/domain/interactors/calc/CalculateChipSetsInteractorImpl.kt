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
            personCountRepository.getWithUpdates(),
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

                val items = listOf(
                    ResultItem(common, personCount, false),
                    ResultItem(chipsRedundant, 0, true)
                )

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

        val specToDiv = spec.toMutableList()
            .apply { sortByDescending { it.number } }

        val divTarget = specToDiv.sum() / persons

        val actions: MutableMap<Int, MutableList<Chip>> = mutableMapOf()

        val specActionsManager = SpecActionsManager(
            actions,
            specToDiv.associateBy { it.number }.toMutableMap()
        )

        for (personI in 0..persons) {
            for (specI in 0 until specToDiv.size) {
                val specChip = specToDiv[specI]

                if (specChip.quantity == 0) continue
                if (specChip.number == divTarget) {
                    specActionsManager.giveOne(personI, specI)
                    break
                }
                if (specChip.number > divTarget) {
                    specActionsManager.giveOne(personI, specI)

                    var diff = specChip.number - divTarget
                    val backToSpec: MutableList<Chip> = mutableListOf()

                    for (commonChip in common.desc()) {
                        if (commonChip.quantity == 0 || commonChip.number > diff) continue
                        if (commonChip.number == diff) {
                            backToSpec.add(commonChip.copy(quantity = 1))
                            break
                        }
                        if (commonChip.number < diff) {
                            var q = diff / commonChip.number

                            if (commonChip.quantity - q < 0) q = commonChip.quantity

                            backToSpec.add(commonChip.copy(quantity = q))

                            val newDiff = diff - q * specChip.number
                            if (newDiff == 0) {
                                break
                            } else {
                                diff = newDiff
                            }
                        }
                    }

                    for (backChipI in 0 until backToSpec.size) {
                        val backChip = backToSpec[backChipI]

                        actions.insOrUpdate(personI, backChip.copy(quantity = -backChip.quantity))

                        val existSpecQ = specToDiv.find { f -> f.number == backChip.number }?.quantity

                        if (existSpecQ != null) {
                            val existSpecChipI = specToDiv.indexOfFirst { it.number == backChip.number }
                            specToDiv[existSpecChipI] = backChip.copy(quantity = existSpecQ + backChip.quantity)
                        } else {
                            specToDiv.add(backChip)
                            specToDiv.sortByDescending { it.number }
                        }
                    }

                    break
                }
                if (specChip.number < divTarget) {
                    val backTo: MutableList<Chip> = mutableListOf()

                    var q = divTarget / specChip.number

                    if (specChip.quantity - q < 0) q = specChip.quantity

                    actions.insOrUpdate(personI, specChip.copy(quantity = q))
                    specToDiv[specI] = specChip.copy(quantity = specChip.quantity - q)

                    val newDiff = divTarget - q * specChip.number
                    if (newDiff == 0) {
                        break
                    } else {
                        for (sI in specI until specToDiv.size) {
                            val cc = specToDiv[sI]
                            if (cc.number == newDiff) {
                                specToDiv[sI] = specChip.copy(quantity = specChip.quantity - 1)
                                actions.insOrUpdate(personI, specChip.copy(quantity = 1))
                                break
                            }
                            if (cc.number > newDiff) {

                            }
                        }
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
            val b = c.quantity - o
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
            chipsMap[c.number] = c.copy(quantity = chipsMap[c.number]?.quantity ?: 0-c.quantity)
        }
        return chipsMap.toList().map { it.second }
    }

    private class SpecActionsManager(
        val actions: MutableMap<Int, MutableList<Chip>>,
        val spec: MutableMap<Int, Chip>
    ) {
        fun action(personI: Int, number: Int, q: Int) {
            val chip = spec[number] ?: error("")
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
