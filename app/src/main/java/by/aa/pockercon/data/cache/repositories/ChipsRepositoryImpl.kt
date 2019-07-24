package by.aa.pockercon.data.cache.repositories

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import io.reactivex.Observable

class ChipsRepositoryImpl : ChipsRepository {
    override fun save(chip: Chip) {
        //TODO
    }

    override fun getAll(): List<Chip> {
        //TODO
        return listOf(
            Chip(20, 13),
            Chip(10, 12),
            Chip(5, 11),
            Chip(50, 10),
            Chip(25, 9)
        )
    }

    override fun getAllWithUpdates(): Observable<List<Chip>> {
        return Observable.fromCallable {
            listOf(
                Chip(20, 13),
                Chip(10, 12),
                Chip(5, 11),
                Chip(50, 10),
                Chip(25, 9)
            )
        }
    }

    override fun getById(number: Int): Chip? {
        //TODO
        return null
    }

    override fun deleteById(number: Int) {
        //TODO
    }
}