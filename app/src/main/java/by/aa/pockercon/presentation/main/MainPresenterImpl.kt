package by.aa.pockercon.presentation.main


class MainPresenterImpl : MainPresenter {

    private var view: MainView? = null

    override fun attach(view: MainView) {
        this.view = view
        view.renderAll()
    }

    override fun firstAttach(view: MainView) {
        this.view = view
        view.renderAll()
    }

    override fun onOpenChipsClicked() {
        view?.openChips()
    }

    override fun detach() {
        view = null
    }

    override fun destroy() {
        view = null
    }

    private fun MainView.renderAll() {

    }
}