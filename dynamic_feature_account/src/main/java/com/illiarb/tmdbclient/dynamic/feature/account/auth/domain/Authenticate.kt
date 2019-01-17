package com.illiarb.tmdbclient.dynamic.feature.account.auth.domain

import com.illiarb.tmdblcient.core.auth.Authenticator
import com.illiarb.tmdblcient.core.common.Result
import com.illiarb.tmdblcient.core.domain.NonBlockingUseCase
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.exception.ErrorHandler
import com.illiarb.tmdblcient.core.system.coroutine.NonBlocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class Authenticate @Inject constructor(
    private val validateCredentials: ValidateCredentials,
    private val authenticator: Authenticator,
    private val errorHandler: ErrorHandler
) : NonBlockingUseCase<Unit, UserCredentials> {

    @NonBlocking
    override suspend fun executeAsync(payload: UserCredentials): Result<Unit> {
        return Result.create(errorHandler) {
            val isCredentialsValid = validateCredentials.executeBlocking(payload)
            if (isCredentialsValid) {
                authenticator.authorize(payload)
            }
        }
    }
}