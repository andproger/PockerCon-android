package by.aa.pockercon.presentation.chips

import by.aa.pockercon.presentation.base.MvpPresenter

interface ChipsPresenter : MvpPresenter<ChipsView> {
    fun onDeleteItemClicked(number: Int)

    fun onAddClicked()

    fun onItemClicked(number: Int)

    fun onEditingComplete(state: ChipViewState)

    fun onEditingCanceled()
}