package com.example.perfumeshop.presentation.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.presentation.features.auth.authGraph
import com.example.perfumeshop.presentation.features.auth.code_verification_feature.navigation.navigateToCodeVerification
import com.example.perfumeshop.presentation.features.auth.login_register_feature.navigation.navigateToLogin
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.navigateToCart
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.navigation.navigateToOrderMaking
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.navigateToHome
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.navigateToSearch
import com.example.perfumeshop.presentation.features.main.mainGraph
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.navigateToProduct
import com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.navigation.navigateToEditProfileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation.navigateToFavouritesRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.orders.navigation.navigateToOrders
import com.example.perfumeshop.presentation.features.start.ask_feature.navigation.navigateToAsk
import com.example.perfumeshop.presentation.features.start.splash_feature.navigation.splashRoute
import com.example.perfumeshop.presentation.features.start.startGraph


@Composable
fun AppNavHost(
    navController: NavHostController,
    onThemeChange: (Boolean) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onBackPressed : () -> Unit
) {


    NavHost(navController = navController, startDestination = splashRoute){

        val startDest = this.build().startDestinationId

        val navOptions = navOptions {
            popUpTo(navController.currentDestination?.id ?: startDest) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }

        startGraph(
            navigateAsk = {
                navController.navigateToAsk(navOptions = navOptions)
                navController.backQueue.removeIf{ nbse -> nbse.destination.route == splashRoute}
            },
            navigateMain = {
               cartViewModel.loadUserProducts()
               favouriteViewModel.loadUserProducts()
               navController.navigateToHome(navOptions = null)
               navController.backQueue.removeIf{ nbse -> nbse.destination.route == splashRoute}
            },
            navigateAuth = { navController.navigateToLogin(navOptions = navOptions) }
        )

        authGraph(
            navController = navController,
            navigateCodeVerification = { navController.navigateToCodeVerification(navOptions = navOptions) },
            navigateMain = {
                cartViewModel.loadUserProducts()
                favouriteViewModel.loadUserProducts()
                navController.navigateToHome(navOptions = null)
            },
            onBackPressed = onBackPressed
        )

        mainGraph(
            //homeViewModel = homeViewModel,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onThemeChange = onThemeChange,
            navigateToCart = { navController.navigateToCart(navOptions = navOptions) },
            navigateToOrderMaking = { navController.navigateToOrderMaking(navOptions = navOptions) },
            navigateSearch = { query, queryType -> navController.navigateToSearch(query = query, queryType = queryType, navOptions = navOptions) },
            navigateOption = { optionType ->
                when(optionType){
                    OptionType.Edit -> navController.navigateToEditProfileRoute(navOptions = navOptions)
                    OptionType.Auth -> navController.navigateToLogin(navOptions = navOptions)
                    OptionType.Orders -> navController.navigateToOrders(navOptions = navOptions)
                    OptionType.Favourite -> navController.navigateToFavouritesRoute(navOptions = navOptions)
                    else -> {}
            }},
            navigateProduct = { productId ->
                navController.navigateToProduct(productId = productId, navOptions = navOptions)
            },
            onBackPressed = onBackPressed
        )
    }
}