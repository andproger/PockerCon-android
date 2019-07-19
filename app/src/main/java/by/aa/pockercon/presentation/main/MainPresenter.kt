package by.aa.pockercon.presentation.main

interface MainPresenter {
    fun attach(view: MainView)

    fun firstAttach(view: MainView)

    fun detach()

    fun destroy()

    fun onOpenChipsClicked()
}