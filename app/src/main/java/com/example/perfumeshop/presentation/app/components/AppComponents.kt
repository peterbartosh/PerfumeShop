package com.example.perfumeshop.presentation.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.app.navigation.nested.cartActiveChild
import com.example.perfumeshop.presentation.app.navigation.nested.getActiveChild
import com.example.perfumeshop.presentation.app.navigation.nested.homeActiveChild
import com.example.perfumeshop.presentation.app.navigation.nested.profileActiveChild
import com.example.perfumeshop.presentation.features.cart.cartRoute
import com.example.perfumeshop.presentation.features.code_verification.codeVerificationRoute
import com.example.perfumeshop.presentation.features.edit_profile.editProfileRoute
import com.example.perfumeshop.presentation.features.favourite.favouriteRoute
import com.example.perfumeshop.presentation.features.home.homeRoute
import com.example.perfumeshop.presentation.features.login.loginParentRoute
import com.example.perfumeshop.presentation.features.login.loginRoute
import com.example.perfumeshop.presentation.features.order_making.orderMakingRoute
import com.example.perfumeshop.presentation.features.orders.ordersRoute
import com.example.perfumeshop.presentation.features.profile.profileRoute
import com.example.perfumeshop.presentation.features.search.searchRoute
import com.example.perfumeshop.presentation.features.settings.settingsRoute
import com.example.perfumeshop.presentation.theme.Gold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    showBackIcon : Boolean = true,
    showSettingsIcon : Boolean = true,
    onSettingsClick : () -> Unit = {},
    onBackArrowClick : () -> Unit = {},
    navigateToHome : () -> Unit = {}
){

    val context = LocalContext.current

    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(50.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ){
                Text(text = context.getString(R.string.app_label), style = MaterialTheme.typography.titleLarge)
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

data class MainItem(
    val selectedIcon: ImageVector,
    val notSelectedIcon : ImageVector,
    val parentRoute : String,
    val label : String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    bottomBarSelectedIndex : Int,
    navController : NavHostController,
    cartProductsAmount: Int
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

    val wp = LocalContext.current.getWidthPercent()

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
                    BadgedBox(
                        badge = {
                            if (index == 1 && cartProductsAmount != 0)
                                Badge {
                                    Text(
                                        text = cartProductsAmount.toString(),
                                        textAlign = TextAlign.Center,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Cursive
                                    )
                                }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected.value)
                                item.selectedIcon
                            else
                                item.notSelectedIcon,
                            contentDescription = null,
                            tint = if (isSelected.value)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }
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
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        when(item.parentRoute){
                            homeRoute -> { homeActiveChild = homeRoute }
                            cartRoute -> { cartActiveChild =
                                cartRoute
                            }
                            profileRoute -> { profileActiveChild = profileRoute }
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavItem(
    ind: Int = 1, isCartItem: Boolean = true, amount: Int = 5,
    isSelected: Boolean = true, item: MainItem = MainItem(selectedIcon = Icons.Filled.ShoppingCart,
                                                          notSelectedIcon = Icons.Outlined.ShoppingCart,
                                                          parentRoute = cartRoute,
                                                          label = "Корзина"), onClick: (Int) -> Unit = {}
) {

    val context = LocalContext.current

    val wp = context.getWidthPercent()

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(wp * 33)
            .clickable { onClick(ind) },
        contentAlignment = Alignment.Center
    ){
        Column(
           modifier = Modifier.fillMaxSize(),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isCartItem)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = amount.toString(),
                        modifier = Modifier
                            .size(25.dp)
                            .background(Color.Red)
                            .clip(CircleShape),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }

            Icon(
                imageVector = if (isSelected)
                    item.selectedIcon
                else
                    item.notSelectedIcon,
                contentDescription = null,
                tint = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = item.label,
                modifier = Modifier.padding(1.dp),
                style = MaterialTheme.typography.bodySmall
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
        orderMakingRoute -> cartRoute.also { cartActiveChild =
            cartRoute
        }
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
