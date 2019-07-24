package by.aa.pockercon.domain.interactors.chips

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Observable

interface GetChipsInteractor {
    fun getAllWithUpdates(): Observable<List<Chip>>
}