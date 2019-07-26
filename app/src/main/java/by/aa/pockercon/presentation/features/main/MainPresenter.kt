package by.aa.pockercon.presentation.features.main

import by.aa.pockercon.presentation.features.base.MvpPresenter

interface MainPresenter : MvpPresenter<MainView> {

    fun onOpenChipsClicked()
}