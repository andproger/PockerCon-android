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

    override fun calculate(): Observable<CalcSetsResult> {
        return Observable.combineLatest(
                chipsRepository.getAllWithUpdates(),
                personCountRepository.getWithUpdates(),
                BiFunction { chips: List<Chip>, personCount: Int ->
                    chips to personCount
                }
        ).switchMap { chipsAndCount ->
            Observable.fromCallable {
                val (chips, count) = chipsAndCount

                CalcSetsResult()
            }
        }
    }
}