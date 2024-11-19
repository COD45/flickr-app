package com.szoldapps.flickrhometask.app.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.szoldapps.flickrhometask.common.FakeFlickrRepository
import com.szoldapps.flickrhometask.data.model.FeedScreenData
import com.szoldapps.flickrhometask.data.model.FeedScreenImage
import com.szoldapps.flickrhometask.ui.feed.FEED_SCREEN_PHOTO_GRID_TEST_TAG
import com.szoldapps.flickrhometask.ui.feed.FeedScreenViewModel
import com.szoldapps.flickrhometask.ui.image_viewer.IMAGE_VIEWER_ASYNC_IMAGE_TEST_TAG
import com.szoldapps.flickrhometask.ui.image_viewer.IMAGE_VIEWER_CLOSE_BUTTON_TEST_TAG
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController
    private lateinit var fakeFlickrRepository: FakeFlickrRepository

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            fakeFlickrRepository = FakeFlickrRepository()
            FlickrHomeTaskNavHost(
                navController = navController,
                feedScreenViewModel = FeedScreenViewModel(fakeFlickrRepository)
            )
        }
    }

    @Test
    fun testNavigationToImageViewer() {
        // given
        runBlocking {
            fakeFlickrRepository.emit(
                FeedScreenData(
                    title = "Test Title",
                    images = listOf(FeedScreenImage(title = "title1", link = "link1"))
                ),
            )
        }

        // when
        composeTestRule.onNodeWithTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG)
            .onChildren()
            .assertCountEquals(1)
            .onFirst()
            .performClick()

        // then
        composeTestRule.onNodeWithTag(IMAGE_VIEWER_ASYNC_IMAGE_TEST_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun testNavigationToImageViewerAndBack() {
        // given
        runBlocking {
            fakeFlickrRepository.emit(
                FeedScreenData(
                    title = "Test Title",
                    images = listOf(FeedScreenImage(title = "title1", link = "link1"))
                ),
            )
        }
        composeTestRule.onNodeWithTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG)
            .onChildren()
            .onFirst()
            .performClick()

        // when
        composeTestRule.onNodeWithTag(IMAGE_VIEWER_CLOSE_BUTTON_TEST_TAG)
            .performClick()

        // then
        composeTestRule.onNodeWithTag(FEED_SCREEN_PHOTO_GRID_TEST_TAG)
            .onChildren()
            .assertCountEquals(1)
    }
}
