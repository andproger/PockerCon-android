package by.aa.pockercon.presentation.chips

import android.support.v7.widget.StaggeredGridLayoutManager
import by.aa.pockercon.R
import by.aa.pockercon.data.cache.repositories.ChipsRepositoryImpl
import by.aa.pockercon.domain.interactors.chips.AddChipInteractorImpl
import by.aa.pockercon.domain.interactors.chips.GetChipsInteractorImpl
import by.aa.pockercon.domain.interactors.chips.RemoveChipInteractorImpl
import by.aa.pockercon.domain.interactors.chips.UpdateChipInteractorImpl
import by.aa.pockercon.presentation.base.BaseMvpActivity
import by.aa.pockercon.presentation.base.MvpView
import by.aa.pockercon.presentation.chips.add.AddChipDialog
import kotlinx.android.synthetic.main.activity_chips.*

class ChipsActivity : BaseMvpActivity<ChipsView, ChipsPresenter>() {

    override fun contentViewResId() = R.layout.activity_chips

    override fun initViews() {
        initToolbar()
        initRecyclerView()

        fabAdd.setOnClickListener { presenter.onAddClicked() }
    }

    override fun getMvpView(mvpView: MvpView): ChipsView {
        return object : MvpView by mvpView, ChipsView {
            override fun renderItems(items: List<ChipViewState>) {
                adapter.update(items)
            }

            override fun openAddDialog(chip: ChipViewState?) {
                AddChipDialog(this@ChipsActivity, chip).show()
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
        recyclerView.adapter = ChipsAdapter(
            this,
            onDeleteClicked = { number ->
                presenter.onDeleteItemClicked(number)
            },
            onItemClicked = { number ->
                presenter.onItemClicked(number)
            }
        )
    }

    private val adapter: ChipsAdapter
        get() = recyclerView.adapter as ChipsAdapter

    override fun createPresenter(): ChipsPresenter {
        val chipRepository = ChipsRepositoryImpl()

        return ChipsPresenterImpl(
            addChipInteractor = AddChipInteractorImpl(chipRepository),
            updateChipInteractor = UpdateChipInteractorImpl(chipRepository),
            getChipsInteractor = GetChipsInteractorImpl(chipRepository),
            removeChipInteractor = RemoveChipInteractorImpl(chipRepository)
        )
    }
}
