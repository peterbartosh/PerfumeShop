package com.example.perfumeshop.presentation.app.navigation.nested

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.perfumeshop.presentation.features.code_verification.codeVerificationScreen
import com.example.perfumeshop.presentation.features.login.loginScreen

fun NavGraphBuilder.authGraph(
    navigateCodeVerification: () -> Unit,
    navigateHome: () -> Unit,
    navController: NavHostController,
    onBackPressed: () -> Unit
){

    loginScreen(onCodeSent = navigateCodeVerification, onSuccess = navigateHome, onBackPressed = onBackPressed)

    codeVerificationScreen(navController = navController, onSuccess = navigateHome, onBackPressed = onBackPressed)
}