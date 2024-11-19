package com.szoldapps.flickrhometask.data

import com.szoldapps.flickrhometask.data.model.FeedScreenData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

internal class FakeFlickrRepository : FlickrRepository {

    private val feedScreenData = MutableSharedFlow<FeedScreenData>()

    var throwableToReturnInMarketPriceDataFlow: Throwable? = null

    suspend fun emit(value: FeedScreenData) = feedScreenData.emit(value)

    override fun getPhotosPublicFeed(tags: List<String>?): Flow<FeedScreenData> =
        if (throwableToReturnInMarketPriceDataFlow != null) {
            flow { throw throwableToReturnInMarketPriceDataFlow as Throwable }
        } else {
            feedScreenData
        }
}
