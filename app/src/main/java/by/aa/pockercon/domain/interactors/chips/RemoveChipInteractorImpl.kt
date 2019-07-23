package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.gateways.repositories.ChipsRepository

class RemoveChipInteractorImpl(
    private val chipsRepository: ChipsRepository
) : RemoveChipInteractor {
    override fun remove(number: Int) {
        chipsRepository.deleteById(number)
    }
}