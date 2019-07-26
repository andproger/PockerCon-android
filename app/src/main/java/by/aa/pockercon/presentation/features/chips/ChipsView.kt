package by.aa.pockercon.presentation.features.chips

import by.aa.pockercon.presentation.features.base.MvpView

interface ChipsView : MvpView {
    fun renderItems(items: List<ChipViewState>)

    fun openEditDialog(state: ChipViewState? = null)

    fun showAllreadyExistWarn()
}