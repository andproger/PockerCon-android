package by.aa.pockercon.presentation.main

import by.aa.pockercon.presentation.base.BaseMvpPresenter


class MainPresenterImpl : BaseMvpPresenter<MainView>(), MainPresenter {

    override fun firstAttach(view: MainView) {
        super.firstAttach(view)
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