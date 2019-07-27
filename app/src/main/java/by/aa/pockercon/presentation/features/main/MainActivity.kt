package by.aa.pockercon.presentation.features.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import by.aa.pockercon.R
import by.aa.pockercon.data.cache.repositories.ChipsRepositoryImpl
import by.aa.pockercon.data.cache.repositories.PersonCountRepositoryImpl
import by.aa.pockercon.domain.interactors.calc.CalculateChipSetsInteractorImpl
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
        }
    }

    override fun initViews() {
        setSupportActionBar(toolbar)

        fabAdd.setOnClickListener { presenter.onOpenChipsClicked() }

        imageViewPlus.setOnClickListener { presenter.onPlusPersonClicked() }

        imageViewMinus.setOnClickListener { presenter.onMinusPersonClicked() }
    }

    override fun createPresenter(): MainPresenter {
        val chipsDao = (application as App).getProvider().provideRoomDb().getChipsDao()

        val chipsRepository = ChipsRepositoryImpl(chipsDao)
        val personCountRepository = PersonCountRepositoryImpl()

        return MainPresenterImpl(
            CalculateChipSetsInteractorImpl(chipsRepository, personCountRepository),
            UpdatePersonCountInteractorImpl(personCountRepository)
        )
    }
}
