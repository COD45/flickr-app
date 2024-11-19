package com.szoldapps.flickrhometask.ui.feed

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.testing.TestNavHostController
import com.szoldapps.flickrhometask.app.theme.FlickrHomeTaskTheme
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.model.FeedScreenImage
import org.junit.Rule
import org.junit.Test

internal class FeedScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testIfTopSearchBarIsDisplayed() {
        // given & when
        composeTestRule.setContent {
            FlickrHomeTaskTheme {
                FeedScreen(
                    uiState = FeedScreenViewModel.UiState.Content(
                        tags = emptyList(),
                        feedScreenData = FeedScreenData(
                            title = "Test Title",
                        ),
                    ),
                    onRefresh = {},
                    onAddTag = {},
                    onClearTags = {},
                    navController = TestNavHostController(LocalContext.current),
                )
            }
        }

        // then
        composeTestRule.onNodeWithTag(FEED_SCREEN_TOP_APP_BAR_TITLE_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Refresh").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }

    @Test
    fun testPhotoGrid() {
        // given & when
        val title1 = "title1"
        val title2 = "title2"
        composeTestRule.setContent {
            FlickrHomeTaskTheme {
                FeedScreen(
                    uiState = FeedScreenViewModel.UiState.Content(
                        tags = emptyList(),
                        feedScreenData = FeedScreenData(
                            title = "Test Title",
                            images = listOf(
                                FeedScreenImage(title = title1, link = "link1"),
                                FeedScreenImage(title = title2, link = "link2"),
                            )
                        ),
                    ),
                    onRefresh = {},
                    onAddTag = {},
                    onClearTags = {},
                    navController = TestNavHostController(LocalContext.current),
                )
            }
        }

        // then
        composeTestRule.onNodeWithTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG)
            .onChildren()
            .assertCountEquals(2)
            .onFirst()
            .assert(hasText(title1))
        composeTestRule.onNodeWithTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG)
            .onChildren()
            .onLast()
            .assert(hasText(title2))
    }

}
