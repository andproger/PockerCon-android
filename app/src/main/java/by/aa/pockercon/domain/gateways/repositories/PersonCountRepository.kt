package by.aa.pockercon.domain.gateways.repositories

import io.reactivex.Observable

interface PersonCountRepository {

    fun get(): Int

    fun getWithUpdates(): Observable<Int>

    fun save(count: Int)
}