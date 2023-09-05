package com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.ui.EditProfileScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.ui.EditProfileViewModel



const val editProfileRoute = "profile editProfile"


fun NavController.navigateToEditProfileRoute(navOptions: NavOptions? = null) {
    this.navigate(editProfileRoute, navOptions)
}

fun NavGraphBuilder.editProfileScreen(onClick : () -> Unit) {
    composable(route = editProfileRoute) {
        val viewModel = hiltViewModel<EditProfileViewModel>()
        EditProfileScreen(viewModel = viewModel, onClick = onClick)
    }
}