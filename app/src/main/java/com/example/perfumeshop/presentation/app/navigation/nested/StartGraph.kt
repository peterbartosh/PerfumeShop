package com.example.perfumeshop.presentation.app.navigation.nested

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.presentation.features.first_ask.askScreen
import com.example.perfumeshop.presentation.features.splash.splashScreen

fun NavGraphBuilder.startGraph(
    navigateAsk: () -> Unit,
    navigateHome: () -> Unit,
    navigateAuth: () -> Unit
){
    splashScreen(navigateAsk = navigateAsk, navigateHome = navigateHome)

    askScreen(navigateAuth = navigateAuth, onSkipClick = navigateHome)
}