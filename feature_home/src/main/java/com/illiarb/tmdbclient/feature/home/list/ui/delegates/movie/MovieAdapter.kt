package com.illiarb.tmdbclient.feature.home.list.ui.delegates.movie

import android.view.ViewGroup
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbexplorer.coreui.common.ViewClickListener
import com.illiarb.tmdbexplorer.coreui.ext.inflate
import com.illiarb.tmdbexplorer.coreui.image.ImageLoader
import com.illiarb.tmdbexplorer.coreui.recyclerview.adapter.BaseAdapter
import com.illiarb.tmdbexplorer.coreui.recyclerview.viewholder.MovieViewHolder
import com.illiarb.tmdblcient.core.entity.Movie

class MovieAdapter(
    private val imageLoader: ImageLoader,
    private val viewClickListener: ViewClickListener
) : BaseAdapter<Movie, MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(parent.inflate(R.layout.item_movie), imageLoader)
            .apply {
                itemView.setOnClickListener {
                    viewClickListener.onClicked(getItemAt(adapterPosition))
                }
            }
}