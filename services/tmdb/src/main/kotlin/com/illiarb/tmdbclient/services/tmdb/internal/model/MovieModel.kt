package com.illiarb.tmdbclient.services.tmdb.internal.model

import com.illiarb.tmdbclient.services.tmdb.domain.Video
import com.ironz.binaryprefs.serialization.serializer.persistable.Persistable
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataInput
import com.ironz.binaryprefs.serialization.serializer.persistable.io.DataOutput
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MovieModel(
  @Json(name = "id") var id: Int = 0,
  @Json(name = "poster_path") var posterPath: String? = null,
  @Json(name = "backdrop_path") var backdropPath: String? = null,
  @Json(name = "release_date") var releaseDate: String = "",
  @Json(name = "overview") var overview: String? = null,
  @Json(name = "title") var title: String = "",
  @Json(name = "vote_average") var voteAverage: Float = 0f,
  @Json(name = "budget") val budget: Int = 0,
  @Json(name = "genres") val genres: List<GenreModel>? = emptyList(),
  @Json(name = "homepage") val homepage: String? = null,
  @Json(name = "runtime") val runtime: Int = 0,
  @Json(name = "status") val status: String? = null,
  @Json(name = "reviews") val reviews: ResultsModel<ReviewModel>? = null,
  @Json(name = "images") val images: BackdropListModel? = null,
  @Json(name = "credits") val credits: CreditsModel? = null,
  @Json(name = "production_countries") val productionCountries: List<ProductionCountryModel> = emptyList(),
  @Json(name = "videos") val videos: ResultsModel<Video>? = null
) : Persistable, TrendingModel {

  override fun readExternal(input: DataInput) =
    with(input) {
      id = readInt()
      posterPath = readString()
      backdropPath = readString()
      releaseDate = readString()
      overview = readString()
      title = readString()
      voteAverage = input.readFloat()
    }

  override fun deepClone(): Persistable =
    MovieModel(
      id,
      posterPath,
      backdropPath,
      releaseDate,
      overview,
      title,
      voteAverage
    )

  override fun writeExternal(output: DataOutput) =
    output.run {
      writeInt(id)
      writeString(posterPath)
      writeString(backdropPath)
      writeString(releaseDate)
      writeString(overview)
      writeString(title)
      writeFloat(voteAverage)
    }
}