package by.aa.pockercon.presentation.chips.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import by.aa.pockercon.R
import by.aa.pockercon.presentation.chips.ChipViewState

class AddChipDialog(context: Context, chipViewState: ChipViewState? = null) : Dialog(context, R.style.SimpleDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_chip_dialog)
    }
}