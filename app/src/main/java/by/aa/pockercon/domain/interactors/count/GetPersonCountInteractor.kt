package by.aa.pockercon.domain.interactors.count

import io.reactivex.Observable

interface GetPersonCountInteractor {
    fun get(): Observable<Int>
}