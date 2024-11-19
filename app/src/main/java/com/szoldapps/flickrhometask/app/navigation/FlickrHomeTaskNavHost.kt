package com.szoldapps.flickrhometask.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.szoldapps.flickrhometask.ui.feed.FeedScreen
import com.szoldapps.flickrhometask.ui.feed.FeedScreenViewModel
import com.szoldapps.flickrhometask.ui.image_viewer.ImageViewerScreen

@Composable
internal fun FlickrHomeTaskNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    feedScreenViewModel: FeedScreenViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.feedScreen,
        modifier = modifier
    ) {
        composable(Route.feedScreen) {
            FeedScreen(
                viewModel = feedScreenViewModel,
                navController = navController
            )
        }
        composable(
            route = "${Route.imageScreen}/{title}/{imgUrl}",
            arguments = listOf(
                navArgument("imgUrl") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            ),
        ) { navBackStackEntry ->
            val imgUrl = navBackStackEntry.arguments?.getString("imgUrl") ?: "imgUrl NOT FOUND"
            val title = navBackStackEntry.arguments?.getString("title") ?: "Title NOT FOUND"
            ImageViewerScreen(title, imgUrl, navController)
        }
    }
}
