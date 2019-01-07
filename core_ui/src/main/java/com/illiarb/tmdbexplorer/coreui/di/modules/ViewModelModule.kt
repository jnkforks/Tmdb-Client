package com.illiarb.tmdbexplorer.coreui.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illiarb.tmdbexplorer.coreui.di.DaggerViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import kotlin.reflect.KClass

/**
 * @author ilya-rb on 06.01.19.
 */
@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}

@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)