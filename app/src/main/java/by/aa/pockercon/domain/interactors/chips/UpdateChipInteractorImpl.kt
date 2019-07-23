package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository

class UpdateChipInteractorImpl(
    private val chipsRepository: ChipsRepository
) : UpdateChipInteractor {

    override fun update(chipToUpdate: Chip) {
        chipsRepository.save(chipToUpdate)
    }
}