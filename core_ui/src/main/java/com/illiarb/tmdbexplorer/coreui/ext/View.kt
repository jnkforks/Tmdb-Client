package com.illiarb.tmdbexplorer.coreui.ext

import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

/**
 * @author ilya-rb on 18.10.18.
 */
private const val VIEW_DEBOUNCED_CLICKS_DELAY = 400L

fun View.setSize(widthSpec: SizeSpec? = null, heightSpec: SizeSpec? = null) {
    val params = layoutParams as ViewGroup.LayoutParams

    widthSpec?.let {
        params.width = when (it) {
            is SizeSpec.MatchParent -> ViewGroup.LayoutParams.MATCH_PARENT
            is SizeSpec.WrapContent -> ViewGroup.LayoutParams.WRAP_CONTENT
            is SizeSpec.Fixed -> resources.getDimensionPixelSize(it.sizeRes)
        }
    }

    heightSpec?.let {
        params.height = when (it) {
            is SizeSpec.MatchParent -> ViewGroup.LayoutParams.MATCH_PARENT
            is SizeSpec.WrapContent -> ViewGroup.LayoutParams.WRAP_CONTENT
            is SizeSpec.Fixed -> resources.getDimensionPixelSize(it.sizeRes)
        }
    }
}

fun View.updatePadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) = setPadding(left, top, right, bottom)

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.debouncedClicks(): Flow<Unit> = callbackFlow {
    val clickListener = View.OnClickListener {
        offer(Unit)
    }

    setOnClickListener(clickListener)
    awaitClose { setOnClickListener(null) }

}.debounce(VIEW_DEBOUNCED_CLICKS_DELAY)

fun View.focusChanges(): Flow<Boolean> = callbackFlow {
    val focusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        offer(hasFocus)
    }

    onFocusChangeListener = focusChangeListener
    awaitClose { onFocusChangeListener = null }
}