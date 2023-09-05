package com.example.perfumeshop.ui_layer.features.main.children.profile.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.data_layer.utils.navigateToGraph
import com.example.perfumeshop.data_layer.utils.profileGraphR
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.navigation.editProfileScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.navigation.favouriteScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation.ordersScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.navigation.productProfileScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileScreen


const val profileGraphRoute = "profileGraph"

val profileGraphStartDestination = mutableStateOf(profileRoute)

//fun NavController.navigateToProfileGraph(){
//    this.navigateToGraph(graphRoute = profileGraphRoute, saveStartDest = true){}
//}

fun NavGraphBuilder.profileGraph(
    onOptionClick: (OptionType) -> Unit,
    onProductClick: () -> Unit
){
    //navigation(startDestination = profileGraphStartDestination.value, route = profileGraphRoute){


        profileScreen(onOptionClick = onOptionClick)

        editProfileScreen(onClick = {})

        favouriteScreen(onProductClick = onProductClick)

        ordersScreen(onProductClick = onProductClick)

        productProfileScreen(onClick = {})


   // }
}