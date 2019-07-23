package by.aa.pockercon.presentation.chips

import by.aa.pockercon.presentation.base.MvpView

interface ChipsView : MvpView {
    fun renderItems(items: List<ChipViewState>)

    fun openAddDialog()
}