package by.aa.pockercon.presentation.features.main

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import by.aa.pockercon.R
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_chips -> {
                presenter.onOpenChipsClicked()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun createPresenter() = MainPresenterImpl()
}
