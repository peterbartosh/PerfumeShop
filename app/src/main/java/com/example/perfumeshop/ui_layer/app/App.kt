package com.example.perfumeshop.ui_layer.app


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.perfumeshop.ui_layer.app.navigation.AppNavHost
import com.example.perfumeshop.ui_layer.features.auth.code_verification_feature.navigation.codeVerificationRoute
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.navigation.loginRoute
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.navigation.orderMakingRoute
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.navigation.searchRoute
import com.example.perfumeshop.ui_layer.features.main.product_feature.navigation.productCartRoute
import com.example.perfumeshop.ui_layer.features.main.product_feature.navigation.productHomeRoute
import com.example.perfumeshop.ui_layer.features.main.product_feature.navigation.productSearchRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.navigation.editProfileRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.navigation.favouriteRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.navigation.ordersRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profileActiveChild
import com.example.perfumeshop.ui_layer.features.main.settings_feature.navigation.navigateToSettings
import com.example.perfumeshop.ui_layer.features.main.settings_feature.navigation.settingsRoute
import com.example.perfumeshop.ui_layer.features.start.ask_feature.navigation.askRoute
import com.example.perfumeshop.ui_layer.features.start.splash_feature.navigation.splashRoute
import com.example.perfumeshop.ui_layer.theme.PerfumeShopTheme
import com.example.perfumeshop.ui_layer.theme.PreferencesManager
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context)
    }
    val defaultValue = isSystemInDarkTheme()

    val isDarkTheme = rememberSaveable {
        mutableStateOf(preferencesManager.getData(defaultValue = defaultValue))
    }

    PerfumeShopTheme(darkTheme = isDarkTheme.value) {

        val navController = rememberNavController()

        var showBackIcon by remember {
            mutableStateOf(false)
        }
        var showSettingsIcon by remember {
            mutableStateOf(false)
        }
        var showBottomBar by remember {
            mutableStateOf(false)
        }

        var bottomBarSelectedIndex by remember {
            mutableStateOf(0)
        }

        navController.addOnDestinationChangedListener{ controller, dest, args ->

            Log.d("YHHJD", "APP: ${FirebaseAuth.getInstance().currentUser?.displayName}")

            Log.d("ACTIVE_CHILD_TEST",
                  "Home child: ${homeActiveChild} Cart child: $cartActiveChild Profile child: $profileActiveChild"
            )

            Log.d("BQ_TEST", "${controller.backQueue.map { it.destination.route }}")
            Log.d("BQ_TEST_ID", "${controller.backQueue.map { it.destination.route + " " + it.id }}")

            showBackIcon = dest.route !in
                    listOf(splashRoute, askRoute, homeRoute, cartRoute, profileRoute)

            showSettingsIcon = dest.route in
                    listOf(homeRoute, cartRoute, profileRoute)

            showBottomBar = dest.route !in
                    listOf(
                        splashRoute, askRoute, loginRoute, codeVerificationRoute,
                        settingsRoute)

            bottomBarSelectedIndex = when(dest.route){
                in listOf(homeRoute, searchRoute, productHomeRoute, productSearchRoute) -> 0
                in listOf(cartRoute, productCartRoute, orderMakingRoute) -> 1
                in listOf(profileRoute, editProfileRoute, favouriteRoute, ordersRoute) -> 2
                else -> bottomBarSelectedIndex
            }
        }

        val cartViewModel = hiltViewModel<CartViewModel>()
        val favouriteViewModel = hiltViewModel<FavouriteViewModel>()
        val homeViewModel = hiltViewModel<HomeViewModel>()

        Scaffold(
            topBar = {
                MyTopAppBar(showBackIcon = showBackIcon,

                            showSettingsIcon = showSettingsIcon,

                            onSettingsClick =  navController::navigateToSettings,

                            onBackArrowClick = {
                                //finishViewModel(controller = navController)
                                onBackArrowClick(navController = navController)
                            }
                )
            },
            bottomBar = {
                if (showBottomBar)
                    BottomNavigationBar(bottomBarSelectedIndex = bottomBarSelectedIndex,
                                        navController = navController)
                 else
                    Box {}
            }
        ) { paddingValues ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {

                AppNavHost(
                    homeViewModel = homeViewModel,
                    cartViewModel = cartViewModel,
                    favouriteViewModel = favouriteViewModel,
                    navController = navController,
                    onThemeChange = { newTheme ->
                    if (isDarkTheme.value != newTheme) {
                        preferencesManager.saveData(newTheme)
                        isDarkTheme.value = newTheme
                    }
                })


            }
        }

    }
}