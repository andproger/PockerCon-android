package by.aa.pockercon.presentation.di

import by.aa.pockercon.data.cache.db.RoomDB

interface CustomProvider {

    fun provideRoomDb(): RoomDB

    fun dispose()
}