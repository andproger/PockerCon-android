package by.aa.pockercon.domain.interactors.calc

import by.aa.pockercon.domain.entity.Chip
import io.reactivex.Observable

interface CalculateChipSetsInteractor {
    fun calculate(): Observable<CalcState>
}

sealed class CalcState

class SuccessCalcState(
    val items: List<ResultItems>,
    val personCount: Int,
    val summary: Int
) : CalcState()

object ProgressState : CalcState()

class ErrorCalcState(
    val msg: String
) : CalcState()

sealed class ResultItems(
    val chips: List<Chip>
)

class DivItems(
    chips: List<Chip>,
    val personCount: Int
) : ResultItems(chips)

class Redundants(
    chips: List<Chip>
) : ResultItems(chips)