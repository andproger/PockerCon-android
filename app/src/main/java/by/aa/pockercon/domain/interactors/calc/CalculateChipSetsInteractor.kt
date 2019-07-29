package by.aa.pockercon.domain.interactors.calc

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Observable

interface CalculateChipSetsInteractor {
    fun calculate(): Observable<CalcResult>
}

class CalcResult(
    val items: List<ResultItem>,
    val personCount: Int,
    val summary: Int
)

class ResultItem(
    val chips: List<Chip>,
    val personCount: Int,
    val redundant: Boolean
)