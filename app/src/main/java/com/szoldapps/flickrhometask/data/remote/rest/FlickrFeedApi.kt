package com.szoldapps.flickrhometask.data.remote.rest

import retrofit2.http.GET
import retrofit2.http.Query

internal interface FlickrFeedApi {

    @GET("photos_public.gne?format=json&nojsoncallback=1")
    suspend fun photosPublicFeed(
        @Query("tags") tags: String? = null
    ): FeedDto

}
