package by.aa.pockercon.presentation.chips

import by.aa.pockercon.presentation.base.BaseMvpPresenter
import java.io.Serializable

class ChipsPresenterImpl : BaseMvpPresenter<ChipsView>(), ChipsPresenter {

    override fun firstAttach(view: ChipsView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)
        view.renderAll()
    }

    override fun attach(view: ChipsView) {
        super.attach(view)
        view.renderAll()
    }

    private fun ChipsView.renderAll() {
        //renderItems(emptyList())
    }

    override fun getRetainable(): Serializable? {
        return "123test"
    }
}