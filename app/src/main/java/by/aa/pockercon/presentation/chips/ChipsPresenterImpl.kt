package by.aa.pockercon.presentation.chips

import by.aa.pockercon.presentation.base.BaseMvpPresenter

class ChipsPresenterImpl : BaseMvpPresenter<ChipsView>(), ChipsPresenter {

    override fun firstAttach(view: ChipsView) {
        super.firstAttach(view)
        view.renderAll()
    }

    override fun attach(view: ChipsView) {
        super.attach(view)
        view.renderAll()
    }

    private fun ChipsView.renderAll() {

    }
}