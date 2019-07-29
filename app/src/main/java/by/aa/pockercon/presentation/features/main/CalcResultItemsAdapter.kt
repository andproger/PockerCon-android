package by.aa.pockercon.presentation.features.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.aa.pockercon.R
import kotlinx.android.synthetic.main.include_item_chip_count.view.*
import kotlinx.android.synthetic.main.item_calc_result.view.*

class CalcResultItemsAdapter(
    context: Context
) : RecyclerView.Adapter<CalcResultItemsAdapter.ItemViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val items = mutableListOf<ResultItemViewState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = inflater.inflate(R.layout.item_calc_result, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.updateWith(items[position])
    }

    fun update(newItems: List<ResultItemViewState>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var model: ResultItemViewState? = null

        fun updateWith(newModel: ResultItemViewState) {
            model = newModel
            updateUI()
        }

        private fun updateUI() {
            model?.apply {
                renderChipCounts(chipCounts)
                itemView.textViewPersonCount.text = personCountState.personCount.toString()

                itemView.groupPersonCount.visibility =
                    if (personCountState.visible) View.VISIBLE else View.GONE
            }
        }

        private fun renderChipCounts(chipCounts: List<ChipCountViewState>) {
            val grid = itemView.gridLayoutSet

            val context = itemView.context
            val inflater = LayoutInflater.from(context)

            grid.removeAllViews()

            chipCounts.forEach { chipCount ->
                val countView = inflater.inflate(R.layout.include_item_chip_count, grid, false) as ViewGroup
                countView.textViewCount.text = chipCount.count.toString()
                countView.textViewChipNumber.text = chipCount.number.toString()
                grid.addView(countView)
            }
        }
    }
}

class ResultItemViewState(
    val chipCounts: List<ChipCountViewState>,
    val personCountState: PersonCountState
)

class ChipCountViewState(
    val number: Int,
    val count: Int
)

class PersonCountState(
    val personCount: Int,
    val visible: Boolean
)