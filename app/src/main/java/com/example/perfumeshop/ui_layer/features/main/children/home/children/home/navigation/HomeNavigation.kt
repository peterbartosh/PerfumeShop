package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui.HomeScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel


const val homeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null){
    this.navigate(route = homeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    onSearchClick: (String, QueryType) -> Unit,
    onProductClick: (String) -> Unit,
    homeViewModel : HomeViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    composable(route = homeRoute) {
        //val homeViewModel = hiltViewModel<HomeViewModel>()

        HomeScreen(homeViewModel = homeViewModel,
                   cartViewModel = cartViewModel,
                   favouriteViewModel = favouriteViewModel,
                   onSearchClick = onSearchClick,
                   onProductClick = onProductClick
        )
    }
}