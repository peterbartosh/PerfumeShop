package com.example.perfumeshop.presentation.features.start.splash_feature.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.features.start.splash_feature.ui.SplashScreen
import com.example.perfumeshop.presentation.features.start.splash_feature.ui.SplashViewModel


const val splashRoute = "splash"

fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    this.navigate(splashRoute, navOptions)
}

fun NavGraphBuilder.splashScreen(
    navigateAsk: () -> Unit,
    navigateHome: () -> Unit
) {
    composable(route = splashRoute) {
        val splashViewModel = hiltViewModel<SplashViewModel>()
        SplashScreen(navigateAsk, navigateHome, splashViewModel)
    }
}