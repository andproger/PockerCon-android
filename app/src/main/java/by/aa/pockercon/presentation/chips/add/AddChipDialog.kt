package by.aa.pockercon.presentation.chips.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import by.aa.pockercon.R
import by.aa.pockercon.presentation.chips.ChipViewState
import kotlinx.android.synthetic.main.add_chip_dialog.*

class AddChipDialog(
        context: Context,
        private val chipViewState: ChipViewState? = null,
        private val onComplete: (ChipViewState) -> Unit
) : Dialog(context, R.style.SimpleDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_chip_dialog)

        initViews()
    }

    private fun initViews() {
        chipViewState?.let { state ->
            editTextValue.setText(state.number.toString())
            editTextValue.isEnabled = false

            editTextCount.setText(state.count.toString())
        }

        buttonOk.setOnClickListener {
            val number = editTextValue.text.toString().toIntOrNull() ?: 0
            val count = editTextCount.text.toString().toIntOrNull() ?: 0

            if (number == 0 || count == 0) return@setOnClickListener

            onComplete(ChipViewState(number, count))
            cancel()
        }

        buttonCancel.setOnClickListener { cancel() }
    }
}