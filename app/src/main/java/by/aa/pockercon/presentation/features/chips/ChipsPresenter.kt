package by.aa.pockercon.presentation.features.chips

import by.aa.pockercon.presentation.features.base.MvpPresenter

interface ChipsPresenter : MvpPresenter<ChipsView> {
    fun onDeleteItemClicked(number: Int)

    fun onAddClicked()

    fun onItemClicked(number: Int)

    fun onEditingComplete(state: ChipViewState)

    fun onEditingCanceled()
}