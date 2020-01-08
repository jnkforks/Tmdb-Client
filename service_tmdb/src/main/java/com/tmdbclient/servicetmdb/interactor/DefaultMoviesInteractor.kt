package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.tools.DispatcherProvider
import com.illiarb.tmdblcient.core.util.Result
import com.tmdbclient.servicetmdb.repository.MoviesRepository
import com.tmdbclient.servicetmdb.api.DiscoverApi
import com.tmdbclient.servicetmdb.cache.TmdbCache
import com.tmdbclient.servicetmdb.mappers.MovieMapper
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject

class DefaultMoviesInteractor @Inject constructor(
    private val repository: MoviesRepository,
    private val discoverApi: DiscoverApi,
    private val movieMapper: MovieMapper,
    private val cache: TmdbCache,
    private val dispatcherProvider: DispatcherProvider
) : MoviesInteractor {

    companion object {
        const val KEY_INCLUDE_IMAGES = "images"
    }

    override suspend fun getAllMovies(): Result<List<MovieBlock>> {
        return repository.getMovieFilters().mapOnSuccess { filters ->
            filters.map {
                val moviesByType = when (val result = getMoviesByType(it)) {
                    is Result.Success -> result.data
                    is Result.Error -> Collections.emptyList()
                }
                MovieBlock(it, moviesByType)
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie> {
        val configuration = withContext(dispatcherProvider.io) { cache.getConfiguration() }
        val imageKey = configuration.changeKeys.find { it == KEY_INCLUDE_IMAGES }

        return if (imageKey == null) {
            repository.getMovieDetails(movieId, "")
        } else {
            repository.getMovieDetails(movieId, imageKey)
        }
    }

    override suspend fun discoverMovies(genreId: Int): Result<List<Movie>> {
        return Result.create {
            val id = if (genreId == Genre.GENRE_ALL) null else genreId.toString()
            val movies = discoverApi.discoverMoviesAsync(id).await().results
            movieMapper.mapList(movies)
        }
    }

    private suspend fun getMoviesByType(filter: MovieFilter): Result<List<Movie>> =
        repository.getMoviesByType(filter.code, false)
}