package com.illiarb.tmdbclient.di.modules

import com.illiarb.tmdbclient.coreimpl.explore.ExploreInteractorImpl
import com.illiarb.tmdbclient.coreimpl.main.MainInteractorImpl
import com.illiarb.tmdbclient.coreimpl.movie.MovieDetailsInteractorImpl
import com.illiarb.tmdbclient.coreimpl.movie.MoviesInteractorImpl
import com.illiarb.tmdblcient.core.modules.explore.ExploreInteractor
import com.illiarb.tmdblcient.core.modules.main.MainInteractor
import com.illiarb.tmdblcient.core.modules.movie.MovieDetailsInteractor
import com.illiarb.tmdblcient.core.modules.movie.MoviesInteractor
import dagger.Binds
import dagger.Module

/**
 * @author ilya-rb on 03.11.18.
 */
@Module
interface InteractorsModule {

    @Binds
    fun bindMoviesInteractor(impl: MoviesInteractorImpl): MoviesInteractor

    @Binds
    fun bindMovieDetailsInteractor(impl: MovieDetailsInteractorImpl): MovieDetailsInteractor

    @Binds
    fun bindExploreInteractor(impl: ExploreInteractorImpl): ExploreInteractor

    @Binds
    fun bindMainInteractor(impl: MainInteractorImpl): MainInteractor
}