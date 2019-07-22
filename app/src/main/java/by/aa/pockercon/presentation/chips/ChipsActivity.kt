package by.aa.pockercon.presentation.chips

import android.support.v7.widget.StaggeredGridLayoutManager
import by.aa.pockercon.R
import by.aa.pockercon.presentation.base.BaseMvpActivity
import by.aa.pockercon.presentation.base.MvpView
import kotlinx.android.synthetic.main.activity_chips.*

class ChipsActivity : BaseMvpActivity<ChipsView, ChipsPresenter>() {

    override fun contentViewResId() = R.layout.activity_chips

    override fun initViews() {
        initToolbar()
        initRecyclerView()
    }

    override fun createPresenter() = ChipsPresenterImpl()

    override fun getView(mvpView: MvpView): ChipsView {
        return object : MvpView by mvpView, ChipsView {
            override fun renderItems(items: List<ChipViewState>) {
                adapter.update(items)
            }
        }
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
    }

    private val adapter: ChipsAdatpter
        get() = recyclerView.adapter as ChipsAdatpter
}
