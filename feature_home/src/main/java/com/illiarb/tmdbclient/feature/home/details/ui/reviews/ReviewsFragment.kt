package com.illiarb.tmdbclient.feature.home.details.ui.reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illiarb.tmdbclient.feature.home.R
import com.illiarb.tmdbclient.feature.home.di.MoviesComponent
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutOrientation
import com.illiarb.tmdbexplorer.coreui.recyclerview.LayoutType
import com.illiarb.tmdbexplorer.coreui.recyclerview.RecyclerViewBuilder
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.entity.Review
import kotlinx.android.synthetic.main.fragment_movie_reviews.*
import java.io.Serializable
import javax.inject.Inject

/**
 * @author ilya-rb on 03.02.19.
 */
class ReviewsFragment : BottomSheetDialogFragment(), Injectable {

    companion object {

        private const val ARG_REVIEWS = "reviews"

        fun newInstance(reviews: List<Review>): ReviewsFragment =
            ReviewsFragment().also {
                it.arguments = Bundle().apply {
                    putSerializable(ARG_REVIEWS, reviews as Serializable)
                }
            }
    }

    @Inject
    lateinit var reviewsAdapter: ReviewsAdapter

    override fun inject(appProvider: AppProvider) = MoviesComponent.get(appProvider).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RecyclerViewBuilder
            .create {
                type(LayoutType.Linear())
                orientation(LayoutOrientation.VERTICAL)
                adapter(reviewsAdapter)
            }
            .setupWith(movieReviews)

        arguments?.let {
            @Suppress("UNCHECKED_CAST")
            val reviews = it.getSerializable(ARG_REVIEWS) as List<Review>
            reviewsAdapter.submitList(reviews)
        }
    }
}