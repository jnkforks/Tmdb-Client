package com.illiarb.tmdbexplorer.coreui.observable

/**
 * @author ilya-rb on 17.01.19.
 */
interface Observer<T> {

    fun onNewValue(state: T)

}