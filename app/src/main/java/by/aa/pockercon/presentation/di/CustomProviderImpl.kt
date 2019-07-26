package by.aa.pockercon.presentation.di

import android.arch.persistence.room.Room
import android.content.Context
import by.aa.pockercon.data.cache.db.RoomDB

class CustomProviderImpl(
    contextParam: Context
) : CustomProvider {
    private var context: Context? = contextParam

    private val instances: MutableMap<String, Any> = mutableMapOf()

    override fun provideRoomDb(): RoomDB {
        return (instances[KEY_DB] as? RoomDB)
            ?: Room.databaseBuilder(
                context!!,
                RoomDB::class.java,
                "chips_db"
            ).build().apply {
                instances[KEY_DB] = this
            }
    }

    override fun dispose() {
        (instances[KEY_DB] as? RoomDB)?.let { db ->
            if (db.isOpen) {
                db.close()
            }
        }

        instances.clear()
        context = null
    }

    companion object {
        const val KEY_DB = "key_db"
    }
}