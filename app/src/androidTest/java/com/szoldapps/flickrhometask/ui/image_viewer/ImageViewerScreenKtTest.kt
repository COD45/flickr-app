package com.szoldapps.flickrhometask.ui.image_viewer

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.testing.TestNavHostController
import com.szoldapps.flickrhometask.app.theme.FlickrHomeTaskTheme
import org.junit.Rule
import org.junit.Test

internal class ImageViewerScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testIfUiElementsAreDisplayedCorrectly() {
        // given & when
        val title = "title"
        composeTestRule.setContent {
            FlickrHomeTaskTheme {
                ImageViewerScreen(
                    title = title,
                    imgSmallUrl = "imgSmallUrl",
                    navController = TestNavHostController(LocalContext.current),
                )
            }
        }

        // then
        composeTestRule.onNodeWithTag(IMAGE_VIEWER_CLOSE_BUTTON_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(IMAGE_VIEWER_ASYNC_IMAGE_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(IMAGE_VIEWER_TITLE_TEST_TAG)
            .assertIsDisplayed()
            .assert(hasText(title))
    }

    @Test
    fun testIfTitleDoesNotExistIfEmpty() {
        // given & when
        composeTestRule.setContent {
            FlickrHomeTaskTheme {
                ImageViewerScreen(
                    title = "",
                    imgSmallUrl = "imgSmallUrl",
                    navController = TestNavHostController(LocalContext.current),
                )
            }
        }

        // then
        composeTestRule.onNodeWithTag(IMAGE_VIEWER_TITLE_TEST_TAG).assertDoesNotExist()
    }
}
