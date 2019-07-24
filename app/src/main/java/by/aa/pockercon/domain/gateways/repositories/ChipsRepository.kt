package by.aa.pockercon.domain.gateways.repositories

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Observable

interface ChipsRepository {
    fun save(chip: Chip)

    fun getAll(): List<Chip>

    fun getAllWithUpdates(): Observable<List<Chip>>

    fun getById(number: Int): Chip?

    fun deleteById(number: Int)
}