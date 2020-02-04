package com.illiarb.tmdbclient.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.illiarb.tmdbclient.video.VideoListModel.UiEvent
import com.illiarb.tmdbclient.video.VideoListModel.UiVideo
import com.illiarb.tmdbexplorer.coreui.base.BasePresentationModel
import com.illiarb.tmdblcient.core.domain.Video
import com.illiarb.tmdblcient.core.interactor.MoviesInteractor
import com.illiarb.tmdblcient.core.util.Result
import kotlinx.coroutines.launch

interface VideoListModel {

    val videos: LiveData<List<Any>>

    val selectedVideo: LiveData<UiVideo>

    fun onUiEvent(event: UiEvent)

    data class UiVideo(val video: Video, val isSelected: Boolean)
    data class UiVideoSection(val title: String, val count: Int)

    sealed class UiEvent {
        object VideoEnded : UiEvent()
        class VideoClick(val video: UiVideo) : UiEvent()
    }
}

class DefaultVideoListModel(
    private val movieId: Int,
    private val moviesInteractor: MoviesInteractor
) : BasePresentationModel(), VideoListModel {

    private val _videos = MutableLiveData<List<Any>>()
    private val _selectedVideo = _videos.map { videos ->
        videos.first { it is UiVideo && it.isSelected } as UiVideo
    }

    init {
        viewModelScope.launch {
            _videos.value = when (val result = moviesInteractor.getMovieVideos(movieId)) {
                is Result.Success -> {
                    val groupedVideos = result.data
                        .mapIndexed { index, video ->
                            if (index == 0) {
                                UiVideo(video, isSelected = true)
                            } else {
                                UiVideo(video, isSelected = false)
                            }
                        }
                        .groupBy { it.video.type }

                    mutableListOf<Any>().apply {
                        groupedVideos.forEach { videoGroup ->
                            add(VideoListModel.UiVideoSection(videoGroup.key, videoGroup.value.size))
                            addAll(videoGroup.value)
                        }
                    }
                }
                is Result.Error -> emptyList()
            }
        }
    }

    override val videos: LiveData<List<Any>>
        get() = _videos

    override val selectedVideo: LiveData<UiVideo>
        get() = _selectedVideo

    override fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.VideoClick -> selectVideo(event.video)
            is UiEvent.VideoEnded -> onVideoEnded()
        }
    }

    private fun onVideoEnded() {
        // Play next video in the list or if end is reached the first one
        _videos.value?.let { videos ->
            videos.indexOfFirst { it is UiVideo && it.isSelected }
                .let { if (it < videos.size - 1) it + 1 else 0 }
                .let { selectVideo(videos[it] as UiVideo) }
        }
    }

    private fun selectVideo(video: UiVideo) {
        _videos.value?.let {
            val position = it.indexOf(video).takeIf { pos -> pos != -1 } ?: return

            _videos.value = it.mapIndexed { index, item ->
                if (item is UiVideo) {
                    if (index == position) {
                        item.copy(isSelected = true)
                    } else {
                        item.copy(isSelected = false)
                    }
                } else {
                    item
                }
            }
        }
    }
}