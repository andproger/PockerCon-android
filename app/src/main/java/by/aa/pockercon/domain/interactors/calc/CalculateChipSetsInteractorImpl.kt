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

                val (common, balanceToDiv) = chipsToDiv.div(personCount)
                    .apply { if (first.isEmpty()) error("") }

                val actionsToDiv: MutableMap<Int, MutableList<Chip>> = mutableMapOf()

                if (balanceToDiv.isNotEmpty()) {
                    balanceToDiv.toDesc()

                    val bSum = balanceToDiv.sum()
                    val oForOne = bSum / personCount

                    for (i in 0..personCount) {
                        for (c in balanceToDiv) {
                            if (c.quantity == 0) continue
                            if (c.number == oForOne) {
                                actionsToDiv.insOrUpdate(i, c.copy(quantity = 1))
                                balanceToDiv[balanceToDiv.indexOf(c)] = c.copy(quantity = c.quantity - 1)
                                break
                            }
                            if (c.number > oForOne) {
                                actionsToDiv.insOrUpdate(i, c.copy(quantity = 1))
                                balanceToDiv[balanceToDiv.indexOf(c)] = c.copy(quantity = c.quantity - 1)

                                var needReturnToDiv = c.number - oForOne

                                val forReturn: MutableList<Chip> = mutableListOf()
                                for (cR in common.desc()) {
                                    if (cR.quantity == 0 || cR.number > needReturnToDiv) continue
                                    if (cR.number == needReturnToDiv) {
                                        forReturn.add(cR.copy(quantity = -1))
                                        break
                                    } else {
                                        val q = needReturnToDiv / cR.number
                                        forReturn.add(cR.copy(quantity = -q))
                                        val newNeedReturn = needReturnToDiv - q * c.number
                                        if (newNeedReturn == 0) {
                                            break
                                        } else {
                                            needReturnToDiv = newNeedReturn
                                        }
                                    }
                                }

                                forReturn.forEach {
                                    actionsToDiv.insOrUpdate(i, it)
                                    val existQ = balanceToDiv.find { f -> f.number == it.number }?.quantity
                                    existQ?.let { q ->
                                        balanceToDiv[balanceToDiv.indexOf(it)] = it.copy(quantity = c.quantity - 1)
                                    }
                                }

                                break
                            }
                        }
                    }
                }

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

    private fun valid(chips: List<Chip>, personCount: Int) = chips.isNotEmpty() && personCount > 1

    private fun MutableMap<Int, MutableList<Chip>>.insOrUpdate(i: Int, c: Chip) {
        this[i] = this[i]?.apply { add(c) } ?: mutableListOf(c)
    }

    /*actionsToDiv[i] = actionsToDiv[i]?.apply { add(c.copy(quantity = 1)) }
                                        ?: mutableListOf(c.copy(quantity = 1))*/
    private fun List<Chip>.desc() = sortedByDescending { it.number }

    private fun MutableList<Chip>.toDesc() = sortByDescending { it.number }

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
}

private fun List<Chip>.div(personCount: Int): Pair<List<Chip>, MutableList<Chip>> {
    val common: MutableList<Chip> = mutableListOf()
    val balanceToDiv: MutableList<Chip> = mutableListOf()

    forEach { c ->
        val o = c.quantity / personCount
        if (o != 0) common.add(Chip(c.number, o))
        val b = c.quantity - o
        if (b != 0) balanceToDiv.add(Chip(c.number, b))
    }

    return common to balanceToDiv
}
