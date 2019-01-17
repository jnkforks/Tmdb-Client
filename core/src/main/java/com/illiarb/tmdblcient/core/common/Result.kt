package com.illiarb.tmdblcient.core.common

import com.illiarb.tmdblcient.core.exception.ErrorHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * @author ilya-rb on 09.01.19.
 */
sealed class Result<out T> {

    companion object {

        suspend fun <T : Any> create(
            errorHandler: ErrorHandler,
            block: suspend CoroutineScope.() -> T
        ): Result<T> = coroutineScope {
            try {
                Result.Success(block(this))
            } catch (e: Exception) {
                Result.Error<T>(errorHandler.createExceptionFromThrowable(e))
            }
        }
    }

    data class Success<out T : Any>(val result: T) : Result<T>()

    data class Error<out T : Any>(val error: Throwable) : Result<T>()
}