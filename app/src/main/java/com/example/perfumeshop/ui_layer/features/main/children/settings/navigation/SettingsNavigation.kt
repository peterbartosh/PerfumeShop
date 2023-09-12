package com.example.perfumeshop.ui_layer.features.main.children.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.settings.ui.SettingsScreen


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