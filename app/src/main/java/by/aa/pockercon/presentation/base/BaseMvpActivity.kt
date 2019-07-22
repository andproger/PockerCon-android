package by.aa.pockercon.presentation.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import by.aa.pockercon.R

abstract class BaseMvpActivity<V : MvpView, P : MvpPresenter<V>> : AppCompatActivity() {

    private lateinit var presenter: P

    private lateinit var view: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view = getMvpView()

        //initViews()
        setupPresenter()
    }

    private fun setupPresenter() {
        presenter = (lastCustomNonConfigurationInstance as P?)?.apply {
            attach(view)
        } ?: createPresenter().apply {
            firstAttach(view)
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any {
        return presenter
    }

    override fun onDestroy() {
        if (isChangingConfigurations) {
            presenter.detach()
        } else {
            presenter.destroy()
        }
        super.onDestroy()
    }

    abstract fun createPresenter(): P

    abstract fun getMvpView(): V
}