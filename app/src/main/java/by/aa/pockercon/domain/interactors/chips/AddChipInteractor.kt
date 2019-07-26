package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Single

interface AddChipInteractor {
    fun add(newChip: Chip): Single<AddResult>
}

enum class AddResult {
    SUCCESS_ADDED,
    ALREADY_EXIST
}