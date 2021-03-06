package com.illiarb.tmdbclient.modules.home.di

import androidx.lifecycle.ViewModel
import com.illiarb.tmdbclient.libs.ui.di.ViewModelKey
import com.illiarb.tmdbclient.modules.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HomeModule {

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  fun bindHomeModel(impl: HomeViewModel): ViewModel
}