package by.aa.pockercon.presentation.features.chips

class ChipsEditModel(
    var items: List<ChipViewState>,
    var dialogState: DialogState?
) {

    companion object {
        val DEFAULT = ChipsEditModel(
            items = emptyList(),
            dialogState = null
        )
    }
}

enum class DialogState {
    ADD_CHIP,
    EDIT_CHIP
}