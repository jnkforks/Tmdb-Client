package com.illiarb.tmdclient.analytics

/**
 * @author ilya-rb on 19.02.19.
 */
sealed class AnalyticEvent(val eventName: String) {

  class RouterAction(val action: String) : AnalyticEvent("screen_opened")
}