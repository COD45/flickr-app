package com.szoldapps.flickrhometask.data.remote.rest

import com.google.gson.annotations.SerializedName

internal data class FeedDto(
    @SerializedName("title") val title: String,
    @SerializedName("items") val items: List<FeedItemDto>,
)

internal data class FeedItemDto(
    @SerializedName("title") val title: String,
    @SerializedName("media") val media: MediaDto,
)

internal data class MediaDto(
    @SerializedName("m") val mediaLink: String,
)
