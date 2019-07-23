package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository

class AddChipInteractorImpl(
    private val chipsRepository: ChipsRepository
) : AddChipInteractor {

    override fun add(newChip: Chip) {
        if (chipsRepository.getById(newChip.number) == null) {
            chipsRepository.save(newChip)
        }
    }
}