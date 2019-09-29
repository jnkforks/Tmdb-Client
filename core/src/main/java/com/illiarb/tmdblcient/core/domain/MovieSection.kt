package com.illiarb.tmdblcient.core.domain

/**
 * @author ilya-rb on 04.11.18.
 */
sealed class MovieSection

class ListSection(val title: String, val movies: List<Movie>) : MovieSection()
class NowPlayingSection(val title: String, val movies: List<Movie>) : MovieSection()
class GenresSection(val genres: List<Genre>) : MovieSection()