package by.aa.pockercon.presentation.features.main

import by.aa.pockercon.domain.interactors.calc.*
import by.aa.pockercon.domain.interactors.count.GetPersonCountInteractor
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
    private val updatePersonCountInteractor: UpdatePersonCountInteractor,
    private val getPersonCountInteractor: GetPersonCountInteractor
) : BaseMvpPresenter<MainView>(), MainPresenter {

    private val editModel = MainEditModel.DEFAULT

    private val personsIncSubject = BehaviorSubject.create<Int>()

    private val compositeDisposable = CompositeDisposable()

    override fun firstAttach(view: MainView, retainedState: Serializable?) {
        super.firstAttach(view, retainedState)

        subscribeToCalculation()
        subscribeToChangePersonCount()
        subscribeToPersonCountUpdates()
    }

    override fun destroy() {
        compositeDisposable.dispose()

        super.destroy()
    }

    override fun onOpenChipsClicked() {
        view?.openChips()
    }

    override fun onPlusPersonClicked() {
        personsIncSubject.onNext(editModel.personCount.get() + 1)
    }

    override fun onMinusPersonClicked() {
        personsIncSubject.onNext(editModel.personCount.get() - 1)
    }

    private fun subscribeToCalculation() {
        calculateChipSetsInteractor.calculate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { calcResult ->
                when (calcResult) {
                    is SuccessCalcState -> {
                        val hasOnlyOneSet = calcResult.items.count { it is DivItems } == 1

                        val items = calcResult.items.map { resultItem ->
                            val personCountVisible = !(resultItem is Redundants || hasOnlyOneSet)

                            when (resultItem) {
                                is DivItems -> ResultItemViewState(
                                    chipCounts = resultItem.chips.map {
                                        ChipCountViewState(it.number, it.quantity)
                                    },
                                    personCountState = PersonCountState(
                                        personCount = resultItem.personCount,
                                        visible = personCountVisible
                                    ),
                                    redundant = false
                                )
                                is Redundants -> ResultItemViewState(
                                    chipCounts = resultItem.chips.map {
                                        ChipCountViewState(it.number, it.quantity)
                                    },
                                    personCountState = PersonCountState(
                                        personCount = 0,
                                        visible = personCountVisible
                                    ),
                                    redundant = true
                                )
                            }
                        }

                        view?.showProgress(false)
                        view?.renderSummary(calcResult.summary)
                        view?.renderCalcResultItems(items)
                    }
                    is ErrorCalcState -> {
                        view?.renderPersonCount(999)//TODO error message
                        view?.showProgress(false)
                    }
                    is ProgressState -> {
                        view?.showProgress(true)
                    }
                }
            }.let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun subscribeToChangePersonCount() {
        personsIncSubject
            .filter { it in 2..29 }
            .doOnNext { personCount ->
                editModel.personCount.input(personCount)
                renderCount()
            }.switchMap { personCount ->
                Observable.fromCallable {
                    updatePersonCountInteractor.update(personCount)
                    personCount
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { personCount ->
                editModel.personCount.complete(personCount)
            }
            .let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun subscribeToPersonCountUpdates() {
        getPersonCountInteractor.get()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                editModel.personCount.render(count) {
                    renderCount()
                }
            }
            .let { d ->
                compositeDisposable.add(d)
            }
    }

    private fun renderCount() {
        view?.renderPersonCount(editModel.personCount.get())
    }
}