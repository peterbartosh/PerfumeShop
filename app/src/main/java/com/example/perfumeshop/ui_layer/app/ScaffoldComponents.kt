package com.example.perfumeshop.ui_layer.app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.perfumeshop.data_layer.utils.getWidthPercent
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    showBackIcon : Boolean,
    showSettingsIcon : Boolean,
    onSettingsClick : () -> Unit,
    onBackArrowClick : () -> Unit
){
    val shape = RoundedCornerShape(30.dp)
    CenterAlignedTopAppBar(
        modifier = Modifier
            //.height(50.dp)
            .padding(top = 5.dp)
            .shadow(
                elevation = 10.dp,
                shape = shape
            )
            .background(color = Color.White, shape = shape),
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
                             .clickable { onBackArrowClick.invoke() })
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
                             .clickable { onSettingsClick.invoke() })
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

private data class MainItem(val selectedIcon: ImageVector, val notSelectedIcon : ImageVector,
                    val parentRoute : String,
                    val label : String)

@Composable
fun BottomNavigationBar(
    bottomBarSelectedIndex : Int,
    onButtonClick : (String) -> Unit = {}){

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


    NavigationBar(modifier = Modifier
        //.height(60.dp)
        .shadow(elevation = 10.dp)
        .background(color = Color.White)) {

        items.forEachIndexed { index, item ->

            val isSelected = mutableStateOf(bottomBarSelectedIndex == index)


            NavigationBarItem(selected = isSelected.value,
                              modifier = Modifier
                                  .fillMaxHeight()
                                  .width(wp * 33),
                              onClick = {
                                  onButtonClick(item.parentRoute)
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

        // clear view model todo

        val parentsList = navController.currentDestination?.route
            ?.split(' ')!!.toMutableList()

        val parentRoute = parentsList.filterIndexed{ i, s ->
            i + 1 != parentsList.size
        }.joinToString(separator = " ")

//        val lastEntry = navController.backQueue.findLast { nbse ->
//            nbse.destination.route == parentRoute &&
//                    nbse.destination.route != navController.currentDestination?.route
//        }

//        val lastRoute =
//            if (lastEntry == null) parentRoute else lastEntry.destination.route!!

        navController.popBackStack()
        navController.navigate(parentRoute)

        Log.d("BQ_BQ_BQ_TEST", "onBackArrowClick: ${navController.backQueue.map { it.destination.route }}")


    }


fun onBottomBarButtonClick(navController: NavHostController,
                           parentRoute : String){
//
//
//    Log.d("TEST_NAV_TEST", currentNestedGraph?.route.toString() + " " +
//            currentNestedGraph?.startDestinationRoute.toString())

//
//    when (parentRoute){
//        homeRoute -> navController.navigateToHomeGraph()
//        cartRoute -> navController.navigateToCartGraph()
//        profileRoute -> navController.navigateToProfileGraph()
//    }

//    Log.d("TEST_NAV_TEST", currentNestedGraph?.route.toString() + " " +
//            currentNestedGraph?.startDestinationRoute.toString())


        if (navController.currentDestination?.route?.startsWith(parentRoute) == true) {
            navController.navigate(parentRoute)
            return
        }

        val lastEntry = navController.backQueue.findLast { nbse ->
            nbse.destination.route?.startsWith(parentRoute) == true
        }

        val lastRoute =
            if (lastEntry == null) parentRoute else lastEntry.destination.route!!



        navController.navigate(lastRoute)

        Log.d("BQ_BQ_BQ_TEST", "onBackArrowClick: ${navController.backQueue.map { it.destination.route }}")


        val showBackIcon = navController.currentDestination?.route !in listOf(
            homeRoute, cartRoute, profileRoute
        )

        val showSettingsIcon = navController.currentDestination?.route in listOf(
            homeRoute, cartRoute, profileRoute
        )

}