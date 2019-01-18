package com.illiarb.tmdbexplorer.coreui.base

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbexplorer.coreui.observable.BypassObservable
import com.illiarb.tmdbexplorer.coreui.observable.Cloneable
import com.illiarb.tmdbexplorer.coreui.observable.Observer
import com.illiarb.tmdbexplorer.coreui.observable.StateObservable
import com.illiarb.tmdbexplorer.coreui.uiactions.ShowErrorDialogAction
import com.illiarb.tmdbexplorer.coreui.uiactions.UiAction
import com.illiarb.tmdblcient.core.common.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

/**
 * @author ilya-rb on 07.01.19.
 */
abstract class BasePresentationModel<T : Cloneable<T>> : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    private val stateObservable = StateObservable<T>()

    private val actionsObservable = BypassObservable<UiAction>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun observeActions(observer: Observer<UiAction>) {
        actionsObservable.addObserver(observer)
    }

    fun stopObservingActions(observer: Observer<UiAction>) {
        actionsObservable.removeObserver(observer)
    }

    fun observeState(observer: Observer<T>) {
        stateObservable.addObserver(observer)
    }

    fun stopObserving(observer: Observer<T>) {
        stateObservable.removeObserver(observer)
    }

    fun <T> handleResult(
        result: Result<T>,
        onSuccess: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) {
        when (result) {
            is Result.Success -> onSuccess(result.result)
            is Result.Error -> {
                val throwable = result.error

                if (onError != null) {
                    onError(throwable)
                } else {
                    handleError(throwable)
                }
            }
        }
    }

    protected fun handleError(throwable: Throwable) {
        throwable.message?.let {
            publishAction(ShowErrorDialogAction(it))
        }
    }

    protected fun setIdleState(state: T) {
        stateObservable.accept(state)
    }

    protected fun setState(block: (T) -> T) {
        stateObservable.accept(
            block(
                stateObservable.valueCopy() ?: throw IllegalStateException("initial state was not set")
            )
        )
    }

    private fun publishAction(action: UiAction) {
        actionsObservable.publish(action)
    }
}