package com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.ui.EditProfileScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.ui.EditProfileViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profileActiveChild


const val editProfileRoute = "edit"


fun NavController.navigateToEditProfileRoute(navOptions: NavOptions? = null) {
    profileActiveChild = editProfileRoute
    this.navigate(route = editProfileRoute, navOptions = navOptions)
}

fun NavGraphBuilder.editProfileScreen(onClick : () -> Unit) {
    composable(route = editProfileRoute) {
        val viewModel = hiltViewModel<EditProfileViewModel>()
        EditProfileScreen(viewModel = viewModel, onClick = onClick)
    }
}