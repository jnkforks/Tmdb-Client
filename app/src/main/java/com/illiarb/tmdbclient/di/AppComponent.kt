package com.illiarb.tmdbclient.di

import android.app.Application
import com.illiarb.tmdbclient.MobileApplication
import com.illiarb.tmdbclient.di.modules.AppModule
import com.illiarb.tmdbclient.di.modules.NavigationModule
import com.illiarb.tmdbclient.di.modules.WorkModule
import com.illiarb.tmdbclient.tools.di.ToolsProvider
import com.illiarb.tmdbclient.ui.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.ui.discover.di.DiscoverComponent
import com.illiarb.tmdbclient.ui.home.di.HomeComponent
import com.illiarb.tmdbclient.ui.main.MainComponent
import com.illiarb.tmdbclient.ui.video.di.VideoListComponent
import com.illiarb.tmdclient.analytics.di.AnalyticsProvider
import com.tmdbclient.servicetmdb.di.TmdbProvider
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
  dependencies = [
    ToolsProvider::class,
    TmdbProvider::class,
    AnalyticsProvider::class
  ],
  modules = [
    AppModule::class,
    NavigationModule::class,
    WorkModule::class
  ]
)
@Singleton
interface AppComponent :
  MainComponent.Dependencies,
  HomeComponent.Dependencies,
  VideoListComponent.Dependencies,
  MovieDetailsComponent.Dependencies,
  DiscoverComponent.Dependencies {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(app: Application): Builder
    fun toolsProvider(provider: ToolsProvider): Builder
    fun tmdbProvider(provider: TmdbProvider): Builder
    fun analyticsProvider(provider: AnalyticsProvider): Builder
    fun appModule(module: AppModule): Builder
    fun build(): AppComponent
  }

  fun inject(app: MobileApplication)
}