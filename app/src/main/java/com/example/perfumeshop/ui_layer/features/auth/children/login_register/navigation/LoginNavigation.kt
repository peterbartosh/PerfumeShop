package com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.LoginScreen
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.AuthViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.start.children.ask.navigation.askRoute


const val loginAskRoute = "ask login"
const val loginProfileRoute = "profile login"


fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    val route = if (this.currentDestination?.route.toString() == askRoute) loginAskRoute else loginProfileRoute
    this.navigate(route = route, navOptions = navOptions)
}

fun NavGraphBuilder.loginScreen(onCodeSent : () -> Unit, onSuccess : () -> Unit) {
    composable(route = loginAskRoute) {
        val viewModel = hiltViewModel<AuthViewModel>()
        LoginScreen(viewModel = viewModel,onCodeSent = onCodeSent, onSuccess = onSuccess)
    }

    composable(route = loginProfileRoute) {
        val viewModel = hiltViewModel<AuthViewModel>()
        LoginScreen(viewModel = viewModel, onCodeSent = onCodeSent, onSuccess = onSuccess)
    }

}