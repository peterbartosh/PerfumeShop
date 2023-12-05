package com.example.perfumeshop.presentation.features.edit_profile

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.components.BackPressHandler
import com.example.perfumeshop.presentation.app.navigation.nested.profileActiveChild


const val editProfileRoute = "edit"


fun NavController.navigateToEditProfileRoute(navOptions: NavOptions? = null) {
    profileActiveChild = editProfileRoute
    this.navigate(route = editProfileRoute, navOptions = navOptions)
}

fun NavGraphBuilder.editProfileScreen(onClick: () -> Unit, onBackPressed: () -> Unit) {
    composable(route = editProfileRoute) {
        val editProfileViewModel = hiltViewModel<EditProfileViewModel>()

        BackPressHandler(onBackPressed = onBackPressed)
        EditProfileScreen(editProfileViewModel = editProfileViewModel)
    }
}