package com.illiarb.tmdbexplorer.coreui.base.recyclerview.adapter

import android.view.ViewGroup
import com.illiarb.tmdbexplorer.coreui.base.recyclerview.viewholder.BaseDelegateViewHolder

/**
 * @author ilya-rb on 04.11.18.
 */
interface AdapterDelegate {

    fun isForViewType(item: Any): Boolean

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDelegateViewHolder
}