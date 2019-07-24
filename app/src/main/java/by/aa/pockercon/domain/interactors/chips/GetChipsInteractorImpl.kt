package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import io.reactivex.Observable

class GetChipsInteractorImpl(
    private val chipsRepository: ChipsRepository
) : GetChipsInteractor {

    override fun getAllWithUpdates(): Observable<List<Chip>> {
        return chipsRepository.getAllWithUpdates()
            .map { chips -> chips.sortedBy { it.number } }
    }
}