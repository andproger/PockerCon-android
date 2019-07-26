package by.aa.pockercon.data.cache.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "chips")
data class ChipDataModel(

    @PrimaryKey
    var number: Int,

    var count: Int
)