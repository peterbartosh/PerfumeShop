package com.example.perfumeshop.presentation.features.first_ask


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val askRoute = "ask"

fun NavController.navigateToAsk(navOptions: NavOptions? = null) {
    this.navigate(route = askRoute, navOptions = navOptions)
}

fun NavGraphBuilder.askScreen(
    navigateAuth: () -> Unit,
    onSkipClick: () -> Unit,
) {

    composable(route = askRoute) {
        AskLoginScreen(onLoginClick = navigateAuth, onSkipClick = onSkipClick)
    }
}