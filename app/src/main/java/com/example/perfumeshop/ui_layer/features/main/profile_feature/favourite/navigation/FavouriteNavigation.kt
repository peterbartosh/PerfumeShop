package com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profileActiveChild


const val favouriteRoute = "favourites"

fun NavController.navigateToFavouritesRoute(navOptions: NavOptions? = null) {
    profileActiveChild = favouriteRoute
    this.navigate(route = favouriteRoute, navOptions = navOptions)
}

fun NavGraphBuilder.favouriteScreen(onProductClick: (String) -> Unit,
                                    favouriteViewModel : FavouriteViewModel,
                                    cartViewModel: CartViewModel
) {
    composable(route = favouriteRoute) {
        favouriteViewModel.isFailure = false
        favouriteViewModel.isSuccess = false
        favouriteViewModel.isLoading = false
        FavouriteScreen(cartViewModel = cartViewModel, favouriteViewModel = favouriteViewModel, onProductClick = onProductClick)
    }
}