package by.aa.pockercon.presentation.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.interactors.chips.AddChipInteractor
import by.aa.pockercon.domain.interactors.chips.GetChipsInteractor
import by.aa.pockercon.domain.interactors.chips.RemoveChipInteractor
import by.aa.pockercon.domain.interactors.chips.UpdateChipInteractor
import by.aa.pockercon.presentation.base.BaseMvpPresenter
import java.io.Serializable

class ChipsPresenterImpl(
    private val addChipInteractor: AddChipInteractor,
    private val updateChipInteractor: UpdateChipInteractor,
    private val getChipsInteractor: GetChipsInteractor,
    private val removeChipInteractor: RemoveChipInteractor
) : BaseMvpPresenter<ChipsView>(), ChipsPresenter {

    private var items: List<ChipViewState> = emptyList()

    override fun firstAttach(view: ChipsView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)

        items = getChipsInteractor.getAll()
            .map { it.toViewState() }

        view.renderItems(items)
    }

    override fun attach(view: ChipsView) {
        super.attach(view)

        view.renderAll()
    }

    override fun onDeleteItemClicked(number: Int) {
        removeChipInteractor.remove(number)
    }

    private fun ChipsView.renderAll() {
        renderItems(items)
    }

    private fun Chip.toViewState() = ChipViewState(number, quantity)
}