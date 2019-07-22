package by.aa.pockercon.presentation.base

import java.io.Serializable

abstract class BaseMvpPresenter<V : MvpView> : MvpPresenter<V> {

    protected var view: V? = null

    override fun attach(view: V) {
        this.view = view
    }

    override fun firstAttach(view: V, retainedState: Serializable?) {
        this.view = view
    }

    override fun detach() {
        view = null
    }

    override fun destroy() {
        view = null
    }

    override fun getRetainable(): Serializable? = null
}