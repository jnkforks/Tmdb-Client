package com.illiarb.tmdbcliient.coretest.tmdb

import com.illiarb.tmdbcliient.coretest.entity.FakeEntityFactory
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.domain.MovieFilter
import com.illiarb.tmdblcient.core.domain.Review
import com.tmdbclient.service_tmdb.MoviesRepository
import java.util.Collections
import java.util.Random

@Suppress("MagicNumber")
class TestMovieRepository : MoviesRepository {

    private val movieFilters = listOf(
        MovieFilter("Popular", MovieFilter.TYPE_POPULAR),
        MovieFilter("Now playing", MovieFilter.TYPE_NOW_PLAYING),
        MovieFilter("Upcoming", MovieFilter.TYPE_UPCOMING),
        MovieFilter("Top rated", MovieFilter.TYPE_TOP_RATED)
    )

    override suspend fun getMoviesByType(type: String, refresh: Boolean): List<Movie> {
        val random = Random()
        val size = random.nextInt(10)

        return mutableListOf<Movie>().apply {
            for (i in 0..size) {
                add(FakeEntityFactory.createFakeMovie())
            }
        }
    }

    override suspend fun getMovieDetails(id: Int, appendToResponse: String): Movie =
        FakeEntityFactory.createFakeMovie()

    override suspend fun getMovieReviews(id: Int): List<Review> = Collections.emptyList()

    override suspend fun getMovieFilters(): List<MovieFilter> = movieFilters
}