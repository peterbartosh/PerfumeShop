package com.example.perfumeshop.presentation.features.main.settings_feature.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.components.ChildScreenAnimation
import com.example.perfumeshop.presentation.features.main.settings_feature.ui.SettingsScreen


const val settingsRoute = "settings"

fun NavOptionsBuilder.sss(){
    this.anim {  }
}

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {

    this.navigate(route = settingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onUserPreferencesChanged: (UserPreferencesType, Int) -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = settingsRoute) {
        ChildScreenAnimation {
            BackPressHandler(onBackPressed = onBackPressed)
            SettingsScreen(onUserPreferencesChanged = onUserPreferencesChanged)
        }
    }
}