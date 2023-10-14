package com.example.perfumeshop.presentation.features.start.ask_feature.navigation


import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.features.start.ask_feature.ui.AskLoginScreen

const val askRoute = "ask"

fun NavController.navigateToAsk(navOptions: NavOptions? = null) {
    this.navigate(route = askRoute, navOptions = navOptions)
}

fun NavGraphBuilder.askScreen(
    navigateAuth: () -> Unit,
    onSkipClick: () -> Unit,
) {
    // TODO: Handle back stack for each top-level destination. At the moment each top-level
    // destination may have own search screen's back stack.
    composable(route = askRoute) {
        AskLoginScreen(onLoginClick = navigateAuth, onSkipClick = onSkipClick)
    }
}