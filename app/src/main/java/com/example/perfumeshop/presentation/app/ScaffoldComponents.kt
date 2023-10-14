package com.example.perfumeshop.presentation.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.features.auth.code_verification_feature.navigation.codeVerificationRoute
import com.example.perfumeshop.presentation.features.auth.login_register_feature.navigation.loginParentRoute
import com.example.perfumeshop.presentation.features.auth.login_register_feature.navigation.loginRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.navigation.orderMakingRoute
import com.example.perfumeshop.presentation.features.main.getActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.searchRoute
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.productCartRoute
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.productHomeRoute
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.productFavouriteRoute
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.productOrdersRoute
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.productSearchRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.navigation.editProfileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation.favouriteRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.orders.navigation.ordersRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profileActiveChild
import com.example.perfumeshop.presentation.features.main.settings_feature.navigation.settingsRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    showBackIcon : Boolean,
    showSettingsIcon : Boolean,
    onSettingsClick : () -> Unit,
    onBackArrowClick : () -> Unit,
    navigateToHome : () -> Unit
){

    //BackHandler(enabled = false, onBack = onBackArrowClick)



    CenterAlignedTopAppBar(
        modifier = Modifier
            //.height(50.dp)
            .padding(bottom = 5.dp)
            .shadow(
                elevation = 10.dp,
                //  shape = shape
            )
            .background(
                color = Color.White,
                //shape = shape
            ),
        title = {
            Column(modifier = Modifier.fillMaxHeight(),
                   verticalArrangement = Arrangement.Center){
                Text(text = "GOLD PARFUM", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        },
        navigationIcon = {
            if (showBackIcon)
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 5.dp),
                       verticalArrangement = Arrangement.Center){
                    Icon(imageVector = Icons.Default.ArrowBack,
                         contentDescription = "arrow back",
                         tint = Color.Black,
                         modifier = Modifier
                             .size(30.dp)
                             .clickable { onBackArrowClick() })
                }
        },
        actions = {
            if (showSettingsIcon)
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 5.dp),
                       verticalArrangement = Arrangement.Center) {
                    Icon(imageVector = Icons.Default.Settings,
                         contentDescription = "settings icon",
                         tint = Color.Black,
                         modifier = Modifier
                             .size(30.dp)
                             .clickable { onSettingsClick() })
                }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = if (isSystemInDarkTheme()) Color.LightGray else Color.White
        ))

//    MediumTopAppBar(
//        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor =
//        if (isSystemInDarkTheme()) Color.LightGray else Color.White ),
//        modifier = Modifier
//            .height(50.dp)
//            .padding(top = 5.dp)
//            .shadow(
//                elevation = 10.dp,
//                shape = shape
//            )
//            .background(color = Color.White, shape = shape),
//
//        title = {
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Start
//            ) {
//
//
//                if (showBackIconState.value) {
//                    Icon(imageVector = Icons.Default.ArrowBack,
//                         contentDescription = "arrow back",
//                         tint = Color.Black,
//                         modifier = Modifier.clickable { onBackArrowClick.invoke() })
//                }
//
//                Icon(
//                    imageVector = Icons.Default.Face,
//                    contentDescription = "logo icon",
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(12.dp))
//                        .scale(0.7f)
//                )
//
//                Text(text = "GOLD PARFUM", fontSize = 15.sp)
//
//                Spacer(modifier = Modifier.width(40.dp))
//
//                if (showSettingsIconState.value) {
//                    Icon(imageVector = Icons.Default.Settings,
//                         contentDescription = "settings icon",
//                         tint = Color.Black,
//                         modifier = Modifier.clickable { onSettingsClick.invoke() })
//                }
//
//
//            }
//        }
//    )
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
    navController : NavHostController){

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


    NavigationBar(modifier = Modifier
        //.height(60.dp)
        .shadow(elevation = 10.dp),
                  containerColor = Color.LightGray
    ) {

        items.forEachIndexed { index, item ->

            val isSelected = mutableStateOf(bottomBarSelectedIndex == index)


            NavigationBarItem(selected = isSelected.value,
                              modifier = Modifier
                                  .fillMaxHeight()
                                  .width(wp * 33),
                              onClick = {

                                  val parentRoute = when(currentDestination?.route){
                                      in listOf(searchRoute, productHomeRoute) -> homeRoute
                                      productSearchRoute -> searchRoute
                                      productCartRoute -> cartRoute
                                      orderMakingRoute -> cartRoute
                                      productFavouriteRoute -> favouriteRoute
                                      productOrdersRoute -> ordersRoute
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
                              },
                              label = { Text(text = item.label, modifier = Modifier.padding(1.dp)) },
                              icon = {
                                  Icon(
                                      imageVector = if (isSelected.value)
                                          item.selectedIcon
                                      else
                                          item.notSelectedIcon,
                                      contentDescription = "main icon ${index + 1}"
                                  )
                              }
            )
        }
    }
}



fun onBackArrowClick(navController: NavHostController) {

    val curDestRoute = navController.currentDestination?.route ?: navController.graph.findStartDestination().route

    if (curDestRoute == settingsRoute) {
        navController.popBackStack()
        return
    }

    val route = when(curDestRoute){
        in listOf(searchRoute, productHomeRoute) -> homeRoute.also { homeActiveChild = homeRoute }
        productSearchRoute -> searchRoute.also { homeActiveChild = searchRoute }
        productCartRoute -> cartRoute.also { cartActiveChild = cartRoute }
        orderMakingRoute -> cartRoute.also { cartActiveChild = cartRoute }
        productFavouriteRoute -> favouriteRoute.also { profileActiveChild = favouriteRoute }
        productOrdersRoute -> ordersRoute.also { profileActiveChild = ordersRoute }
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