package com.illiarb.tmdbclient.main.di

import androidx.fragment.app.FragmentActivity
import com.illiarb.tmdbclient.main.MainActivity
import com.illiarb.tmdbexplorer.coreui.di.ActivityModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

/**
 * @author ilya-rb on 28.12.18.
 */
@Component(
    dependencies = [AppProvider::class],
    modules = [
        MainModule::class,
        ActivityModule::class
    ]
)
interface MainComponent {

    companion object {
        fun get(appProvider: AppProvider, activity: FragmentActivity): MainComponent =
            DaggerMainComponent.builder()
                .appProvider(appProvider)
                .activityModule(ActivityModule(activity))
                .build()
    }

    fun inject(activity: MainActivity)
}