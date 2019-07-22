package by.aa.pockercon.domain.gateways.repositories

import by.aa.pockercon.domain.entity.Chip

interface ChipsRepository {
    fun save(chip: Chip)

    fun getAll(): List<Chip>

    fun getById(number: Int): Chip?
}