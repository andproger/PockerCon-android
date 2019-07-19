package by.aa.pockercon.presentation.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import by.aa.pockercon.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupPresenter()
    }

    override fun onDestroy() {
        if (isChangingConfigurations) {
            presenter.detach()
        } else {
            presenter.destroy()
        }
        super.onDestroy()
    }

    private fun setupPresenter() {
        presenter = (lastCustomNonConfigurationInstance as MainPresenter?)?.apply {
            attach(this@MainActivity)
        } ?: createPresenter().apply {
            firstAttach(this@MainActivity)
        }
    }

    private fun createPresenter(): MainPresenter {
        return MainPresenterImpl()
    }

    private fun initViews() {
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

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter
    }
}
