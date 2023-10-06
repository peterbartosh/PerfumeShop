package com.example.perfumeshop.ui_layer.features.main.home_feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui.HomeScreen
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel


const val homeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null){
    this.navigate(route = homeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    onSearchClick: (String, QueryType) -> Unit
) {
    composable(route = homeRoute) {
        HomeScreen(onSearchClick = onSearchClick)
    }
}