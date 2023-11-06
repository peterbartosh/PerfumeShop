package com.example.perfumeshop.presentation.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.features.auth.code_verification_feature.navigation.codeVerificationRoute
import com.example.perfumeshop.presentation.features.auth.login_feature.navigation.loginParentRoute
import com.example.perfumeshop.presentation.features.auth.login_feature.navigation.loginRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.navigation.orderMakingRoute
import com.example.perfumeshop.presentation.features.main.getActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.searchRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.navigation.editProfileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation.favouriteRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.orders.navigation.ordersRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profileActiveChild
import com.example.perfumeshop.presentation.features.main.settings_feature.navigation.settingsRoute
import com.example.perfumeshop.presentation.theme.Gold

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    showBackIcon : Boolean = true,
    showSettingsIcon : Boolean = true,
    onSettingsClick : () -> Unit = {},
    onBackArrowClick : () -> Unit = {},
    navigateToHome : () -> Unit = {}
){

    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(50.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ){
                Text(text = "GOLD PARFUM", style = MaterialTheme.typography.titleLarge)
            }
        },
        navigationIcon = {
            if (showBackIcon)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 5.dp),
                    verticalArrangement = Arrangement.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.back_arrow),
                         contentDescription = "arrow back",
                         modifier = Modifier
                             .shadow(elevation = 0.dp, shape = CircleShape)
                             .size(25.dp)
                             .clip(CircleShape)
                             .clickable { onBackArrowClick() }
                    )
                }
        },
        actions = {
            if (showSettingsIcon)
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 5.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "settings icon",
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .clickable { onSettingsClick() }
                    )
                }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = Gold,
            actionIconContentColor = Gold,
            navigationIconContentColor = Gold,
        )
    )
}

private data class MainItem(
    val selectedIcon: ImageVector,
    val notSelectedIcon : ImageVector,
    val parentRoute : String,
    val label : String
)

@Composable
fun BottomNavigationBar(
    bottomBarSelectedIndex : Int,
    navController : NavHostController
){

    val items = listOf(
        MainItem(selectedIcon = Icons.Filled.Home,
                 notSelectedIcon = Icons.Outlined.Home,
                 parentRoute = homeRoute,
                 label = "Главная"),
        MainItem(selectedIcon = Icons.Filled.ShoppingCart,
                 notSelectedIcon = Icons.Outlined.ShoppingCart,
                 parentRoute = cartRoute,
                 label = "Корзина"),
        MainItem(selectedIcon = Icons.Filled.Person,
                 notSelectedIcon = Icons.Outlined.Person,
                 parentRoute = profileRoute,
                 label = "Профиль")
    )

    val wp = getWidthPercent(LocalContext.current)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        items.forEachIndexed { index, item ->

            val isSelected = mutableStateOf(bottomBarSelectedIndex == index)

            NavigationBarItem(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(wp * 33),
                selected = isSelected.value,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                ),
                label = {
                    Text(
                        text = item.label,
                        modifier = Modifier.padding(1.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected.value)
                            item.selectedIcon
                        else
                            item.notSelectedIcon,
                        contentDescription = null
                    )
                },
                onClick = {
                    val parentRoute = when(currentDestination?.route){
                        searchRoute -> homeRoute
//                        productSearchRoute -> searchRoute
//                        productCartRoute -> cartRoute
                        orderMakingRoute -> cartRoute
//                        productFavouriteRoute -> favouriteRoute
//                        productOrdersRoute -> ordersRoute
                        in listOf(editProfileRoute, favouriteRoute, ordersRoute) -> profileRoute
                        loginRoute -> loginParentRoute
                        codeVerificationRoute -> loginRoute
                        else -> currentDestination?.route ?: homeRoute
                    }

                    if (item.parentRoute != parentRoute) {
                        val route = getActiveChild(item.parentRoute)

                        navController.navigate(route = route){
                            popUpTo(currentDestination?.id ?: navController.graph.findStartDestination().id){
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                    } else {
                        navController.navigate(item.parentRoute){
                            popUpTo(currentDestination?.id!!){
                                //saveState = false
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        when(item.parentRoute){
                            homeRoute -> { homeActiveChild = homeRoute}
                            cartRoute -> { cartActiveChild = cartRoute}
                            profileRoute -> { profileActiveChild = profileRoute}
                        }
                    }
                }
            )
        }
    }
}



fun onBackArrowClick(navController: NavHostController) {

    val curDestRoute = navController.currentDestination?.route ?: navController.graph.findStartDestination().route

    if (curDestRoute in listOf(settingsRoute, codeVerificationRoute)) {
        navController.popBackStack()
        return
    }

    val route = when(curDestRoute){
        searchRoute -> homeRoute.also { homeActiveChild = homeRoute }
        //productSearchRoute -> searchRoute.also { homeActiveChild = searchRoute }
        //productCartRoute -> cartRoute.also { cartActiveChild = cartRoute }
        orderMakingRoute -> cartRoute.also { cartActiveChild = cartRoute }
        //productFavouriteRoute -> favouriteRoute.also { profileActiveChild = favouriteRoute }
        //productOrdersRoute -> ordersRoute.also { profileActiveChild = ordersRoute }
        in listOf(editProfileRoute, favouriteRoute, ordersRoute) -> profileRoute.also { profileActiveChild = profileRoute }
        loginRoute -> loginParentRoute
        codeVerificationRoute -> loginRoute
        else -> curDestRoute ?: homeRoute
    }

    navController.navigate(route = route){
        popUpTo(navController.currentDestination?.id ?:
        navController.graph.findStartDestination().id){
            //saveState = true
            inclusive = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
