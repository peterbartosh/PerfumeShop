package com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.ui.ProductProfileViewModel


const val favouriteRoute = "profile favourites"


fun NavController.navigateToFavouritesRoute(navOptions: NavOptions? = null) {
    this.navigate(favouriteRoute, navOptions)
}

fun NavGraphBuilder.favouriteScreen(onProductClick : () -> Unit) {
    composable(route = favouriteRoute) {
        val viewModel = hiltViewModel<FavouriteViewModel>()
        val productProfileViewModel = hiltViewModel<ProductProfileViewModel>()
        FavouriteScreen(viewModel = viewModel, onProductClick = { productId ->
            productProfileViewModel.updateProductId(productId)
            onProductClick.invoke()
        })
    }
}