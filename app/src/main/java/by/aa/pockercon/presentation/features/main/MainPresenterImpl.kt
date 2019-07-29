package by.aa.pockercon.presentation.features.main

import by.aa.pockercon.domain.interactors.calc.CalculateChipSetsInteractor
import by.aa.pockercon.domain.interactors.count.UpdatePersonCountInteractor
import by.aa.pockercon.presentation.features.base.BaseMvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.Serializable


class MainPresenterImpl(
    private val calculateChipSetsInteractor: CalculateChipSetsInteractor,
    private val updatePersonCountInteractor: UpdatePersonCountInteractor
) : BaseMvpPresenter<MainView>(), MainPresenter {

    private val personsIncSubject = BehaviorSubject.create<IncOperation>()

    private val compositeDisposable = CompositeDisposable()

    override fun firstAttach(view: MainView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)

        subscribeToCalculation()
        subscribeToChangePersonCount()
    }

    override fun destroy() {
        compositeDisposable.dispose()

        super.destroy()
    }

    override fun onOpenChipsClicked() {
        view?.openChips()
    }

    override fun onPlusPersonClicked() {
        personsIncSubject.onNext(IncOperation.PLUS)
    }

    override fun onMinusPersonClicked() {
        personsIncSubject.onNext(IncOperation.MINUS)
    }

    private fun subscribeToCalculation() {
        calculateChipSetsInteractor.calculate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { calcResult ->

                val items = calcResult.items.map { resultItem ->
                    ResultItemViewState(
                        chipCounts = resultItem.chips.map {
                            ChipCountViewState(it.number, it.quantity)
                        },
                        personCount = resultItem.personCount,
                        redundant = resultItem.redundant
                    )
                }

                view?.renderSummary(calcResult.summary)
                view?.renderPersonCount(calcResult.personCount)
                view?.renderCalcResultItems(items)
            }.let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun subscribeToChangePersonCount() {
        personsIncSubject.switchMap { incOperation ->
            Observable.fromCallable {
                when (incOperation) {
                    IncOperation.PLUS -> {
                        updatePersonCountInteractor.plus()
                    }
                    IncOperation.MINUS -> {
                        updatePersonCountInteractor.minus()
                    }
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
            .let { d ->
                compositeDisposable.add(d)
            }
    }

    enum class IncOperation {
        PLUS,
        MINUS
    }
}