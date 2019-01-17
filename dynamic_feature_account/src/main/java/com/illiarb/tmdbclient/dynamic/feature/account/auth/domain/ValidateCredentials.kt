package com.illiarb.tmdbclient.dynamic.feature.account.auth.domain

import com.illiarb.tmdblcient.core.domain.BlockingUseCase
import com.illiarb.tmdblcient.core.entity.UserCredentials
import com.illiarb.tmdblcient.core.exception.ErrorCodes
import com.illiarb.tmdblcient.core.exception.ErrorMessageBag
import com.illiarb.tmdblcient.core.system.coroutine.Blocking
import javax.inject.Inject

/**
 * @author ilya-rb on 10.01.19.
 */
class ValidateCredentials @Inject constructor(
    private val validator: Validator,
    private val errorMessageBag: ErrorMessageBag
) : BlockingUseCase<Boolean, UserCredentials> {

    @Blocking
    override fun executeBlocking(payload: UserCredentials): Boolean {
        val errors = mutableListOf<Pair<Int, String>>()

        if (validator.isUsernameEmpty(payload.username)) {
            errors.add(Pair(ErrorCodes.ERROR_USERNAME_EMPTY, errorMessageBag.getUsernameEmptyMessage()))
        }

        if (validator.isPasswordEmpty(payload.password)) {
            errors.add(Pair(ErrorCodes.ERROR_PASSWORD_EMPTY, errorMessageBag.getPasswordEmptyMessage()))
        } else if (!validator.isPasswordLengthCorrect(payload.password)) {
            errors.add(Pair(ErrorCodes.ERROR_PASSWORD_LENGTH, errorMessageBag.getIncorrectPasswordLengthMessage()))
        }

        // If errors is empty - credentials are valid
        return errors.isEmpty()
    }
}