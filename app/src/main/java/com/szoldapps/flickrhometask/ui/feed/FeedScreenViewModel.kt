package com.szoldapps.flickrhometask.ui.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szoldapps.flickrhometask.data.FlickrRepository
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okio.IOException
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class FeedScreenViewModel @Inject constructor(
    private val flickrRepository: FlickrRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val tags: StateFlow<List<String>> = savedStateHandle.getStateFlow("tags", emptyList())

    private fun saveTags(tags: List<String>) {
        savedStateHandle["tags"] = tags
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState.Content(tags = emptyList(), feedScreenData = FeedScreenData())
    )
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadData(tags.value)
    }

    private fun loadData(tags: List<String>) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading(tags))
            flickrRepository.getPhotosPublicFeed(tags)
                .map<FeedScreenData, UiState> { feedScreenData ->
                    UiState.Content(tags, feedScreenData)
                }
                .catch { throwable ->
                    Timber.e(throwable)
                    emit(UiState.Error(tags = tags, message = throwable.toErrorMessage()))
                }
                .collect { contentUiState ->
                    saveTags(tags)
                    _uiState.emit(contentUiState)
                }
        }
    }

    fun addTag(tag: String) {
        if (tag.isNotBlank()) {
            val tags = _uiState.value.tags.toMutableList().apply {
                this.add(tag)
            }
            loadData(tags)
        }
    }

    fun clearTags() {
        loadData(emptyList())
    }

    private fun Throwable.toErrorMessage() = when (this) {
        is IOException -> "IOException"
        else -> "Unknown Error"
    }

    fun refresh() {
        loadData(_uiState.value.tags)
    }

    sealed class UiState {
        open val tags: List<String> = emptyList()

        data class Loading(
            override val tags: List<String>
        ) : UiState()

        data class Error(
            override val tags: List<String>,
            val message: String
        ) : UiState()

        data class Content(
            override val tags: List<String>,
            val feedScreenData: FeedScreenData
        ) : UiState()
    }
}
