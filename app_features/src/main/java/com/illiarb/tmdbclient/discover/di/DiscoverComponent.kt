package com.illiarb.tmdbclient.discover.di

import com.illiarb.tmdbclient.discover.DiscoverFragment
import com.illiarb.tmdbexplorer.coreui.di.ViewModelModule
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import dagger.Component

@Component(
    dependencies = [AppProvider::class],
    modules = [
        DiscoverModule::class,
        ViewModelModule::class
    ]
)
interface DiscoverComponent {

    companion object {
        fun get(appProvider: AppProvider): DiscoverComponent =
            DaggerDiscoverComponent.builder()
                .appProvider(appProvider)
                .build()
    }

    fun inject(fragment: DiscoverFragment)
}