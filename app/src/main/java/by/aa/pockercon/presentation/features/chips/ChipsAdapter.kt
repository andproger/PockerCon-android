package by.aa.pockercon.presentation.features.chips

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.aa.pockercon.R
import kotlinx.android.synthetic.main.item_chip_count.view.*

class ChipsAdapter(
    context: Context,
    private val onDeleteClicked: (Int) -> Unit,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<ChipsAdapter.RowViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val items = mutableListOf<ChipViewState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView = inflater.inflate(R.layout.item_chip_count, parent, false)
        return RowViewHolder(itemView, onDeleteClicked, onItemClicked)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.updateWith(items[position])
    }

    fun update(newItems: List<ChipViewState>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class RowViewHolder(
        itemView: View,
        onDeleteClicked: (Int) -> Unit,
        onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private var model: ChipViewState? = null

        init {
            itemView.containerContent.setOnClickListener {
                model?.let { model ->
                    onItemClicked(model.number)
                }
            }

            itemView.containerDelete.setOnClickListener {
                model?.let { model ->
                    onDeleteClicked(model.number)
                }
            }
        }

        fun updateWith(newModel: ChipViewState) {
            model = newModel
            updateUI()
        }

        private fun updateUI() {
            model?.apply {
                itemView.textViewChipNumber.text = number.toString()
                itemView.textViewCount.text = count.toString()
            }
        }
    }
}