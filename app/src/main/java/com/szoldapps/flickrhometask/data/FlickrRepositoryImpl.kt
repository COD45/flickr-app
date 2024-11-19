package com.szoldapps.flickrhometask.data

import com.szoldapps.flickrhometask.data.mapper.mapToFeedData
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.remote.rest.FlickrFeedApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class FlickrRepositoryImpl @Inject constructor(
    private val flickrFeedApi: FlickrFeedApi
) : FlickrRepository {

    override fun getPhotosPublicFeed(tags: List<String>?): Flow<FeedScreenData> = flow {
        val tagString = if (tags?.isNotEmpty() == true) tags.joinToString(",") else null
        emit(flickrFeedApi.photosPublicFeed(tagString).mapToFeedData())
    }.flowOn(Dispatchers.IO)
}
