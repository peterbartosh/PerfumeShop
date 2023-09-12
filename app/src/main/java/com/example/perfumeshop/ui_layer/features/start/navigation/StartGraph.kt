package com.example.perfumeshop.ui_layer.features.start.navigation

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.ui_layer.features.start.children.ask.navigation.askScreen
import com.example.perfumeshop.ui_layer.features.start.children.splash.navigation.splashScreen

//const val startGraphRoute = "startGraph"

//fun NavController.navigateToStartGraph(){
//    this.navigateToGraph(graphRoute = startGraphRoute, saveStartDest = false){}
//}

fun NavGraphBuilder.startGraph(
    navigateAsk : () -> Unit,
    navigateMain : () -> Unit,
    navigateAuth : () -> Unit
){
   // navigation(startDestination = splashRoute, route = startGraphRoute){

        //setCurrentNestedGraph(build())

        splashScreen(navigateAsk = navigateAsk, navigateMain = navigateMain)

        askScreen(navigateAuth = navigateAuth, onSkipClick = navigateMain)

    //}
}