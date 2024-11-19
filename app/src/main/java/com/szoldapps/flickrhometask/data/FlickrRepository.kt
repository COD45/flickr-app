package com.szoldapps.flickrhometask.data

import com.szoldapps.flickrhometask.data.model.FeedScreenData
import kotlinx.coroutines.flow.Flow

internal interface FlickrRepository {
    fun getPhotosPublicFeed(tags: List<String>? = null): Flow<FeedScreenData>
}
