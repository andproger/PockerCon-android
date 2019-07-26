package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import io.reactivex.Completable

class UpdateChipInteractorImpl(
    private val chipsRepository: ChipsRepository
) : UpdateChipInteractor {

    override fun update(chipToUpdate: Chip): Completable {
        return Completable.fromCallable {
            chipsRepository.save(chipToUpdate)
        }
    }
}