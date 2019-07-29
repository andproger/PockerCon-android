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
                val (chips, count) = chipsAndCount

                val sum = chips.sumBy { it.number * it.quantity }

                val items = listOf(
                        ResultItem(chips, count, false),
                        ResultItem(listOf(Chip(5, 1)), 0, true)
                )

                CalcResult(
                        items,
                        count,
                        sum
                )
            }
        }
    }
}