package by.aa.pockercon.presentation.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.interactors.chips.AddChipInteractor
import by.aa.pockercon.domain.interactors.chips.GetChipsInteractor
import by.aa.pockercon.domain.interactors.chips.RemoveChipInteractor
import by.aa.pockercon.domain.interactors.chips.UpdateChipInteractor
import by.aa.pockercon.presentation.base.BaseMvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

class ChipsPresenterImpl(
    private val addChipInteractor: AddChipInteractor,
    private val updateChipInteractor: UpdateChipInteractor,
    private val getChipsInteractor: GetChipsInteractor,
    private val removeChipInteractor: RemoveChipInteractor
) : BaseMvpPresenter<ChipsView>(), ChipsPresenter {

    private val compositeDisposable = CompositeDisposable()

    private var items: List<ChipViewState> = emptyList()

    override fun firstAttach(view: ChipsView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)

        subscribeToChips()
    }

    private fun subscribeToChips() {
        getChipsInteractor.getAllWithUpdates()
            .map { chips -> chips.map { c -> c.toViewState() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { chipStates ->
                items = chipStates
                view?.renderItems(items)
            }.let { d ->
                compositeDisposable.add(d)
            }
    }

    override fun attach(view: ChipsView) {
        super.attach(view)

        view.renderAll()
    }

    override fun onAddClicked() {
        view?.openAddDialog()
    }

    override fun onDeleteItemClicked(number: Int) {
        removeChipInteractor.remove(number)
    }

    override fun onItemClicked(number: Int) {
        items.find { c -> c.number == number }?.let { chip ->
            view?.openAddDialog(chip)
        }
    }

    private fun ChipsView.renderAll() {
        renderItems(items)
    }

    private fun Chip.toViewState() = ChipViewState(number, quantity)

    override fun destroy() {
        compositeDisposable.dispose()

        super.destroy()
    }
}