package com.example.perfumeshop.ui_layer.features.main.children.home.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.perfumeshop.data_layer.utils.homeGraphR
import com.example.perfumeshop.data_layer.utils.navigateToGraph
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation.productHomeScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.searchScreen

const val homeGraphRoute = "homeGraph"

var homeGraphStartDestination = mutableStateOf(homeRoute)

//fun NavController.navigateToHomeGraph(){
//    this.navigateToGraph(graphRoute = homeGraphRoute, saveStartDest = true) {
//
//    }
//}

fun NavGraphBuilder.homeGraph(
    onSearchClick: () -> Unit,
    onProductHomeClick: () -> Unit,
){
   // navigation(startDestination = homeGraphStartDestination.value, route = homeGraphRoute){


        homeScreen(onSearchClick = onSearchClick, onProductHomeClick =  onProductHomeClick )

        searchScreen(onProductHomeClick = onProductHomeClick)

        productHomeScreen()

   // }
}