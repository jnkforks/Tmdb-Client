package com.illiarb.tmdbclient.services.tmdb.domain

/**
 * @author ilya-rb on 18.02.19.
 */
data class MovieBlock(val filter: MovieFilter, val movies: List<Movie>)