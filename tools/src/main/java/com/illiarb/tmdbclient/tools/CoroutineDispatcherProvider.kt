package com.illiarb.tmdbclient.tools

import com.illiarb.tmdblcient.core.system.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * @author ilya-rb on 07.01.19.
 */
class CoroutineDispatcherProvider @Inject constructor() : DispatcherProvider {

    override val io: CoroutineDispatcher = Dispatchers.IO

    override val main: CoroutineDispatcher = Dispatchers.Main

    override val default: CoroutineDispatcher = Dispatchers.Default
}