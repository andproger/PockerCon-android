package by.aa.pockercon.presentation.features.main

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import by.aa.pockercon.R
import by.aa.pockercon.data.cache.repositories.ChipsRepositoryImpl
import by.aa.pockercon.data.cache.repositories.PersonCountRepositoryImpl
import by.aa.pockercon.domain.interactors.calc.CalculateChipSetsInteractorImpl
import by.aa.pockercon.domain.interactors.count.GetPersonCountInteractorImpl
import by.aa.pockercon.domain.interactors.count.UpdatePersonCountInteractorImpl
import by.aa.pockercon.presentation.app.App
import by.aa.pockercon.presentation.features.base.BaseMvpActivity
import by.aa.pockercon.presentation.features.base.MvpView
import by.aa.pockercon.presentation.features.chips.ChipsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<MainView, MainPresenter>() {

    override fun contentViewResId() = R.layout.activity_main

    override fun getMvpView(mvpView: MvpView): MainView {
        return object : MvpView by mvpView, MainView {

            override fun openChips() {
                startActivity(Intent(this@MainActivity, ChipsActivity::class.java))
            }

            override fun renderCalcResultItems(items: List<ResultItemViewState>) {
                adapter.update(items)
            }

            override fun renderPersonCount(personCount: Int) {
                textViewPersonCount.text = personCount.toString()
            }

            override fun renderSummary(summary: Int) {
                textViewSummary.text = summary.toString()
            }

            override fun showProgress(show: Boolean) {
                textViewNoChips.visibility = View.GONE
                progressBar.visibility = if (show) View.VISIBLE else View.GONE
                recyclerView.visibility = if (!show) View.VISIBLE else View.INVISIBLE
            }

            override fun showCalcError(calcError: CalcError?) {
                val showError = calcError != null
                recyclerView.visibility = if (showError) View.INVISIBLE else View.VISIBLE
                textViewNoChips.visibility = if (showError) View.VISIBLE else View.GONE

                if (calcError == null) return

                textViewNoChips.text = when (calcError) {
                    is CalcError.Message -> calcError.text
                    is CalcError.NoChips -> calcError.toMessage()
                }
            }
        }
    }

    override fun initViews() {
        initListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = CalcResultItemsAdapter(this)
    }

    private fun initListeners() {
        fabChips.setOnClickListener { presenter.onOpenChipsClicked() }

        fabPlus.setOnClickListener { presenter.onPlusPersonClicked() }

        fabMinus.setOnClickListener { presenter.onMinusPersonClicked() }
    }

    override fun createPresenter(): MainPresenter {
        val chipsDao = (application as App).getProvider().provideRoomDb().getChipsDao()

        val chipsRepository = ChipsRepositoryImpl(chipsDao)
        val personCountRepository = PersonCountRepositoryImpl(applicationContext)

        return MainPresenterImpl(
            CalculateChipSetsInteractorImpl(chipsRepository, personCountRepository),
            UpdatePersonCountInteractorImpl(personCountRepository),
            GetPersonCountInteractorImpl(personCountRepository)
        )
    }

    private val adapter: CalcResultItemsAdapter
        get() = recyclerView.adapter as CalcResultItemsAdapter

    private fun CalcError.NoChips.toMessage(): String {
        val sum = chipsSum
        val count = personCount

        val noChips = getString(R.string.no_chips_for_div)
        val sumLabel = getString(R.string.sum_of_chips)
        val countLabel = getString(R.string.count_of_persons)

        return "$noChips\n$sumLabel $sum\n$countLabel $count"
    }
}
