package com.example.perfumeshop.ui_layer.features.main.children.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.settings.ui.SettingsScreen


const val settingsHomeRoute = "home settings"
const val settingsCartRoute = "cart settings"
const val settingsProfileRoute = "profile settings"


fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    val route = when (this.currentDestination?.route.toString()) {
        homeRoute -> settingsHomeRoute
        cartRoute -> settingsCartRoute
        profileRoute -> settingsProfileRoute
        else -> ""
    }
    if (route.isNotEmpty())
        this.navigate(route = route, navOptions)
}

fun NavGraphBuilder.settingsScreen(onClick : () -> Unit = {}, onThemeChange : (Boolean) -> Unit) {
    composable(route = settingsHomeRoute) {
        SettingsScreen(onThemeChange = onThemeChange)
    }

    composable(route = settingsCartRoute) {
        SettingsScreen(onThemeChange = onThemeChange)
    }

    composable(route = settingsProfileRoute) {
        SettingsScreen(onThemeChange = onThemeChange)
    }
}