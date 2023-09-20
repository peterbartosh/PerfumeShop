package com.example.perfumeshop.ui_layer.features.main.home_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.navigation.homeScreen
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.navigation.searchScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel

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

//        productScreen(cartViewModel = cartViewModel,
//                      favouriteViewModel = favouriteViewModel,
//                      onClick = {})

}