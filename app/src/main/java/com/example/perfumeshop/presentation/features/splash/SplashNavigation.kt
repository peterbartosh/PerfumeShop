package com.example.perfumeshop.presentation.features.splash

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable


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