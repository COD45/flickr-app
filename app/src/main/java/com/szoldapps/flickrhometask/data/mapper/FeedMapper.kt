package com.szoldapps.flickrhometask.data.mapper

import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.model.FeedScreenImage
import com.szoldapps.flickrhometask.data.remote.rest.FeedDto

internal fun FeedDto.mapToFeedData(): FeedScreenData =
    FeedScreenData(
        title = title,
        images = items.map { item ->
            FeedScreenImage(
                title = item.title,
                link = item.media.mediaLink
            )
        }
    )
