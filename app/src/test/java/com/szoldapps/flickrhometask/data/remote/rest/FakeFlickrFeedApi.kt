package com.szoldapps.flickrhometask.data.remote.rest

internal class FakeFlickrFeedApi : FlickrFeedApi {

    var photosPublicFeedFeedDtoReturn: FeedDto? = null

    var throwableToReturnInPhotosPublicFeed: Throwable? = null

    var tagsArgumentOfPhotosPublicFeed: String? = null
        private set

    override suspend fun photosPublicFeed(tags: String?): FeedDto {
        tagsArgumentOfPhotosPublicFeed = tags
        return if (throwableToReturnInPhotosPublicFeed != null || photosPublicFeedFeedDtoReturn == null) {
            throw throwableToReturnInPhotosPublicFeed as Throwable
        } else {
            photosPublicFeedFeedDtoReturn!!
        }
    }
}
