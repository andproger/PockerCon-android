package by.aa.pockercon.domain.interactors.chips

import io.reactivex.Completable

interface RemoveChipInteractor {
    fun remove(number: Int) : Completable
}