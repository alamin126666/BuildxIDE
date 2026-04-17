package com.buildxide.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.buildxide.ui.navigation.AppNavGraph
import com.buildxide.ui.theme.BuildxIDETheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BuildxIDETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildxApp()
                }
            }
        }

        // Handle OAuth callback
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data
        if (data != null && data.scheme == "buildxide" && data.host == "oauth") {
            val code = data.getQueryParameter("code")
            if (code != null) {
                // Store the code to be handled by the LoginViewModel
                OAuthCallbackHolder.code = code
            }
        }
    }
}

@Composable
fun BuildxApp() {
    val navController = rememberNavController()

    BuildxIDETheme {
        AppNavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// Object to hold OAuth callback code
object OAuthCallbackHolder {
    var code: String? = null
}
