package com.example.perfumeshop.presentation.features.profile

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.presentation.app.components.BackPressHandler


const val profileRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(route = profileRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileScreen(
    onOptionClick: (OptionType) -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = profileRoute) {
        val profileViewModel = hiltViewModel<ProfileViewModel>()
        BackPressHandler(onBackPressed = onBackPressed)
        ProfileScreen(
            onOptionClick = onOptionClick,
            profileViewModel = profileViewModel
        )
    }
}