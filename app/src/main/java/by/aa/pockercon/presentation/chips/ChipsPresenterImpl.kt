package by.aa.pockercon.presentation.chips

import by.aa.pockercon.domain.entity.Chip
import by.aa.pockercon.domain.interactors.chips.AddChipInteractor
import by.aa.pockercon.domain.interactors.chips.GetChipsInteractor
import by.aa.pockercon.domain.interactors.chips.UpdateChipQuantityInteractor
import by.aa.pockercon.presentation.base.BaseMvpPresenter
import java.io.Serializable

class ChipsPresenterImpl(
    private val addChipInteractor: AddChipInteractor,
    private val updateChipQuantityInteractor: UpdateChipQuantityInteractor,
    private val getChipsInteractor: GetChipsInteractor
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

    private fun ChipsView.renderAll() {
        renderItems(items)
    }

    private fun Chip.toViewState() = ChipViewState(number, quantity)
}