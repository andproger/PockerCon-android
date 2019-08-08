package by.aa.pockercon.domain.interactors.calc

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Observable

interface CalculateChipSetsInteractor {
    fun calculate(): Observable<CalcState>
}

sealed class CalcState

class SuccessCalcState(
    val items: List<ResultItem>,
    val personCount: Int,
    val summary: Int
) : CalcState()

object ProgressState : CalcState()

class ErrorCalcState(
    val msg: String
) : CalcState()

class ResultItem(
    val chips: List<Chip>,
    val personCount: Int,
    val redundant: Boolean
)