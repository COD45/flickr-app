package com.szoldapps.flickrhometask.ui.image_viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
internal fun ImageViewerScreen(
    title: String,
    imgSmallUrl: String,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .systemBarsPadding()
            .navigationBarsPadding()
    ) {
        val imgLargeUrl = imgSmallUrl.replace("_m.jpg", "_b.jpg")
        AsyncImage(
            model = imgLargeUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .testTag(IMAGE_VIEWER_ASYNC_IMAGE_TEST_TAG),
        )
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .size(TOP_APP_BAR_HEIGHT)
                .zIndex(8f)
                .testTag(IMAGE_VIEWER_CLOSE_BUTTON_TEST_TAG)
        ) {
            Icon(Icons.Filled.Close, "Close")
        }
        if (title.isNotBlank()) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 5,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
                    .testTag(IMAGE_VIEWER_TITLE_TEST_TAG),
            )
        }
    }
}

internal val TOP_APP_BAR_HEIGHT = 64.dp
internal const val IMAGE_VIEWER_ASYNC_IMAGE_TEST_TAG = "IMAGE_VIEWER_ASYNC_IMAGE_TEST_TAG"
internal const val IMAGE_VIEWER_CLOSE_BUTTON_TEST_TAG = "IMAGE_VIEWER_CLOSE_BUTTON_TEST_TAG"
internal const val IMAGE_VIEWER_TITLE_TEST_TAG = "IMAGE_VIEWER_TITLE_TEST_TAG"
