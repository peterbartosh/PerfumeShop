package com.example.perfumeshop.presentation.features.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.perfumeshop.presentation.features.auth.code_verification_feature.navigation.codeVerificationScreen
import com.example.perfumeshop.presentation.features.auth.login_feature.navigation.loginScreen

fun NavGraphBuilder.authGraph(
    navigateCodeVerification: () -> Unit,
    navigateHome: () -> Unit,
    navController: NavHostController,
    onBackPressed: () -> Unit
){

    loginScreen(onCodeSent = navigateCodeVerification, onSuccess = navigateHome, onBackPressed = onBackPressed)

    codeVerificationScreen(navController = navController, onSuccess = navigateHome, onBackPressed = onBackPressed)
}