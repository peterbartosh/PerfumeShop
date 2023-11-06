package com.example.perfumeshop.presentation.features.start.splash_feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.features.start.splash_feature.ui.SplashScreen


const val splashRoute = "splash"

fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    this.navigate(splashRoute, navOptions)
}

fun NavGraphBuilder.splashScreen(
    navigateAsk: () -> Unit,
    navigateMain: () -> Unit
) {
    composable(route = splashRoute) {
        SplashScreen(navigateAsk = navigateAsk, navigateHome = navigateMain)
    }
}