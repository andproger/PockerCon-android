package by.aa.pockercon.data.cache.repositories

import by.aa.pockercon.data.cache.datastores.ChipsDao
import by.aa.pockercon.data.cache.entity.ChipDataModel
import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.gateways.repositories.ChipsRepository
import io.reactivex.Observable

class ChipsRepositoryImpl(
    private val chipsDao: ChipsDao
) : ChipsRepository {
    override fun save(chip: Chip) {
        chipsDao.insert(chip.toData())
    }

    override fun getAll(): List<Chip> {
        return chipsDao.getAll().map { it.toCore() }
    }

    override fun getAllWithUpdates(): Observable<List<Chip>> {
        return chipsDao.getAllWithUpdates().map { models ->
            models.map { it.toCore() }
        }.toObservable()
    }

    override fun getById(number: Int): Chip? {
        return chipsDao.getById(number)?.toCore()
    }

    override fun deleteById(number: Int) {
        chipsDao.deleteById(number)
    }

    private fun ChipDataModel.toCore() = Chip(number, count)

    private fun Chip.toData() = ChipDataModel(number, quantity)
}