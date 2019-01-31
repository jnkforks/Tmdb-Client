package com.illiarb.tmdbclient.storage.system

import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkerFactory
import com.illiarb.tmdbclient.storage.network.api.config.ConfigurationFetchWork
import com.illiarb.tmdblcient.core.di.App
import com.illiarb.tmdblcient.core.storage.WorkManager
import javax.inject.Inject
import javax.inject.Singleton
import androidx.work.WorkManager as AndroidWorkManager

/**
 * @author ilya-rb on 03.12.18.
 */
@Singleton
class AppWorkManager @Inject constructor(app: App, workerFactory: WorkerFactory) :
    WorkManager {

    init {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

        AndroidWorkManager.initialize(app.getApplication(), config)
    }

    override fun schedulerPeriodicConfigurationFetch() {
        AndroidWorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                ConfigurationFetchWork::class.java.name,
                ExistingPeriodicWorkPolicy.KEEP,
                ConfigurationFetchWork.createWorkRequest()
            )
    }
}