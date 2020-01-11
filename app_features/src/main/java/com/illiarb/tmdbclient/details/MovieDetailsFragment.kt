package com.illiarb.tmdbclient.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.illiarb.coreuiimage.CropOptions
import com.illiarb.coreuiimage.loadImage
import com.illiarb.tmdbclient.common.delegates.movieDelegate
import com.illiarb.tmdbclient.details.MovieDetailsModel.UiEvent
import com.illiarb.tmdbclient.details.delegates.photoDelegate
import com.illiarb.tmdbclient.details.di.MovieDetailsComponent
import com.illiarb.tmdbclient.movies.home.R
import com.illiarb.tmdbclient.movies.home.databinding.FragmentMovieDetailsBinding
import com.illiarb.tmdbexplorer.coreui.base.BaseViewBindingFragment
import com.illiarb.tmdbexplorer.coreui.common.SizeSpec
import com.illiarb.tmdbexplorer.coreui.ext.dimen
import com.illiarb.tmdbexplorer.coreui.ext.doOnApplyWindowInsets
import com.illiarb.tmdbexplorer.coreui.ext.removeAdapterOnDetach
import com.illiarb.tmdbexplorer.coreui.ext.setVisible
import com.illiarb.tmdbexplorer.coreui.ext.updatePadding
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.DelegatesAdapter
import com.illiarb.tmdbexplorer.coreui.widget.recyclerview.SpaceDecoration
import com.illiarb.tmdblcient.core.di.Injectable
import com.illiarb.tmdblcient.core.di.providers.AppProvider
import com.illiarb.tmdblcient.core.domain.Movie
import com.illiarb.tmdblcient.core.navigation.Router.Action.ShowMovieDetails
import com.illiarb.tmdblcient.core.util.Async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class MovieDetailsFragment : BaseViewBindingFragment<FragmentMovieDetailsBinding>(), Injectable {

    companion object {
        const val DISPLAY_DATE_FORMAT_PATTERN = "dd MMM yyyy"
        const val PARSE_DATE_FORMAT_PATTERN = "yyyy-mm-dd"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val photosAdapter = DelegatesAdapter({ listOf(photoDelegate(it)) })
    private val moviesAdapter = DelegatesAdapter({
        listOf(
            movieDelegate(
                it,
                SizeSpec.Fixed(R.dimen.item_movie_width),
                SizeSpec.Fixed(R.dimen.item_movie_height)
            )
        )
    })

    private val viewModel: MovieDetailsModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory.create(DefaultDetailsViewModel::class.java)
    }

    override fun inject(appProvider: AppProvider) {
        val id = requireArguments().getInt(ShowMovieDetails.EXTRA_MOVIE_DETAILS)
        MovieDetailsComponent.get(appProvider, id).inject(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentMovieDetailsBinding =
        FragmentMovieDetailsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        binding.swipeRefresh.isEnabled = false

        lifecycleScope.launch {
            moviesAdapter.clicks().collect {
                viewModel.onUiEvent(UiEvent.ItemClick(it))
            }
        }

        setupMoviesList()
        setupPhotosList()

        ViewCompat.requestApplyInsets(view)

        bind(viewModel)
    }

    private fun setupToolbar() {
        binding.movieDetailsToolbar.apply {
            navigationIcon?.mutate()?.setTint(Color.WHITE)

            doOnApplyWindowInsets { v, windowInsets, initialPadding ->
                v.updatePadding(top = initialPadding.top + windowInsets.systemWindowInsetTop)
            }

            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupMoviesList() {
        binding.movieDetailsSimilar.apply {
            adapter = moviesAdapter
            layoutManager = createHorizontalLayoutManager()
            removeAdapterOnDetach()
            isNestedScrollingEnabled = false
            addItemDecoration(createHorizontalListDecoration())
            doOnApplyWindowInsets { v, windowInsets, initialPadding ->
                v.updatePadding(bottom = initialPadding.bottom + windowInsets.systemWindowInsetBottom)
            }
        }
    }

    private fun setupPhotosList() {
        binding.movieDetailsPhotos.apply {
            adapter = photosAdapter
            layoutManager = createHorizontalLayoutManager()
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            removeAdapterOnDetach()
            addItemDecoration(createHorizontalListDecoration())
        }
    }

    private fun createHorizontalLayoutManager(): RecyclerView.LayoutManager =
        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    private fun createHorizontalListDecoration(): RecyclerView.ItemDecoration {
        return SpaceDecoration(
            orientation = LinearLayoutManager.HORIZONTAL,
            spacingLeftFirst = dimen(R.dimen.spacing_normal),
            spacingLeft = dimen(R.dimen.spacing_small),
            spacingRight = dimen(R.dimen.spacing_small),
            spacingRightLast = dimen(R.dimen.spacing_normal)
        )
    }

    private fun bind(viewModel: MovieDetailsModel) {
        viewModel.movie.observe(
            viewLifecycleOwner,
            Observer {
                binding.swipeRefresh.isRefreshing = it is Async.Loading

                if (it is Async.Success) {
                    showMovieDetails(it())
                }
            }
        )
        viewModel.similarMovies.observe(viewLifecycleOwner, moviesAdapter)
    }

    private fun String.asFormattedDate(parseFormat: String, displayFormat: String): String {
        val dateParser = SimpleDateFormat(parseFormat, Locale.getDefault())
        val dateFormatter = SimpleDateFormat(displayFormat, Locale.getDefault())

        return try {
            val date = dateParser.parse(this)
            if (date == null) {
                this
            } else {
                dateFormatter.format(date)
            }
        } catch (e: ParseException) {
            this
        }
    }

    private fun showMovieDetails(movie: Movie) {
        with(binding) {
            movieDetailsTitle.text = movie.title
            movieDetailsOverview.text = movie.overview
            movieDetailsLength.text = getString(R.string.movie_details_duration, movie.runtime)
            movieDetailsCountry.text = movie.country
            movieDetailsTags.text = movie.getGenresString()

            movieDetailsDate.text = movie.releaseDate.asFormattedDate(
                PARSE_DATE_FORMAT_PATTERN,
                DISPLAY_DATE_FORMAT_PATTERN
            )

            movieDetailsPoster.loadImage(movie.posterPath) {
                crop(CropOptions.CENTER_CROP)
            }

//            movieDetailsPlay.setVisible(movie.video)
            movieDetailsPlay.setOnClickListener {
                viewModel.onUiEvent(UiEvent.PlayClicked)
            }
        }

        photosAdapter.submitList(movie.images)
    }
}