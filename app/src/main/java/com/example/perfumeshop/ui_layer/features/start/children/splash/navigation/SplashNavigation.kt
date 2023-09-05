package com.example.perfumeshop.ui_layer.features.start.children.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.start.children.splash.ui.SplashScreen


const val splashRoute = "splash"

fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    this.navigate(splashRoute, navOptions)
}

fun NavGraphBuilder.splashScreen(
    navigateAsk: () -> Unit,
    navigateMain: () -> Unit
) {
        // TODO: Handle back stack for each top-level destination. At the moment each top-level
        // destination may have own search screen's back stack.
        composable(route = splashRoute) {
            SplashScreen(navigateAsk = navigateAsk, navigateMain = navigateMain)
        }
    }