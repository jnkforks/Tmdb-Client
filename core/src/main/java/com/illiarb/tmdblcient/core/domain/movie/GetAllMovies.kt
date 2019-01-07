package com.illiarb.tmdblcient.core.domain.movie

import com.illiarb.tmdblcient.core.entity.Movie
import com.illiarb.tmdblcient.core.entity.MovieFilter
import com.illiarb.tmdblcient.core.system.NonBlocking

/**
 * @author ilya-rb on 07.01.19.
 */
interface GetAllMovies {

    @NonBlocking
    suspend operator fun invoke(): List<Pair<MovieFilter, List<Movie>>>
}