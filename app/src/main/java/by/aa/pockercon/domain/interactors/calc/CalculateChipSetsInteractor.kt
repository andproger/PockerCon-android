package by.aa.pockercon.domain.interactors.calc

import io.reactivex.Observable

interface CalculateChipSetsInteractor {
    fun calculate(): Observable<CalcSetsResult>
}

class CalcSetsResult(

)