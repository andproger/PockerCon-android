package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository

class GetChipsInteractorImpl(
    private val chipsRepository: ChipsRepository
) : GetChipsInteractor {

    override fun getAll(): List<Chip> {
        return chipsRepository.getAll()
            .sortedBy { it.number }
    }
}