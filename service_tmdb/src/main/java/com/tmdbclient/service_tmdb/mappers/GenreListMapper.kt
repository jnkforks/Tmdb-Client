package com.tmdbclient.service_tmdb.mappers

import com.illiarb.tmdblcient.core.domain.Genre
import com.illiarb.tmdblcient.core.util.Mapper
import com.tmdbclient.service_tmdb.model.GenreListModel
import javax.inject.Inject

class GenreListMapper @Inject constructor(
    private val genreMapper: GenreMapper
) : Mapper<GenreListModel, List<Genre>> {

    override fun map(from: GenreListModel): List<Genre> = genreMapper.mapList(from.genres)
}