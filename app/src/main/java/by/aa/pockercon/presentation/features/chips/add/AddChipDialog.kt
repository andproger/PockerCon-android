package by.aa.pockercon.presentation.features.chips.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import by.aa.pockercon.R
import by.aa.pockercon.presentation.features.chips.ChipViewState
import kotlinx.android.synthetic.main.add_chip_dialog.*

class AddChipDialog(
        context: Context,
        private val chipViewState: ChipViewState? = null,
        private val onComplete: (ChipViewState) -> Unit,
        private val onDeleteClicked: (Int) -> Unit
) : Dialog(context, R.style.SimpleDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_chip_dialog)

        initViews()
    }

    private fun initViews() {
        chipViewState?.let { state ->
            imageViewDelete.visibility = View.VISIBLE

            editTextValue.setText(state.number.toString())
            editTextValue.isEnabled = false

            editTextCount.setText(state.count.toString())
        }

        imageViewDelete.setOnClickListener {
            chipViewState?.let { state ->
                onDeleteClicked(state.number)
                cancel()
            }
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