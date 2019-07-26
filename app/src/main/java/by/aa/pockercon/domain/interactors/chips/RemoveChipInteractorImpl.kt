package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import io.reactivex.Completable

class RemoveChipInteractorImpl(
    private val chipsRepository: ChipsRepository
) : RemoveChipInteractor {
    override fun remove(number: Int): Completable {
        return Completable.fromCallable {
            chipsRepository.deleteById(number)
        }
    }
}