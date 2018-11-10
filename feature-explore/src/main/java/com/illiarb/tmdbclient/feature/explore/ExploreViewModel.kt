package com.illiarb.tmdbclient.feature.explore

import com.illiarb.tmdbexplorer.coreui.viewmodel.BaseViewModel
import com.illiarb.tmdblcient.core.entity.Location
import com.illiarb.tmdblcient.core.ext.addTo
import com.illiarb.tmdblcient.core.modules.location.LocationInteractor
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author ilya-rb on 31.10.18.
 */
class ExploreViewModel @Inject constructor(
    private val locationInteractor: LocationInteractor
) : BaseViewModel(), CoroutineScope {

    private val coroutinesJob = Job()
    private val theatersSubject = BehaviorSubject.create<List<Location>>()

    override val coroutineContext: CoroutineContext
        get() = coroutinesJob + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        coroutinesJob.cancel()
    }

    fun observeNearbyTheaters(): Observable<List<Location>> = theatersSubject.hide()

    fun fetchNearbyMovieTheaters() {
        launch(context = coroutineContext) {
            try {
                val result = withContext(Dispatchers.IO) {
                    getNearbyLocationInterop()
                }
                theatersSubject.onNext(result)
            } catch (e: Exception) {
                theatersSubject.onError(e)
            }
        }
    }

    private suspend fun getNearbyLocationInterop(): List<Location> =
        suspendCoroutine { c ->
            locationInteractor.getNearbyMovieTheaters()
                .subscribe(
                    { c.resume(it) },
                    { c.resumeWithException(it) }
                )
                .addTo(clearDisposable)
        }
}