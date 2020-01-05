package com.illiarb.tmdbclient.home.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import com.illiarb.core_ui_image.CropOptions
import com.illiarb.core_ui_image.RequestOptions
import com.illiarb.core_ui_image.loadImage
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbexplorer.coreui.common.OnClickListener
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.domain.MovieSection
import com.illiarb.tmdblcient.core.domain.TrendingSection
import com.illiarb.tmdblcient.core.domain.TrendingSection.TrendingItem

fun trendingSectionDelegate(clickListener: OnClickListener) =
    adapterDelegate<TrendingSection, MovieSection>(R.layout.item_trending_section) {

        val adapter = TrendingSectionAdapter(clickListener)
        val trendingList = itemView.findViewById<RecyclerView>(R.id.itemTrendingSectionList)

        trendingList.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            it.addItemDecoration(
                SpaceDecoration(
                    orientation = LinearLayoutManager.HORIZONTAL,
                    spacingLeft = itemView.dimen(R.dimen.spacing_small),
                    spacingLeftFirst = itemView.dimen(R.dimen.spacing_normal),
                    spacingRight = itemView.dimen(R.dimen.spacing_small),
                    spacingRightLast = itemView.dimen(R.dimen.spacing_normal)
                )
            )
        }

        bind {
            adapter.items = item.items
            adapter.notifyDataSetChanged()
        }
    }

private class TrendingSectionAdapter(clickListener: OnClickListener) :
    ListDelegationAdapter<List<TrendingItem>>() {

    init {
        delegatesManager.addDelegate(trendingDelegate(clickListener))
    }

    private fun trendingDelegate(clickListener: OnClickListener) =
        adapterDelegate<TrendingItem, TrendingItem>(R.layout.item_trending) {

            val image = itemView.findViewById<ImageView>(R.id.itemTrendingImage)
            val name = itemView.findViewById<TextView>(R.id.itemTrendingName)

            bind {
                name.text = item.name
                image.loadImage(item.image, RequestOptions.requestOptions {
                    cropOptions(CropOptions.CIRCLE)
                })

                itemView.setOnClickListener {
                    clickListener.onClick(item)
                }
            }
        }
}