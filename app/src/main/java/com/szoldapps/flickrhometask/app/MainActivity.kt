package com.szoldapps.flickrhometask.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.szoldapps.flickrhometask.app.navigation.FlickrHomeTaskNavHost
import com.szoldapps.flickrhometask.app.theme.FlickrHomeTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            FlickrHomeTaskApp()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FlickrHomeTaskApp() {
    val navController = rememberNavController()
    FlickrHomeTaskTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .semantics {
                    testTagsAsResourceId = true
                },
            color = MaterialTheme.colorScheme.background
        ) {
            FlickrHomeTaskNavHost(navController = navController)
        }
    }
}
