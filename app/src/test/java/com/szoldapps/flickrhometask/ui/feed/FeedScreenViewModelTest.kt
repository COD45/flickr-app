package com.szoldapps.flickrhometask.ui.feed

import com.szoldapps.flickrhometask.common.CoroutineTestExtension
import com.szoldapps.flickrhometask.data.FakeFlickrRepository
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.ui.feed.FeedScreenViewModel.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
internal class FeedScreenViewModelTest {

    @Test
    fun `initial uiState is Loading`() = runTest {
        // given
        val fakeMatchRepository = FakeFlickrRepository()

        // when
        val viewModel = FeedScreenViewModel(fakeMatchRepository)

        // then
        assertEquals(UiState.Loading(emptyList()), viewModel.uiState.value)
    }

    @Test
    fun `correct uiState when repository returns valid FeedScreenData`() = runTest {
        // given
        val feedScreenData = FeedScreenData(title = "Feed Title")
        val tags = emptyList<String>()
        val fakeMatchRepository = FakeFlickrRepository()

        // when
        val viewModel =
            FeedScreenViewModel(fakeMatchRepository)

        // then
        assertEquals(UiState.Loading(tags), viewModel.uiState.value)
        fakeMatchRepository.emit(feedScreenData)
        assertEquals(
            UiState.Content(feedScreenData = feedScreenData, tags = tags),
            viewModel.uiState.value
        )
    }

    @Test
    fun `tag is added to uiState after ViewModel-addTag is called`() = runTest {
        // given
        val feedScreenData = FeedScreenData(title = "Feed Title")
        val fakeMatchRepository = FakeFlickrRepository()
        val viewModel = FeedScreenViewModel(fakeMatchRepository)
        val addedTag = "added Tag"
        val tags = listOf(addedTag)

        // when
        viewModel.addTag(addedTag)

        // then
        fakeMatchRepository.emit(feedScreenData)
        assertEquals(
            UiState.Content(feedScreenData = feedScreenData, tags = tags),
            viewModel.uiState.value
        )
    }

    @Test
    fun `tags are removed from uiState after ViewModel-clearTags is called`() = runTest {
        // given
        val feedScreenData = FeedScreenData(title = "Feed Title")
        val fakeMatchRepository = FakeFlickrRepository()
        val viewModel = FeedScreenViewModel(fakeMatchRepository)
        viewModel.addTag("added Tag 1")
        viewModel.addTag("added Tag 2")

        // when
        viewModel.clearTags()

        // then
        fakeMatchRepository.emit(feedScreenData)
        assertEquals(
            UiState.Content(feedScreenData = feedScreenData, tags = emptyList()),
            viewModel.uiState.value
        )
    }

    @Test
    fun `refresh collects a fresh flow`() = runTest {
        // given
        val feedScreenData1 = FeedScreenData(title = "Feed Title 1")
        val feedScreenData2 = FeedScreenData(title = "Feed Title 2")
        val fakeMatchRepository = FakeFlickrRepository()
        val viewModel = FeedScreenViewModel(fakeMatchRepository)
        fakeMatchRepository.emit(feedScreenData1)

        // when
        viewModel.refresh()

        // then
        fakeMatchRepository.emit(feedScreenData2)
        assertEquals(
            UiState.Content(feedScreenData = feedScreenData2, tags = emptyList()),
            viewModel.uiState.value
        )
    }

    @Test
    fun `when IOException thrown, IOException error is emitted`() = runTest {
        // given
        val fakeMatchRepository = FakeFlickrRepository()
        val tags = emptyList<String>()
        fakeMatchRepository.throwableToReturnInMarketPriceDataFlow = IOException()

        // when
        val viewModel =
            FeedScreenViewModel(fakeMatchRepository)

        // then
        assertEquals(UiState.Error(tags, "IOException"), viewModel.uiState.value)
    }

    @Test
    fun `when non-IOException thrown, Unknown error is emitted`() = runTest {
        // given
        val fakeMatchRepository = FakeFlickrRepository()
        val tags = emptyList<String>()
        fakeMatchRepository.throwableToReturnInMarketPriceDataFlow = Exception()

        // when
        val viewModel =
            FeedScreenViewModel(fakeMatchRepository)

        // then
        assertEquals(UiState.Error(tags, "Unknown Error"), viewModel.uiState.value)
    }
}
