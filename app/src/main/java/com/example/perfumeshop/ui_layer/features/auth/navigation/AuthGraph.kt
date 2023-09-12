package com.example.perfumeshop.ui_layer.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.ui_layer.features.auth.children.code_verif.navigation.codeVerificationScreen
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation.loginScreen


//const val authRoute = "auth"
//
//fun NavController.navigateToAuth(){
//    this.navigateToGraph(graphRoute = authRoute, saveStartDest = true){}
//}


fun NavGraphBuilder.authGraph(navigateCodeVerification : () -> Unit, navigateMain : () -> Unit){


    //navigation(startDestination = loginAskRoute, route = authRoute) {

        //setCurrentNestedGraph(build())

        loginScreen(onCodeSent = navigateCodeVerification, onSuccess = navigateMain)

        codeVerificationScreen(onSuccess = navigateMain)

    //}

}