package by.aa.pockercon.presentation.base

import java.io.Serializable

interface MvpPresenter<V : MvpView> {
    fun attach(view: V)

    fun firstAttach(view: V, retainedState: Serializable?)

    fun detach()

    fun destroy()

    fun getRetainable(): Serializable?
}