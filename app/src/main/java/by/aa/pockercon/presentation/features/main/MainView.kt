package by.aa.pockercon.presentation.features.main

import by.aa.pockercon.presentation.features.base.MvpView

interface MainView : MvpView {
    fun openChips()

    fun renderCalcResultItems(items: List<ResultItemViewState>)

    fun renderPersonCount(personCount: Int)

    fun renderSummary(summary: Int)

    fun showProgress(show: Boolean)

    fun showCalcError(calcError: CalcError?)
}

sealed class CalcError {
    class Message(val text: String) : CalcError()

    class NoChips(
        val chipsSum: Int,
        val personCount: Int
    ) : CalcError()
}