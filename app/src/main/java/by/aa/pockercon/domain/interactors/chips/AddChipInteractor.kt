package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip

interface AddChipInteractor {
    fun add(newChip: Chip)
}