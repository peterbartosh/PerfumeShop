package com.example.perfumeshop.presentation.features.app_blocked.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.app_blocked.ui.AppBlockedScreen
import com.example.perfumeshop.presentation.features.app_blocked.ui.AppBlockedViewModel

const val appBlockedRoute = "app_blocked"

fun NavController.navigateToAppBlocked(navOptions: NavOptions? = null) {
    this.navigate(appBlockedRoute, navOptions)
}

fun NavGraphBuilder.appBlockedScreen(
    navigateHome: () -> Unit,
    navigateAsk: () -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = appBlockedRoute) {
        val appBlockedViewModel = hiltViewModel<AppBlockedViewModel>()
        BackPressHandler(onBackPressed = onBackPressed)
        AppBlockedScreen(navigateHome, navigateAsk, appBlockedViewModel)
    }
}