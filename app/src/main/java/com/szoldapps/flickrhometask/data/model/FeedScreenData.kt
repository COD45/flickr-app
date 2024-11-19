package com.szoldapps.flickrhometask.data.model

internal data class FeedScreenData(
    val title: String = "",
    val images: List<FeedScreenImage> = emptyList(),
)

internal data class FeedScreenImage(
    val title: String = "",
    val link: String = "",
)
