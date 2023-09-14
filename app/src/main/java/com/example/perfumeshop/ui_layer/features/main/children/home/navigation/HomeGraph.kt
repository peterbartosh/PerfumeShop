package com.example.perfumeshop.ui_layer.features.main.children.home.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.searchScreen
import com.example.perfumeshop.ui_layer.features.main.children.product.navigation.productScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel

var homeActiveChild by mutableStateOf(homeRoute)

fun NavGraphBuilder.homeGraph(
    onSearchClick: (String, QueryType) -> Unit,
    onProductClick: (String) -> Unit,
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
){
        homeScreen(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onSearchClick = onSearchClick,
            homeViewModel = homeViewModel,
                   onProductClick = onProductClick)

        searchScreen(cartViewModel = cartViewModel,
                     favouriteViewModel = favouriteViewModel,
                     onProductClick = onProductClick)

        productScreen(cartViewModel = cartViewModel,
                      favouriteViewModel = favouriteViewModel,
                      onClick = {})

}