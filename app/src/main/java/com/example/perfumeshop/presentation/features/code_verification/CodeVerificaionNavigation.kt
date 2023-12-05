package com.example.perfumeshop.presentation.features.code_verification

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.components.BackPressHandler
import com.example.perfumeshop.presentation.features.login.AuthViewModel


const val codeVerificationRoute = "code"

fun NavController.navigateToCodeVerification(navOptions: NavOptions? = null) {
    this.navigate(route = codeVerificationRoute, navOptions = null)
}

fun NavGraphBuilder.codeVerificationScreen(
    onSuccess: () -> Unit,
    navController: NavHostController,
    onBackPressed: () -> Unit
) {
    composable(route = codeVerificationRoute) {
        navController.previousBackStackEntry?.let { previousBackStackEntry ->
            val viewModel =
                hiltViewModel<AuthViewModel>(viewModelStoreOwner = previousBackStackEntry)
            BackPressHandler(onBackPressed = onBackPressed)
            CodeVerificationScreen(authViewModel = viewModel, onSuccess = onSuccess)
        }
    }
}