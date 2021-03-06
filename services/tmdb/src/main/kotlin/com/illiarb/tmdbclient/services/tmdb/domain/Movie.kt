package com.illiarb.tmdbclient.services.tmdb.domain

import com.illiarb.tmdbclient.libs.util.DisplayFormattedDate

data class Movie(
  val id: Int,
  val posterPath: Image?,
  val backdropPath: Image?,
  val genres: List<Genre>,
  val homepage: String?,
  val credits: List<Person>,
  val releaseDate: DisplayFormattedDate,
  val overview: String?,
  val reviews: List<Review>,
  val runtime: Int,
  val title: String,
  val images: List<Image>,
  val voteAverage: Float,
  val country: String?,
  val rating: Int = 0,
  val videos: List<Video>? = null
) {

  companion object {
    const val DELIMITER_SLASH_SPACED = " / "
  }

  fun getGenresString(delimiter: String = DELIMITER_SLASH_SPACED): String? {
    return if (genres.isNotEmpty()) {
      val result = buildString {
        genres.distinctBy { it.id }.forEachIndexed { index, genre ->
          append(genre.name)
          append(" ")

          val emoji = genre.getEmoji()
          emoji?.let {
            append(it)
          }

          if (index < genres.size - 1) {
            append(delimiter)
          }
        }
      }
      result
    } else {
      null
    }
  }
}