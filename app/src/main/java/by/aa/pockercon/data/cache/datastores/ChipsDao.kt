package by.aa.pockercon.data.cache.datastores

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import by.aa.pockercon.data.cache.entity.ChipDataModel
import io.reactivex.Flowable

@Dao
interface ChipsDao {

    @Query("SELECT * FROM chips")
    fun getAll(): List<ChipDataModel>

    @Query("SELECT * FROM chips")
    fun getAllWithUpdates(): Flowable<List<ChipDataModel>>

    @Query("SELECT * FROM chips WHERE number = :number")
    fun getById(number: Int): ChipDataModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(chip: ChipDataModel)

    @Query("DELETE FROM chips WHERE number = :number")
    fun deleteById(number: Int)
}