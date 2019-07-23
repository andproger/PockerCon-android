package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip

interface UpdateChipInteractor {
    fun update(chipToUpdate: Chip)
}