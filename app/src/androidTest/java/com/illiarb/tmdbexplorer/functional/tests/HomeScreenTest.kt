package com.illiarb.tmdbexplorer.functional.tests

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.illiarb.tmdbclient.ui.main.MainActivity
import com.illiarb.tmdbexplorer.functional.screens.DiscoverScreen
import com.illiarb.tmdbexplorer.functional.screens.HomeScreen
import com.illiarb.tmdbexplorer.functional.screens.HomeScreen.GenreSectionItem
import com.illiarb.tmdbexplorer.functional.screens.HomeScreen.NowPlayingSectionItem
import com.illiarb.tmdbexplorer.functional.screens.HomeScreen.NowPlayingSectionItem.NowPlayingItem
import com.illiarb.tmdbexplorer.functional.screens.HomeScreen.TrendingSectionItem
import com.illiarb.tmdbexplorer.functional.screens.MovieDetailsScreen
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class HomeScreenTest : TestCase(Kaspresso.Builder.simple()) {

  @get:Rule
  val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
  )

  @get:Rule
  val activityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

  @Test
  fun testHomeSectionsAreDisplayed() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {
      step("Check home screen is visible") {
        HomeScreen {
          checkIsVisible()
        }
      }

      step("Check home sections is displayed") {
        HomeScreen {
          moviesList.firstChild<NowPlayingSectionItem> { isVisible() }
          moviesList.firstChild<GenreSectionItem> { isVisible() }
          moviesList.firstChild<TrendingSectionItem> { isVisible() }
        }
      }
    }
  }

  @Test
  fun testNowPlayingSectionMovieClickOpensMovieDetailsScreen() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {
      step("click now playing section first item and check movie details screen is visible") {
        HomeScreen {
          moviesList.firstChild<NowPlayingSectionItem> {
            items.firstChild<NowPlayingItem> {
              isCompletelyDisplayed()
              click()

              MovieDetailsScreen {
                poster.isVisible()
              }
            }
          }
        }
      }
    }
  }

  @Test
  fun testGenreClickOpensDiscoverScreen() = run {
    before {
      activityTestRule.launchActivity(null)
    }.after {
    }.run {
      step("Open home screen click first genre chip and check discover screen is displayed") {
        HomeScreen {
          moviesList.firstChild<GenreSectionItem> {
            genres.selectChipAt(0)

            DiscoverScreen {
              root.isVisible()
            }
          }
        }
      }
    }
  }
}