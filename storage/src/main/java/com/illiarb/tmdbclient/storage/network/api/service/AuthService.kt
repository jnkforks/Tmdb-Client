package com.illiarb.tmdbclient.storage.network.api.service

import com.illiarb.tmdbclient.storage.network.request.CreateSessionRequest
import com.illiarb.tmdbclient.storage.network.request.ValidateTokenRequest
import com.illiarb.tmdbclient.storage.network.response.AuthTokenResponse
import com.illiarb.tmdbclient.storage.network.response.CreateSessionResponse
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author ilya-rb on 21.11.18.
 */
interface AuthService {

    @GET("authentication/token/new")
    fun requestAuthToken(): Deferred<AuthTokenResponse>

    @POST("authentication/token/validate_with_login")
    fun validateTokenWithCredentials(@Body request: ValidateTokenRequest): Deferred<AuthTokenResponse>

    @POST("authentication/session/new")
    fun createNewSession(@Body request: CreateSessionRequest): Deferred<CreateSessionResponse>

}