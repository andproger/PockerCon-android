package by.aa.pockercon.presentation.features.chips

import android.support.v7.widget.StaggeredGridLayoutManager
import android.widget.Toast
import by.aa.pockercon.R
import by.aa.pockercon.data.cache.repositories.ChipsRepositoryImpl
import by.aa.pockercon.domain.interactors.chips.AddChipInteractorImpl
import by.aa.pockercon.domain.interactors.chips.GetChipsInteractorImpl
import by.aa.pockercon.domain.interactors.chips.RemoveChipInteractorImpl
import by.aa.pockercon.domain.interactors.chips.UpdateChipInteractorImpl
import by.aa.pockercon.presentation.app.App
import by.aa.pockercon.presentation.features.base.BaseMvpActivity
import by.aa.pockercon.presentation.features.base.MvpView
import by.aa.pockercon.presentation.features.chips.add.AddChipDialog
import kotlinx.android.synthetic.main.activity_chips.*

class ChipsActivity : BaseMvpActivity<ChipsView, ChipsPresenter>() {

    override fun contentViewResId() = R.layout.activity_chips

    override fun initViews() {
        initToolbar()
        initRecyclerView()

        fabChips.setOnClickListener { presenter.onAddClicked() }
    }

    override fun getMvpView(mvpView: MvpView): ChipsView {
        return object : MvpView by mvpView, ChipsView {
            override fun renderItems(items: List<ChipViewState>) {
                adapter.update(items)
            }

            override fun openEditDialog(state: ChipViewState?) {
                AddChipDialog(
                        context = this@ChipsActivity,
                        chipViewState = state,
                        onComplete = { newState ->
                            presenter.onEditingComplete(newState)
                        },
                        onDeleteClicked = { number ->
                            presenter.onDeleteItemClicked(number)
                        }
                ).apply {
                    setOnCancelListener {
                        presenter.onEditingCanceled()
                    }
                }.show()
            }

            override fun showAllreadyExistWarn() {
                Toast.makeText(this@ChipsActivity, R.string.allready_exist_warning, Toast.LENGTH_SHORT).show()
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
                context = this,
                onItemClicked = { number ->
                    presenter.onItemClicked(number)
                }
        )
    }

    private val adapter: ChipsAdapter
        get() = recyclerView.adapter as ChipsAdapter

    override fun createPresenter(): ChipsPresenter {
        val chipsDao = (application as App).getProvider().provideRoomDb().getChipsDao()

        val chipRepository = ChipsRepositoryImpl(chipsDao)

        return ChipsPresenterImpl(
                addChipInteractor = AddChipInteractorImpl(chipRepository),
                updateChipInteractor = UpdateChipInteractorImpl(chipRepository),
                getChipsInteractor = GetChipsInteractorImpl(chipRepository),
                removeChipInteractor = RemoveChipInteractorImpl(chipRepository)
        )
    }
}
