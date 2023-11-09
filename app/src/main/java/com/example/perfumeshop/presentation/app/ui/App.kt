package com.example.perfumeshop.presentation.app.ui


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.user_preferences.PreferencesManager
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.presentation.app.navigation.AppNavHost
import com.example.perfumeshop.presentation.features.app_blocked.navigation.appBlockedRoute
import com.example.perfumeshop.presentation.features.app_blocked.navigation.navigateToAppBlocked
import com.example.perfumeshop.presentation.features.auth.code_verification_feature.navigation.codeVerificationRoute
import com.example.perfumeshop.presentation.features.auth.login_feature.navigation.loginRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.navigation.orderMakingRoute
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.navigateToHome
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.searchRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.navigation.editProfileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation.favouriteRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.orders.navigation.ordersRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.presentation.features.main.settings_feature.navigation.navigateToSettings
import com.example.perfumeshop.presentation.features.main.settings_feature.navigation.settingsRoute
import com.example.perfumeshop.presentation.features.start.ask_feature.navigation.askRoute
import com.example.perfumeshop.presentation.features.start.splash_feature.navigation.splashRoute
import com.example.perfumeshop.presentation.theme.PerfumeShopTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val TAG = "App_Tag"
const val ERROR_TAG = "ERROR_OCCURRED"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(finishAffinity : () -> Unit) {

    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context)
    }

    val isDarkTheme = remember {
        mutableStateOf(preferencesManager.getThemeData(defaultValue = 0))
    }

    val fontSize = remember {
        mutableStateOf(preferencesManager.getFontSizeData(defaultValue = 0))
    }

    PerfumeShopTheme(darkTheme = isDarkTheme.value == 0, fontSize = fontSize.value) {

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

            Log.d("BACK_Q_TEST", "${controller.backQueue.map { it.destination.route }}")

            if (FirebaseAuth.getInstance().currentUser != null)
                CoroutineScope(Job() + Dispatchers.Main).launch {
                    FireRepository.isAppBlocked()?.let { isBlocked ->
                        if (isBlocked && dest.route != appBlockedRoute) navController.navigateToAppBlocked(null)
                    }
                }

            showBackIcon = dest.route !in
                    listOf(
                        splashRoute,
                        askRoute,
                        homeRoute,
                        cartRoute,
                        profileRoute,
                        appBlockedRoute
                    )

            showSettingsIcon = dest.route in
                    listOf(homeRoute, cartRoute, profileRoute)

            showBottomBar = dest.route !in
                    listOf(
                        splashRoute,
                        askRoute,
                        loginRoute,
                        codeVerificationRoute,
                        settingsRoute,
                        appBlockedRoute
                    )

                bottomBarSelectedIndex = when (dest.route) {
                    in listOf(
                        homeRoute, searchRoute,
                        //productHomeRoute, productSearchRoute
                    ) -> 0

                    in listOf(
                        cartRoute,
                        //productCartRoute,
                        orderMakingRoute
                    ) -> 1

                    in listOf(profileRoute, editProfileRoute, favouriteRoute, ordersRoute) -> 2
                    else -> bottomBarSelectedIndex
                }
        }

        val cartViewModel = hiltViewModel<CartViewModel>()
        val favouriteViewModel = hiltViewModel<FavouriteViewModel>()

        Scaffold(
            topBar = {
                MyTopAppBar(
                    showBackIcon = showBackIcon,
                    showSettingsIcon = showSettingsIcon,
                    onSettingsClick =  navController::navigateToSettings,
                    onBackArrowClick = { onBackArrowClick(navController = navController) },
                    navigateToHome = navController::navigateToHome
                )
            },
            bottomBar = {
                if (showBottomBar)
                    BottomNavigationBar(
                        bottomBarSelectedIndex = bottomBarSelectedIndex,
                        navController = navController
                    )
                 else
                    Box {}
            }
        ) { paddingValues ->

            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                cartViewModel = cartViewModel,
                favouriteViewModel = favouriteViewModel,
                navController = navController,
                onBackPressed = {
                    if (showBackIcon)
                        onBackArrowClick(navController)
                    else
                        finishAffinity()
                },
                onUserPreferencesChanged = { ipt, value ->
                    when (ipt){
                        UserPreferencesType.Theme -> {
                            isDarkTheme.value = value
                        }
                        UserPreferencesType.FontSize -> {
                            fontSize.value = value
                        }
                    }
                }
            )
        }
    }
}