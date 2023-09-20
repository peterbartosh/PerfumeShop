package com.example.perfumeshop.ui_layer.features.main.settings_feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.settings_feature.ui.SettingsScreen


const val settingsRoute = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(route = settingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onClick: () -> Unit = {},
    onThemeChange: (Boolean) -> Unit) {
    composable(route = settingsRoute) {
        SettingsScreen(onThemeChange = onThemeChange)
    }
}