package com.example.perfumeshop.presentation.features.main.home_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeScreen
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.searchScreen
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel

var homeActiveChild by mutableStateOf(homeRoute)

fun NavGraphBuilder.homeGraph(
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onSearchClick: (String, QueryType) -> Unit,
    onProductClick: (String) -> Unit,
    onBackPressed : () -> Unit
){
        homeScreen(
            onSearchClick = onSearchClick,
            onBackPressed = onBackPressed
        )

        searchScreen(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onProductClick = onProductClick,
            onBackPressed = onBackPressed
        )

}