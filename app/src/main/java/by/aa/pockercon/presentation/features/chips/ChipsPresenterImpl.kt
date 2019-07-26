package by.aa.pockercon.presentation.features.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.interactors.chips.*
import by.aa.pockercon.presentation.features.base.BaseMvpPresenter
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

    private val editModel = ChipsEditModel.DEFAULT

    private val compositeDisposable = CompositeDisposable()

    override fun firstAttach(view: ChipsView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)

        subscribeToChips()
    }

    override fun attach(view: ChipsView) {
        super.attach(view)

        view.renderAll()
    }

    override fun onAddClicked() {
        editModel.dialogState = DialogState.ADD_CHIP
        view?.openEditDialog()
    }

    override fun onDeleteItemClicked(number: Int) {
        makeDeleteSingle(number)
    }

    override fun onItemClicked(number: Int) {
        editModel.items.find { c -> c.number == number }?.let { chip ->
            editModel.dialogState = DialogState.EDIT_CHIP
            view?.openEditDialog(chip)
        }
    }

    override fun onEditingComplete(state: ChipViewState) {
        val chip = Chip(state.number, state.count)

        when (editModel.dialogState) {
            DialogState.ADD_CHIP -> makeAddSingle(chip)
            DialogState.EDIT_CHIP -> makeUpdateSinge(chip)
        }

        editModel.dialogState = null
    }

    override fun onEditingCanceled() {
        editModel.dialogState = null
    }

    private fun ChipsView.renderAll() {
        renderItems(editModel.items)
    }

    private fun subscribeToChips() {
        getChipsInteractor.getAllWithUpdates()
            .map { chips -> chips.map { c -> c.toViewState() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { chipStates ->
                editModel.items = chipStates
                view?.renderItems(editModel.items)
            }.let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun makeAddSingle(chip: Chip) {
        addChipInteractor.add(chip)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                if (result == AddResult.ALREADY_EXIST) {
                    view?.showAllreadyExistWarn()
                }
            }.let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun makeUpdateSinge(chip: Chip) {
        updateChipInteractor.update(chip)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun makeDeleteSingle(number: Int) {
        removeChipInteractor.remove(number)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe().let { d ->
                compositeDisposable.add(d)
            }
    }

    override fun destroy() {
        compositeDisposable.dispose()

        super.destroy()
    }

    private fun Chip.toViewState() = ChipViewState(number, quantity)
}