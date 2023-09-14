package com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.LoginScreen
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.AuthViewModel
import com.example.perfumeshop.ui_layer.features.start.children.ask.navigation.askRoute


const val loginRoute = "login"

var loginParentRoute = askRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    loginParentRoute = this.currentDestination?.route ?: askRoute
    this.navigate(route = loginRoute, navOptions = navOptions)
}

fun NavGraphBuilder.loginScreen(
    onCodeSent: () -> Unit,
    onSuccess: () -> Unit
) {
    composable(route = loginRoute) {
        val viewModel = hiltViewModel<AuthViewModel>()
        LoginScreen(viewModel = viewModel,onCodeSent = onCodeSent, onSuccess = onSuccess)
    }



}