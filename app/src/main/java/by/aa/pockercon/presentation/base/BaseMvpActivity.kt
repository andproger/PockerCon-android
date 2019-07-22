package by.aa.pockercon.presentation.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

abstract class BaseMvpActivity<V : MvpView, P : MvpPresenter<V>> : AppCompatActivity() {

    protected lateinit var presenter: P

    private var view: V? = null

    private var mvpView: MvpView? = object : MvpView {

    }

    @LayoutRes
    abstract fun contentViewResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentViewResId())

        view = getView(mvpView!!)

        initViews()
        setupPresenter()
    }

    abstract fun initViews()

    private fun setupPresenter() {
        view?.let { view ->
            presenter = (lastCustomNonConfigurationInstance as? P)?.apply {
                attach(view)
            } ?: createPresenter().apply {
                firstAttach(view)
            }
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

        mvpView = null
        view = null
        super.onDestroy()
    }

    abstract fun createPresenter(): P

    abstract fun getView(mvpView: MvpView): V
}