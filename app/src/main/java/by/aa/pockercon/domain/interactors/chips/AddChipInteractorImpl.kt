package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import io.reactivex.Single

class AddChipInteractorImpl(
    private val chipsRepository: ChipsRepository
) : AddChipInteractor {

    override fun add(newChip: Chip): Single<AddResult> {
        return Single.fromCallable {
            if (chipsRepository.getById(newChip.number) != null) {
                AddResult.ALREADY_EXIST
            } else {
                chipsRepository.save(newChip)
                AddResult.SUCCESS_ADDED
            }
        }
    }
}