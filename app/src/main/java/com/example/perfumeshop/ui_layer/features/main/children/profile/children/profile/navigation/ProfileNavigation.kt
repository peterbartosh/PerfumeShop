package com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.ui.ProfileScreen


const val profileRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(profileRoute, navOptions)
}

fun NavGraphBuilder.profileScreen(onOptionClick: (OptionType) -> Unit) {
    composable(route = profileRoute) {
        ProfileScreen(onOptionClick = onOptionClick)
    }
}