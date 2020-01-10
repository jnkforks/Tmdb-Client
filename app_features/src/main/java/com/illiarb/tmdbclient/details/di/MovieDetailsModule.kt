package com.illiarb.tmdbclient.details.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.details.DefaultDetailsViewModel
import com.illiarb.tmdbexplorer.coreui.di.ViewModelKey
import com.illiarb.tmdblcient.core.analytics.AnalyticsService
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.navigation.Router
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * @author ilya-rb on 08.01.19.
 */
@Module
class MovieDetailsModule(val id: Int) {

    @Provides
    @IntoMap
    @ViewModelKey(DefaultDetailsViewModel::class)
    fun provideMovieDetailsModel(
        interactor: MoviesInteractor,
        router: Router,
        analyticsService: AnalyticsService
    ): ViewModel = DefaultDetailsViewModel(id, interactor, router, analyticsService)
}