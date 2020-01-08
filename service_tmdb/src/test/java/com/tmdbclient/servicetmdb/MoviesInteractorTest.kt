package com.tmdbclient.servicetmdb

import com.illiarb.tmdbcliient.coretest.TestDependencyProvider
import com.illiarb.tmdblcient.core.domain.Genre
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.configuration.Configuration
import com.tmdbclient.servicetmdb.configuration.ImageUrlCreator
import com.tmdbclient.servicetmdb.interactor.DefaultMoviesInteractor
import com.tmdbclient.servicetmdb.mappers.GenreMapper
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import com.tmdbclient.servicetmdb.mappers.PersonMapper
import com.tmdbclient.servicetmdb.mappers.ReviewMapper
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MoviesInteractorTest {

    private val cache = mock<TmdbCache>()
    private val discoverApi = mock<DiscoverApi>()
    private val moviesRepository = TestDependencyProvider.provideMovieRepository()

    private val interactor = DefaultMoviesInteractor(
        moviesRepository,
        discoverApi,
        MovieMapper(
            GenreMapper(),
            PersonMapper(),
            ReviewMapper(),
            ImageUrlCreator(cache)
        ),
        cache,
        TestDependencyProvider.provideDispatcherProvider()
    )

    @Test
    fun `should not pass genre id if all genres are selected`() = runBlockingTest {
        interactor.discoverMovies(Genre.GENRE_ALL)

        @Suppress("DeferredResultUnused")
        verify(discoverApi).discoverMoviesAsync(null)
    }

    @Test
    fun `should include images to response if change key is present`() = runBlockingTest {
        val id = 1
        val configuration = Configuration(changeKeys = listOf("images"))

        whenever(cache.getConfiguration()).thenReturn(configuration)

        val details = interactor.getMovieDetails(id).getOrThrow()
        assertTrue(details.images.isNotEmpty())
    }
}