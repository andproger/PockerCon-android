package by.aa.pockercon.presentation.features.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import java.io.Serializable

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

        view = getMvpView(mvpView!!)

        initViews()
        setupPresenter(savedInstanceState?.getSerializable(RETAIN_STATE))
    }

    abstract fun initViews()

    private fun setupPresenter(retainedState: Serializable?) {
        view?.let { view ->
            presenter = (lastCustomNonConfigurationInstance as? P)?.apply {
                attach(view)
            } ?: createPresenter().apply {
                firstAttach(view, retainedState)
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

    override fun onSaveInstanceState(outState: Bundle?) {
        if (!isChangingConfigurations) {
            presenter.getRetainable()?.let { state ->
                outState?.putSerializable(RETAIN_STATE, state)
            }
        }
        super.onSaveInstanceState(outState)
    }

    abstract fun createPresenter(): P

    abstract fun getMvpView(mvpView: MvpView): V

    companion object {
        const val RETAIN_STATE = "retain_state"
    }
}