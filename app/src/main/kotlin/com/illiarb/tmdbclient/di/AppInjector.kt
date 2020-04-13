package com.illiarb.tmdbclient.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.illiarb.tmdbclient.App

/**
 * @author ilya-rb on 24.12.18.
 */
class AppInjector(private val app: App) : Application.ActivityLifecycleCallbacks {

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    if (activity is Injectable) {
      activity.inject(app.appComponent)
    }

    if (activity is FragmentActivity) {
      activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
        fragmentLifecycleCallbacks(),
        true
      )
    }
  }

  override fun onActivityPaused(activity: Activity?) = Unit
  override fun onActivityResumed(activity: Activity?) = Unit
  override fun onActivityStarted(activity: Activity?) = Unit
  override fun onActivityDestroyed(activity: Activity?) = Unit
  override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
  override fun onActivityStopped(activity: Activity?) = Unit

  fun registerLifecycleCallbacks() {
    app.registerActivityLifecycleCallbacks(this)
  }

  private fun fragmentLifecycleCallbacks(): FragmentManager.FragmentLifecycleCallbacks {
    return object : FragmentManager.FragmentLifecycleCallbacks() {
      override fun onFragmentPreAttached(
        fm: FragmentManager,
        fragment: Fragment,
        context: Context
      ) {
        if (fragment is Injectable) {
          fragment.inject(app.appComponent)
        }
      }
    }
  }
}