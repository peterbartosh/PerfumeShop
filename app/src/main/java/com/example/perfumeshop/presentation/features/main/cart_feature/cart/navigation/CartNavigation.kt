package com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartScreen
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel

const val cartRoute = "cart"

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    cartActiveChild = cartRoute
    this.navigate(cartRoute, navOptions)
}

fun NavGraphBuilder.cartScreen(
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onOrderMakeClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = cartRoute) {
        BackPressHandler(onBackPressed = onBackPressed)
        CartScreen(
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            onProductClick = onProductClick,
            onOrderMakeClick = onOrderMakeClick
        )
    }
}