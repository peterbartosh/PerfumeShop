package com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileActiveChild


const val favouriteRoute = "favourites"

fun NavController.navigateToFavouritesRoute(navOptions: NavOptions? = null) {
    profileActiveChild = favouriteRoute
    this.navigate(route = favouriteRoute, navOptions = navOptions)
}

fun NavGraphBuilder.favouriteScreen(onProductClick: (String) -> Unit, cartViewModel: CartViewModel) {
    composable(route = favouriteRoute) {
        val favouriteViewModel = hiltViewModel<FavouriteViewModel>()
        FavouriteScreen(cartViewModel = cartViewModel, favouriteViewModel = favouriteViewModel, onProductClick = onProductClick)
    }
}