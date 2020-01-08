package com.tmdbclient.servicetmdb.interactor

import com.illiarb.tmdblcient.core.interactor.GenresInteractor
import com.illiarb.tmdblcient.core.interactor.HomeInteractor
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.interactor.TrendingInteractor
import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.domain.GenresSection
import com.illiarb.tmdblcient.core.domain.ListSection
import com.illiarb.tmdblcient.core.domain.MovieBlock
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.NowPlayingSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class DefaultHomeInteractor @Inject constructor(
    private val moviesInteractor: MoviesInteractor,
    private val genresInteractor: GenresInteractor,
    private val trendingInteractor: TrendingInteractor
) : HomeInteractor {

    companion object {
        // Max genres displayed in the section
        private const val GENRES_MAX_SIZE = 8
    }

    override suspend fun getHomeSections(): Result<List<MovieSection>> = coroutineScope {
        Result.create {
            val movies = async { moviesInteractor.getAllMovies().getOrThrow() }
            val genres = async { genresInteractor.getAllGenres().getOrThrow() }
            val trending = async { trendingInteractor.getTrending().getOrThrow() }
            createMovieSections(movies.await(), genres.await(), trending.await())
        }
    }

    private fun createMovieSections(
        movieBlocks: List<MovieBlock>,
        genres: List<Genre>,
        trending: List<TrendingItem>
    ): List<MovieSection> {
        return movieBlocks
            .filter { it.movies.isNotEmpty() }
            .map {
                // Now playing section should always be on top
                if (it.filter.isNowPlaying()) {
                    NowPlayingSection(it.filter.name, it.movies)
                } else {
                    ListSection(it.filter.code, it.filter.name, it.movies)
                }
            }
            .toMutableList()
            .also {
                if (genres.isNotEmpty()) {
                    if (genres.size > GENRES_MAX_SIZE) {
                        it.add(1, GenresSection(genres.subList(0, GENRES_MAX_SIZE)))
                    } else {
                        it.add(1, GenresSection(genres))
                    }
                }
            }
            .also {
                if (trending.isNotEmpty()) {
                    it.add(0, TrendingSection(trending))
                }
            }
    }
}