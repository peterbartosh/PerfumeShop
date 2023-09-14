package com.example.perfumeshop.ui_layer.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.auth.code_verification_feature.navigation.navigateToCodeVerification
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.navigation.navigateToLogin
import com.example.perfumeshop.ui_layer.features.auth.authGraph
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.mainGraph
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.navigation.navigateToHome
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.navigation.navigateToSearch
import com.example.perfumeshop.ui_layer.features.main.product_feature.navigation.navigateToProduct
import com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.navigation.navigateToEditProfileRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.navigation.navigateToFavouritesRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.navigation.navigateToOrders
import com.example.perfumeshop.ui_layer.features.start.ask_feature.navigation.navigateToAsk
import com.example.perfumeshop.ui_layer.features.start.splash_feature.navigation.splashRoute
import com.example.perfumeshop.ui_layer.features.start.startGraph



@Composable
fun AppNavHost(
    navController: NavHostController,
    onThemeChange: (Boolean) -> Unit,
    homeViewModel : HomeViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    NavHost(navController = navController, startDestination = splashRoute){

        startGraph(navigateAsk = navController::navigateToAsk,
                   navigateMain = navController::navigateToHome,
                   navigateAuth = navController::navigateToLogin)

        authGraph(
            navController = navController,
            navigateCodeVerification = navController::navigateToCodeVerification,
            navigateMain = navController::navigateToHome
        )

        mainGraph(
            homeViewModel = homeViewModel,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onThemeChange = onThemeChange,
            navigateSearch = { query, queryType -> navController.navigateToSearch(
                query = query, queryType = queryType, navOptions = navOptions {
                    popUpTo(navController.currentDestination?.id ?:
                        navController.graph.findStartDestination().id){
                        saveState = true
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }) },
            navigateOption = { optionType ->
                when(optionType){
                    OptionType.Edit -> navController.navigateToEditProfileRoute(navOptions = navOptions {
                        popUpTo(navController.currentDestination?.id ?:
                        navController.graph.findStartDestination().id){
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    })

                    OptionType.Auth -> navController.navigateToLogin(navOptions = navOptions {
                        popUpTo(navController.currentDestination?.id ?:
                        navController.graph.findStartDestination().id){
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    })

                    OptionType.Orders -> navController.navigateToOrders(navOptions = navOptions {
                        popUpTo(navController.currentDestination?.id ?:
                        navController.graph.findStartDestination().id){
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    })
                    OptionType.Favourite -> navController.navigateToFavouritesRoute(navOptions = navOptions {
                        popUpTo(navController.currentDestination?.id ?:
                        navController.graph.findStartDestination().id){
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    })
                    else -> {}
            }},
            navigateProduct = { productId ->
                navController.navigateToProduct(productId = productId, navOptions = navOptions {
                    popUpTo(navController.currentDestination?.id ?:
                    navController.graph.findStartDestination().id){
                        saveState = true
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                })
            }
        )
    }
}