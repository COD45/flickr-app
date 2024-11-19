package com.szoldapps.flickrhometask.data

import com.szoldapps.flickrhometask.common.CoroutineTestExtension
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.model.FeedScreenImage
import com.szoldapps.flickrhometask.data.remote.rest.FakeFlickrFeedApi
import com.szoldapps.flickrhometask.data.remote.rest.FeedDto
import com.szoldapps.flickrhometask.data.remote.rest.FeedItemDto
import com.szoldapps.flickrhometask.data.remote.rest.FlickrFeedApi
import com.szoldapps.flickrhometask.data.remote.rest.MediaDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
internal class FlickrRepositoryImplTest {

    @Test
    fun `getPhotosPublicFeed() returns correct FeedScreenData`() = runTest {
        // given
        val title = "title"
        val title1 = "item 1 title"
        val title2 = "item 2 title"
        val mediaLink1 = "item 1 media link"
        val mediaLink2 = "item 2 media link"
        val items = listOf(
            FeedItemDto(title = title1, media = MediaDto(mediaLink = mediaLink1)),
            FeedItemDto(title = title2, media = MediaDto(mediaLink = mediaLink2))
        )
        val feedDto = FeedDto(title = title, items = items)
        val expectedFeedScreenData = FeedScreenData(
            title = title,
            images = listOf(
                FeedScreenImage(title = title1, link = mediaLink1),
                FeedScreenImage(title = title2, link = mediaLink2),
            )
        )
        val flickrFeedApi: FlickrFeedApi = FakeFlickrFeedApi().apply {
            photosPublicFeedFeedDtoReturn = feedDto
        }
        val flickrRepository = FlickrRepositoryImpl(flickrFeedApi)

        // when
        val feedScreenDataFlow = flickrRepository.getPhotosPublicFeed()

        // then
        assertEquals(expectedFeedScreenData, feedScreenDataFlow.first())
    }

    @Test
    fun `flickrFeedApi-photosPublicFeed is called with null when tags null`() = runTest {
        // given
        val flickrFeedApi = FakeFlickrFeedApi().apply {
            photosPublicFeedFeedDtoReturn = FeedDto(title = "title", items = emptyList())
        }
        val flickrRepository = FlickrRepositoryImpl(flickrFeedApi)

        // when
        flickrRepository.getPhotosPublicFeed(tags = null).collect()

        // then
        assertEquals(null, flickrFeedApi.tagsArgumentOfPhotosPublicFeed)
    }

    @Test
    fun `flickrFeedApi-photosPublicFeed is called with null when tags empty`() = runTest {
        // given
        val flickrFeedApi = FakeFlickrFeedApi().apply {
            photosPublicFeedFeedDtoReturn = FeedDto(title = "title", items = emptyList())
        }
        val flickrRepository = FlickrRepositoryImpl(flickrFeedApi)

        // when
        flickrRepository.getPhotosPublicFeed(tags = emptyList()).collect()

        // then
        assertEquals(null, flickrFeedApi.tagsArgumentOfPhotosPublicFeed)
    }

    @Test
    fun `flickrFeedApi-photosPublicFeed is called with correct String`() = runTest {
        // given
        val flickrFeedApi = FakeFlickrFeedApi()
            .apply {
                photosPublicFeedFeedDtoReturn = FeedDto(title = "title", items = emptyList())
            }
        val flickrRepository = FlickrRepositoryImpl(flickrFeedApi)

        // when
        flickrRepository.getPhotosPublicFeed(tags = listOf("tag1", "tag2")).collect()

        // then
        assertEquals("tag1,tag2", flickrFeedApi.tagsArgumentOfPhotosPublicFeed)
    }

    @Test
    fun `flickrRepository-getPhotosPublicFeed fails when flickrFeedApi-photosPublicFeed fails`() {
        // given
        val unknownError = UnknownError("No idea about the Error")
        val flickrFeedApi = FakeFlickrFeedApi()
            .apply {
                photosPublicFeedFeedDtoReturn = FeedDto(title = "title", items = emptyList())
                throwableToReturnInPhotosPublicFeed = unknownError
            }
        val flickrRepository = FlickrRepositoryImpl(flickrFeedApi)

        // when & then
        assertThrows(UnknownError::class.java) {
            runTest {
                flickrRepository.getPhotosPublicFeed().collect()
            }
        }
    }
}
