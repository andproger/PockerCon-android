package by.aa.pockercon.presentation.chips

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import by.aa.pockercon.R
import kotlinx.android.synthetic.main.activity_chips.*

class ChipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chips)

        initViews()
    }

    private fun initViews() {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = ChipsAdatpter(this, {})

        adapter.update(
                listOf(
                        ChipViewState(1, 5, 48),
                        ChipViewState(2, 10, 38),
                        ChipViewState(3, 20, 35),
                        ChipViewState(4, 25, 40),
                        ChipViewState(5, 50, 42)
                )
        )
    }

    private val adapter: ChipsAdatpter
        get() = recyclerView.adapter as ChipsAdatpter
}
