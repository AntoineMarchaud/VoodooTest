package com.amarchaud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amarchaud.ui.screen.MainComposable
import com.amarchaud.ui.theme.VoodooTestTheme
import com.example.voodoosdk.ui.AdComposable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Serializable
    object Home

    @Serializable
    object AdDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            VoodooTestTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Home,
                ) {
                    composable<Home> {
                        MainComposable(
                            onDisplayAd = {
                                navController.navigate(AdDetail)
                            }
                        )
                    }

                    composable<AdDetail> {
                        AdComposable(
                            onAdClose = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}