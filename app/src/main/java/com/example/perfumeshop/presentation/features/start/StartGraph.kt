package com.example.perfumeshop.presentation.features.start

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.presentation.features.start.ask_feature.navigation.askScreen
import com.example.perfumeshop.presentation.features.start.splash_feature.navigation.splashScreen

//const val startGraphRoute = "startGraph"

//fun NavController.navigateToStartGraph(){
//    this.navigateToGraph(graphRoute = startGraphRoute, saveStartDest = false){}
//}

fun NavGraphBuilder.startGraph(
    navigateAsk: () -> Unit,
    navigateHome: () -> Unit,
    navigateAuth: () -> Unit,
){
    splashScreen(navigateAsk = navigateAsk, navigateMain = navigateHome)

    askScreen(navigateAuth = navigateAuth, onSkipClick = navigateHome)
}