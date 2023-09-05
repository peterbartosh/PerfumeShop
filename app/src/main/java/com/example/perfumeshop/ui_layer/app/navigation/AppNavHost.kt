package com.example.perfumeshop.ui_layer.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.auth.children.code_verif.navigation.navigateToCodeVerification
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation.navigateToLogin
import com.example.perfumeshop.ui_layer.features.auth.navigation.authGraph
import com.example.perfumeshop.ui_layer.features.auth.navigation.navigateToAuth
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.navigation.navigateToProductCart
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.navigateToProductHome
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.navigateToSearch
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.navigation.navigateToEditProfileRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.navigation.navigateToFavouritesRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation.navigateToOrders
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.navigation.navigateToProductProfile
import com.example.perfumeshop.ui_layer.features.main.children.settings.navigation.navigateToSettings
import com.example.perfumeshop.ui_layer.features.main.navigation.mainGraph
import com.example.perfumeshop.ui_layer.features.main.navigation.navigateToMainGraph
import com.example.perfumeshop.ui_layer.features.start.children.ask.navigation.navigateToAsk
import com.example.perfumeshop.ui_layer.features.start.navigation.startGraph
import com.example.perfumeshop.ui_layer.features.start.navigation.startGraphRoute

@Composable
fun AppNavHost(navController: NavHostController, onThemeChange : (Boolean) -> Unit) {

    NavHost(navController = navController, startDestination = startGraphRoute){


        startGraph(navigateAsk = navController::navigateToAsk,
                   navigateMain = navController::navigateToMainGraph,
                    navigateAuth = navController::navigateToAuth)

        authGraph(
            navigateCodeVerification = navController::navigateToCodeVerification,
            navigateMain = navController::navigateToMainGraph
        )

        mainGraph(
            onThemeChange = onThemeChange,
            navigateSearch = navController::navigateToSearch,
            navigateOption = { optionType ->
                when(optionType){
                    OptionType.Auth -> navController.navigateToEditProfileRoute()
                    OptionType.Orders -> navController.navigateToOrders()
                    OptionType.Favourite -> navController.navigateToFavouritesRoute()
                    else -> {}
            }},
            navigateProductHome =  navController::navigateToProductHome,
            navigateProductCart =  navController::navigateToProductCart,
            navigateProductProfile = navController::navigateToProductProfile
        )


    }
}