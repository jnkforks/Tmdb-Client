package com.illiarb.tmdbclient.services.tmdb.internal.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

internal class TmdbDateFormatter @Inject constructor() {

  companion object {
    const val DISPLAY_DATE_FORMAT_PATTERN = "dd MMM yyyy"
    const val PARSE_DATE_FORMAT_PATTERN = "yyyy-mm-dd"
  }

  private val dateParser = SimpleDateFormat(PARSE_DATE_FORMAT_PATTERN, Locale.getDefault())
  private val dateFormatter = SimpleDateFormat(DISPLAY_DATE_FORMAT_PATTERN, Locale.getDefault())

  fun formatDate(date: String): String {
    return try {
      val parsed = dateParser.parse(date)
      if (parsed == null) {
        date
      } else {
        dateFormatter.format(parsed)
      }
    } catch (e: ParseException) {
      date
    }
  }
}