package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip

interface GetChipsInteractor {
    fun getAll(): List<Chip>
}