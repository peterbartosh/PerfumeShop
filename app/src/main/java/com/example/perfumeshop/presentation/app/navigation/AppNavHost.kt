package com.example.perfumeshop.presentation.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.presentation.app.navigation.nested.authGraph
import com.example.perfumeshop.presentation.app.navigation.nested.mainGraph
import com.example.perfumeshop.presentation.app.navigation.nested.startGraph
import com.example.perfumeshop.presentation.features.app_blocked.appBlockedScreen
import com.example.perfumeshop.presentation.features.cart.navigateToCart
import com.example.perfumeshop.presentation.features.code_verification.navigateToCodeVerification
import com.example.perfumeshop.presentation.features.edit_profile.navigateToEditProfileRoute
import com.example.perfumeshop.presentation.features.favourite.navigateToFavouritesRoute
import com.example.perfumeshop.presentation.features.first_ask.navigateToAsk
import com.example.perfumeshop.presentation.features.home.navigateToHome
import com.example.perfumeshop.presentation.features.login.navigateToLogin
import com.example.perfumeshop.presentation.features.order_making.navigateToOrderMaking
import com.example.perfumeshop.presentation.features.orders.navigateToOrders
import com.example.perfumeshop.presentation.features.search.navigateToSearch
import com.example.perfumeshop.presentation.features.splash.splashRoute


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    loadData: () -> Unit,
    onUserPreferencesChanged: (UserPreferencesType, Int) -> Unit,
    onBackPressed : () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = splashRoute,
        modifier = modifier
    ){

        val startDest = this.build().startDestinationId

        val navOptions = navOptions {
            popUpTo(navController.currentDestination?.id ?: startDest) {
                saveState = true
                inclusive = true
            }
            launchSingleTop = true
            restoreState = true
        }

        appBlockedScreen(
            navigateAsk = {
                navController.navigateToAsk(navOptions = navOptions)
            },
            navigateHome = {
                loadData()
                navController.navigateToHome(navOptions = null)
            },
            onBackPressed = onBackPressed
        )

        startGraph(
            navigateAsk = {
                navController.navigateToAsk(navOptions = navOptions)
            },
            navigateHome = {
               loadData()
               navController.navigateToHome(navOptions = null)
            },
            navigateAuth = { navController.navigateToLogin(navOptions = navOptions) }
        )

        authGraph(
            navController = navController,
            navigateCodeVerification = { navController.navigateToCodeVerification(navOptions = navOptions) },
            navigateHome = {
                loadData()
                navController.navigateToHome(navOptions = null)
            },
            onBackPressed = onBackPressed
        )

        mainGraph(
            onUserPreferencesChanged = onUserPreferencesChanged,
            navigateToCart = { navController.navigateToCart(navOptions = navOptions) },
            navigateToOrderMaking = { navController.navigateToOrderMaking(navOptions = navOptions) },
            navigateToSearch = { query, queryType -> navController.navigateToSearch(query = query, queryType = queryType, navOptions = navOptions) },
            navigateToOption = { optionType ->
                when(optionType){
                    OptionType.Edit -> navController.navigateToEditProfileRoute(navOptions = navOptions)
                    OptionType.Auth -> navController.navigateToLogin(navOptions = navOptions)
                    OptionType.Orders -> navController.navigateToOrders(navOptions = navOptions)
                    OptionType.Favourite -> navController.navigateToFavouritesRoute(navOptions = navOptions)
                    else -> {}
            }},
            onBackPressed = onBackPressed
        )
    }
}
