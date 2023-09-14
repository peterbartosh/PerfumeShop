package com.example.perfumeshop.ui_layer.features.auth.children.code_verif.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.auth.children.code_verif.ui.CodeVerificationScreen
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.AuthViewModel


const val codeVerificationRoute = "code"

fun NavController.navigateToCodeVerification(navOptions: NavOptions? = null) {
    this.navigate(route = codeVerificationRoute, navOptions = null)
}

fun NavGraphBuilder.codeVerificationScreen(onSuccess: () -> Unit, navController: NavHostController) {
    composable(route = codeVerificationRoute) {
        val viewModel = hiltViewModel<AuthViewModel>(viewModelStoreOwner = navController.previousBackStackEntry!!)
        CodeVerificationScreen(viewModel = viewModel, onSuccess = onSuccess)
    }
}