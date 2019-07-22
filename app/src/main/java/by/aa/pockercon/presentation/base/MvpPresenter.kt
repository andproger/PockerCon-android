package by.aa.pockercon.presentation.base

interface MvpPresenter<V : MvpView> {
    fun attach(view: V)

    fun firstAttach(view: V)

    fun detach()

    fun destroy()
}