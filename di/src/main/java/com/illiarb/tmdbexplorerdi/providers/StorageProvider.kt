package com.illiarb.tmdbexplorerdi.providers

import com.illiarb.tmdblcient.core.modules.account.AccountRepository
import com.illiarb.tmdblcient.core.modules.auth.Authenticator
import com.illiarb.tmdblcient.core.modules.movie.MoviesRepository
import com.illiarb.tmdblcient.core.system.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.ResourceResolver
import com.illiarb.tmdblcient.core.system.WorkManager

/**
 * Interface for providing repositories
 * And all storage-module related dependencies
 */
interface StorageProvider {

    fun provideMoviesRepository(): MoviesRepository

    fun provideAccountRepository(): AccountRepository

    fun provideResourceResolver(): ResourceResolver

    fun provideAuthenticator(): Authenticator

    fun provideErrorMessageBar(): ErrorMessageBag

    fun provideWorkManager(): WorkManager
}