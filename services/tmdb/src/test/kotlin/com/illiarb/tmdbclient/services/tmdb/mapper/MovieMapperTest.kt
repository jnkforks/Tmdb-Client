package com.illiarb.tmdbclient.services.tmdb.mapper

import com.google.common.truth.Correspondence
import com.google.common.truth.Truth.assertThat
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageConfig
import com.illiarb.tmdbclient.services.tmdb.internal.image.ImageUrlCreator
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.GenreMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.MovieMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.PersonMapper
import com.illiarb.tmdbclient.services.tmdb.internal.mappers.ReviewMapper
import com.illiarb.tmdbclient.services.tmdb.internal.model.BackdropListModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.BackdropModel
import com.illiarb.tmdbclient.services.tmdb.internal.model.Configuration
import com.illiarb.tmdbclient.services.tmdb.internal.model.MovieModel
import com.illiarb.tmdbclient.services.tmdb.internal.util.TmdbDateFormatter
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieMapperTest {

  private val secureBaseUrl = "https://base-url.com"
  private val imageUrlCreator = ImageUrlCreator()
  private val movieMapper =
    MovieMapper(GenreMapper(), PersonMapper(), ReviewMapper(), imageUrlCreator, TmdbDateFormatter())

  private val configuration: Configuration =
    Configuration(
      images = ImageConfig(secureBaseUrl = secureBaseUrl)
    )

  @Test
  fun `it should append url to start of the backdrop path`() = runBlockingTest {
    val input = MovieModel(backdropPath = "backdrop_path")
    val result = movieMapper.map(configuration, input)
    assertThat(result.backdropPath!!.baseUrl).startsWith(secureBaseUrl)
  }

  @Test
  fun `it should append url to start of the poster path`() = runBlockingTest {
    val input = MovieModel(posterPath = "poster_path")
    val result = movieMapper.map(configuration, input)
    assertThat(result.posterPath!!.baseUrl).startsWith(secureBaseUrl)
  }

  @Test
  fun `should add secure base url to image object`() = runBlockingTest {
    val input = MovieModel(images = BackdropListModel(createBackdropList()))
    val images = movieMapper.map(configuration, input).images.map { it.baseUrl }
    val elementsStartsWith = Correspondence.from<String, String>(
      { e, a -> e?.startsWith(a ?: "") ?: false },
      "starts with"
    )
    assertThat(images).comparingElementsUsing(elementsStartsWith).contains(secureBaseUrl)
  }

  private fun createBackdropList(): List<BackdropModel> {
    return mutableListOf(BackdropModel(filePath = "file_path"))
  }
}