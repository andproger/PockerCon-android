package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Completable

interface UpdateChipInteractor {
    fun update(chipToUpdate: Chip) : Completable
}