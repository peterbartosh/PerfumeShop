package com.example.perfumeshop.presentation.features.auth.login_register_feature.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.LoginScreen
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.AuthViewModel
import com.example.perfumeshop.presentation.features.start.ask_feature.navigation.askRoute


const val loginRoute = "login"

var loginParentRoute = askRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    loginParentRoute = this.currentDestination?.route ?: askRoute
    this.navigate(route = loginRoute, navOptions = navOptions)
}

fun NavGraphBuilder.loginScreen(
    onCodeSent: () -> Unit,
    onSuccess: () -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = loginRoute) {
        val viewModel = hiltViewModel<AuthViewModel>()
        BackPressHandler(onBackPressed = onBackPressed)
        LoginScreen(authViewModel = viewModel, onCodeSent = onCodeSent, onSuccess = onSuccess)
    }



}