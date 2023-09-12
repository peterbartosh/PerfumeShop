package com.example.perfumeshop.ui_layer.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.auth.children.code_verif.navigation.navigateToCodeVerification
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation.navigateToLogin
import com.example.perfumeshop.ui_layer.features.auth.navigation.authGraph
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.navigateToHome
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.navigateToSearch
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.searchRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.navigation.navigateToEditProfileRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.navigation.navigateToFavouritesRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation.navigateToOrders
import com.example.perfumeshop.ui_layer.features.main.navigation.mainGraph
import com.example.perfumeshop.ui_layer.features.main.children.product.navigation.navigateToProduct
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.start.children.ask.navigation.navigateToAsk
import com.example.perfumeshop.ui_layer.features.start.children.splash.navigation.splashRoute
import com.example.perfumeshop.ui_layer.features.start.navigation.startGraph



@Composable
fun AppNavHost(
    navController: NavHostController,
    onThemeChange: (Boolean) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    NavHost(navController = navController, startDestination = splashRoute){

        startGraph(navigateAsk = navController::navigateToAsk,
                   navigateMain = navController::navigateToHome,
                   navigateAuth = navController::navigateToLogin)

        authGraph(
            navigateCodeVerification = navController::navigateToCodeVerification,
            navigateMain = navController::navigateToHome
        )

        mainGraph(
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
                    restoreState = homeActiveChild == searchRoute
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