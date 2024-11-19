package com.szoldapps.flickrhometask.ui.feed

import android.view.KeyEvent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.szoldapps.flickrhometask.app.navigation.Route
import com.szoldapps.flickrhometask.app.theme.FlickrHomeTaskTheme
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.model.FeedScreenImage
import com.szoldapps.flickrhometask.ui.feed.FeedScreenViewModel.UiState
import kotlinx.coroutines.android.awaitFrame
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
internal fun FeedScreen(
    viewModel: FeedScreenViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FeedScreen(
        uiState = uiState,
        modifier = modifier,
        onRefresh = viewModel::refresh,
        onAddTag = viewModel::addTag,
        onClearTags = viewModel::clearTags,
        navController = navController,
    )
}

@Composable
internal fun FeedScreen(
    uiState: UiState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onAddTag: (String) -> Unit,
    onClearTags: () -> Unit,
    navController: NavHostController,
) {
    Column(modifier) {
        TopSearchBar(uiState, onRefresh, onAddTag, onClearTags)
        when (uiState) {
            is UiState.Content -> PhotoGrid(uiState.feedScreenData, navController)
            is UiState.Error -> Error(uiState)
            is UiState.Loading -> Loading()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopSearchBar(
    uiState: UiState,
    onRefresh: () -> Unit,
    onAddTag: (String) -> Unit,
    onClearTags: () -> Unit,
) {
    val searchOpen = rememberSaveable { mutableStateOf(false) }
    Column {
        TopAppBar(
            title = {
                Text(
                    "Flickr: Uploads from everyone",
                    modifier = Modifier.testTag(FEED_SCREEN_TOP_APP_BAR_TITLE_TEST_TAG)
                )
            },
            actions = {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Filled.Refresh, "Refresh")
                }
                IconButton(onClick = { searchOpen.value = !searchOpen.value }) {
                    Icon(Icons.Filled.Search, "Search")
                }
            },
        )
        if (searchOpen.value) {
            SearchTextFieldAndTagsRow(uiState, onAddTag, onClearTags)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchTextFieldAndTagsRow(
    uiState: UiState,
    onAddTag: (String) -> Unit,
    onClearTags: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var text by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { textStr -> text = textStr },
        singleLine = true,
        label = { Text("Search for tags") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    onAddTag(text.filterNot { char -> char == '\n' })
                    text = ""
                    return@onKeyEvent true
                }
                false
            }
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onAddTag(text.filterNot { char -> char == '\n' })
                text = ""
            },
        ),
    )
    // open keyboard
    LaunchedEffect(focusRequester) {
        awaitFrame()
        focusRequester.requestFocus()
    }

    TagsRow(uiState, onClearTags)
}

@Composable
private fun TagsRow(
    uiState: UiState,
    onClearTags: () -> Unit
) {
    Row(
        modifier = Modifier
            .defaultMinSize(minHeight = 48.dp)
            .padding(
                start = 16.dp,
                end = 4.dp,
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Tags: ${uiState.tags.joinToString(" | ")}",
            modifier = Modifier.weight(1f),
        )
        if (uiState.tags.isNotEmpty()) {
            IconButton(
                onClick = { onClearTags() },
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Icon(Icons.Filled.Clear, null)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PhotoGrid(
    feedScreenData: FeedScreenData,
    navController: NavHostController,
) {
    val state = rememberLazyStaggeredGridState()
    if (state.isScrollInProgress) {
        val focusManager = LocalFocusManager.current
        focusManager.clearFocus()
    }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp),
        verticalItemSpacing = 4.dp,
        state = state,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG)
    ) {
        items(feedScreenData.images) { image ->
            Photo(image, navController)
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun Photo(
    image: FeedScreenImage,
    navController: NavHostController,
) {
    Box(
        modifier = Modifier
            .clickable {
                val encodedUrl =
                    URLEncoder.encode(image.link, StandardCharsets.UTF_8.toString())
                val encodedTitle = image.title.replace("/", "\\")
                navController.navigate("${Route.imageScreen}/${encodedTitle}/${encodedUrl}")
            }
    ) {
        AsyncImage(
            model = image.link,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        if (image.title.isNotBlank()) {
            Text(
                text = image.title,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(4.dp)
                    .align(Alignment.BottomStart),
            )
        }
    }
}

@Composable
private fun Error(errorState: UiState.Error) {
    Text(
        text = "ERROR:\n${errorState.message}",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp)
    )
}

@Composable
private fun Loading() {
    Text(
        text = "LOADING...",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp)
    )
}

internal const val FEED_SCREEN_TOP_APP_BAR_TITLE_TEST_TAG = "FEED_SCREEN_TOP_APP_BAR_TITLE_TEST_TAG"
internal const val FEED_SCREEN_PHOTO_GRID_TEST_TAG = "FEED_SCREEN_PHOTO_GRID_TEST_TAG"

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    FlickrHomeTaskTheme {
        FeedScreen(
            uiState = UiState.Content(
                tags = emptyList(),
                feedScreenData = FeedScreenData(
                    title = "Test Title",
                ),
            ),
            onRefresh = {},
            onAddTag = {},
            onClearTags = {},
            navController = NavHostController(LocalContext.current),
        )
    }
}
