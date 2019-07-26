package by.aa.pockercon.data.cache.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import by.aa.pockercon.data.cache.datastores.ChipsDao
import by.aa.pockercon.data.cache.entity.ChipDataModel

@Database(entities = [ChipDataModel::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun getChipsDao(): ChipsDao
}