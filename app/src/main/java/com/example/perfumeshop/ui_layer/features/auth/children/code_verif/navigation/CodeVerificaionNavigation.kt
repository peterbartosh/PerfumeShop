package com.example.perfumeshop.ui_layer.features.auth.children.code_verif.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation.loginAskRoute
import com.example.perfumeshop.ui_layer.features.auth.children.code_verif.ui.CodeVerificationScreen
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.AuthViewModel


const val codeAskVerification = "ask login code"
const val codeProfileVerification = "profile login code"


fun NavController.navigateToCodeVerification(navOptions: NavOptions? = null) {
    val route = if (this.currentDestination?.route.toString() == loginAskRoute) codeAskVerification else codeProfileVerification
    this.navigate(route = route, navOptions = navOptions)
}

fun NavGraphBuilder.codeVerificationScreen(onSuccess : () -> Unit) {
    composable(route = codeAskVerification) {
        val viewModel = hiltViewModel<AuthViewModel>()
        CodeVerificationScreen(viewModel = viewModel, onSuccess = onSuccess)
    }

    composable(route = codeProfileVerification) {
        val viewModel = hiltViewModel<AuthViewModel>()
        CodeVerificationScreen(viewModel = viewModel, onSuccess = onSuccess)
    }

}