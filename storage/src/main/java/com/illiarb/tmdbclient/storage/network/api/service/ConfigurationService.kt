package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.network.api.config.Configuration
import io.reactivex.Single
import retrofit2.http.GET

/**
 * @author ilya-rb on 03.12.18.
 */
interface ConfigurationService {

    @GET("configuration")
    fun getConfiguration() : Single<Configuration>

}