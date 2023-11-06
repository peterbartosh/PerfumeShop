package com.example.perfumeshop.presentation.features.app_blocked.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.features.app_blocked.ui.AppBlockedScreen

const val appBlockedRoute = "app_blocked"

fun NavController.navigateToAppBlocked(navOptions: NavOptions? = null) {
    this.navigate(appBlockedRoute, navOptions)
}

fun NavGraphBuilder.appBlockedScreen(
    navigateToHome : () -> Unit,
    navigateToAsk: () -> Unit
) {
    composable(route = appBlockedRoute) {
        AppBlockedScreen(navigateToHome = navigateToHome, navigateToAsk = navigateToAsk)
    }
}