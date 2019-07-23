package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip

interface AddChipInteractor {
    fun add(newChip: Chip): AddResult
}

enum class AddResult {
    SUCCESS_ADDED,
    ALREADY_EXIST
}