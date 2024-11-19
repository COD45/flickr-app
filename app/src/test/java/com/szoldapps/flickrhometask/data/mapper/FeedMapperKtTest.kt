package com.szoldapps.flickrhometask.data.mapper

import com.szoldapps.flickrhometask.common.CoroutineTestExtension
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.model.FeedScreenImage
import com.szoldapps.flickrhometask.data.remote.rest.FeedDto
import com.szoldapps.flickrhometask.data.remote.rest.FeedItemDto
import com.szoldapps.flickrhometask.data.remote.rest.MediaDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutineTestExtension::class)
internal class FeedMapperKtTest {

    @Test
    fun `mapToFeedData() returns correct FeedScreenData`() = runTest {
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

        // when
        val feedScreenData = feedDto.mapToFeedData()

        // then
        assertEquals(expectedFeedScreenData, feedScreenData)
    }
}
