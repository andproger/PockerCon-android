package by.aa.pockercon.presentation.features.main

import by.aa.pockercon.presentation.features.base.BaseMvpPresenter
import java.io.Serializable


class MainPresenterImpl : BaseMvpPresenter<MainView>(), MainPresenter {

    override fun firstAttach(view: MainView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)
        view.renderAll()
    }

    override fun attach(view: MainView) {
        super.attach(view)
        view.renderAll()
    }

    override fun onOpenChipsClicked() {
        view?.openChips()
    }

    private fun MainView.renderAll() {

    }
}