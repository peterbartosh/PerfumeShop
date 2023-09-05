package com.example.perfumeshop.ui_layer.app


import androidx.compose.foundation.background
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
import androidx.navigation.compose.rememberNavController
import com.example.perfumeshop.ui.theme.PerfumeShopTheme
import com.example.perfumeshop.ui_layer.app.navigation.AppNavHost
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation.loginAskRoute
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.navigation.loginProfileRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.searchProductRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.settings.navigation.navigateToSettings
import com.example.perfumeshop.ui_layer.features.main.children.settings.navigation.settingsCartRoute
import com.example.perfumeshop.ui_layer.features.main.children.settings.navigation.settingsHomeRoute
import com.example.perfumeshop.ui_layer.features.main.navigation.mainGraphRoute
import com.example.perfumeshop.ui_layer.features.start.children.ask.navigation.askRoute
import com.example.perfumeshop.ui_layer.features.start.children.splash.navigation.splashRoute
import com.example.perfumeshop.ui_layer.theme_handler.PreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context)
    }
    val isDarkTheme = rememberSaveable {
        mutableStateOf(preferencesManager.getData(false))
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
            showBackIcon = dest.route?.split(' ')?.last() !in
                    listOf(splashRoute, askRoute, mainGraphRoute, homeRoute, cartRoute, profileRoute)

            showSettingsIcon = dest.route?.split(' ')?.last() in
                    listOf(mainGraphRoute, homeRoute, cartRoute, profileRoute)

            showBottomBar = dest.route?.split(' ')?.last() !in
                    listOf(splashRoute, askRoute, loginAskRoute, loginProfileRoute,
                           settingsHomeRoute, settingsCartRoute, searchProductRoute)

            bottomBarSelectedIndex = when(dest.route?.split(' ')?.first()){
                homeRoute -> 0
                cartRoute -> 1
                profileRoute -> 2
                else -> 0
            }
        }

        //val homeViewModel = hiltViewModel<HomeViewModel>()
//        val cartViewModel = hiltViewModel<CartViewModel>()                        // TODO TODO TODO
//
//
//        val productHomeViewModel = hiltViewModel<ProductHomeViewModel>()
//        val productSearchViewModel = hiltViewModel<ProductSearchViewModel>()
//        val productCartViewModel = hiltViewModel<ProductCartViewModel>()
//
//        val searchViewModel = hiltViewModel<SearchViewModel>()
//
//        val optionViewModel = hiltViewModel<OptionViewModel>()
//
//        val authViewModel =  hiltViewModel<AuthViewModel>()

//        fun finishViewModel(controller : NavHostController){
//            when(controller.currentDestination?.route?.split(' ')?.last()){
//                homeProductRoute -> productHomeViewModel.clear()
//                cartProductRoute -> productCartViewModel.clear()
//                searchProductRoute -> productSearchViewModel.clear()
//                searchRoute -> searchViewModel.clear()
//                optionRoute -> optionViewModel.clear()
//                loginRoute -> authViewModel.clear()
//            }
//        }

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
                if (showBottomBar) {
                    BottomNavigationBar(bottomBarSelectedIndex = bottomBarSelectedIndex) { parentRoute ->
                        onBottomBarButtonClick(
                            navController = navController,
                            parentRoute = parentRoute
                        )
                    }
                } else {
                    Box {}
                }
            }
        ) { paddingValues ->

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {

                AppNavHost(
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


//@Composable
//fun navhost() {
//
//                NavHost(
//                    navController = navController,
//                    startDestination = startRoute
//                ) {
//
//                    navigation(startDestination = splashRoute, route = startRoute) {
//
//                        splashScreen(
//                            navigateAsk = navController::navigateToAsk,
//                            navigateMain = navController::navigateToMain
//                        )
//                    }
//
//                   // navigation(startDestination = askRoute, route = optionalRoute) {
//                        askScreen(
//                            navigateAuth = {
//                                //navComponents.manageStates(showBackIcon = true, showSettingsIcon = false, showBottomBar = false)
//
//                                navController.navigateToLogin(parentRoute = askRoute)
//                                           },
//                            onSkipClick = navController::navigateToMain
//                        )
//                   // }
//
//                    //navigation(startDestination = loginRoute, route = authRoute) {
//                        loginScreen(viewModel = authViewModel, onCodeSent = {
//                            //navComponents.manageStates(showBackIcon = false,
//                            //                           showSettingsIcon = false, showBottomBar = false)
//                            navController.navigateToCodeVerification(
//                                parentRoute = navController.currentDestination?.route.toString())
//                        }, onSuccess = {
//                            navController.navigateToMain(navOptions = null)
//                        })
//
//                    codeVerificationScreen(viewModel = authViewModel, onSuccess = navController::navigateToMain)
//                   // }
//
//
//                    navigation(startDestination = homeRoute, route = mainRoute) {
//
//                        homeScreen(
//                            onSearchClick = { query, queryType ->
//
//                                       navController.navigateToSearch(query = query, queryType = queryType)
//                                   },
//                            onProductHomeClick = { productId ->
//                                       com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.navigateToProduct(
//                                           route = homeProductRoute
//                                       )
//                                   }
//                        )
//
//                        cartScreen(viewModel = cartViewModel) { productId ->
//                            com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.navigateToProduct(
//                                route = cartProductRoute
//                            )
//                        }
//
//                        profileScreen { optionType ->
//                            navController.navigateToOption(optionType = optionType)
//                        }
//
//
//                        settingsScreen(onClick = {}, onThemeChange = { newTheme ->
//                                if (isDarkTheme.value != newTheme) {
//                                    preferencesManager.saveData(newTheme)
//                                    isDarkTheme.value = newTheme
//                                }
//                            }
//                        )
//
//                        // home children
//
//                        searchScreen(viewModel = searchViewModel) { productId ->
//                            com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.navigateToProduct(
//                                route = searchProductRoute
//                            )
//                        }
//
//                        // home children
//
//                        com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.productHomeScreen(
//                            viewModel = productHomeViewModel,
//                            onClick = {})
//
//                        com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.productSearchScreen(
//                            viewModel = productSearchViewModel,
//                            onClick = {})
//
//                        com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.productCartScreen(
//                            viewModel = productCartViewModel,
//                            onClick = {})
//
//                        optionScreen(viewModel = optionViewModel) {
//                            navController.navigateToLogin(parentRoute = optionRoute)
//                        }
//
//
//                    }
//
//                }
//}
