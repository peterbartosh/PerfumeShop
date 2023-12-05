package com.example.perfumeshop.presentation.app


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.perfumeshop.presentation.app.components.BottomNavigationBar
import com.example.perfumeshop.presentation.app.components.MyTopAppBar
import com.example.perfumeshop.presentation.app.components.onBackArrowClick
import com.example.perfumeshop.presentation.app.navigation.AppNavHost
import com.example.perfumeshop.presentation.features.app_blocked.appBlockedRoute
import com.example.perfumeshop.presentation.features.app_blocked.navigateToAppBlocked
import com.example.perfumeshop.presentation.features.cart.cartRoute
import com.example.perfumeshop.presentation.features.code_verification.codeVerificationRoute
import com.example.perfumeshop.presentation.features.edit_profile.editProfileRoute
import com.example.perfumeshop.presentation.features.favourite.favouriteRoute
import com.example.perfumeshop.presentation.features.first_ask.askRoute
import com.example.perfumeshop.presentation.features.home.homeRoute
import com.example.perfumeshop.presentation.features.home.navigateToHome
import com.example.perfumeshop.presentation.features.login.loginRoute
import com.example.perfumeshop.presentation.features.order_making.orderMakingRoute
import com.example.perfumeshop.presentation.features.orders.ordersRoute
import com.example.perfumeshop.presentation.features.profile.profileRoute
import com.example.perfumeshop.presentation.features.search.searchRoute
import com.example.perfumeshop.presentation.features.settings.navigateToSettings
import com.example.perfumeshop.presentation.features.settings.settingsRoute
import com.example.perfumeshop.presentation.features.splash.splashRoute
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
fun App(
    finishAffinity : () -> Unit,
    loadData: () -> Unit
) {

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
                    listOf(homeRoute,
                           cartRoute, profileRoute)

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

        val appViewModel = hiltViewModel<AppViewModel>()



        val cartProductsAmount = appViewModel.cartProductsAmount.collectAsState()

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
                        navController = navController,
                        cartProductsAmount = cartProductsAmount.value
                    )
                 else
                    Box {}
            }
        ) { paddingValues ->

            AppNavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                navController = navController,
                loadData = loadData,
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