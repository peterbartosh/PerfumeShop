package com.example.perfumeshop.presentation.features.main.product_feature.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.searchRoute
import com.example.perfumeshop.presentation.features.main.product_feature.ui.ProductScreen
import com.example.perfumeshop.presentation.features.main.product_feature.ui.ProductViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation.favouriteRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.orders.navigation.ordersRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profileActiveChild


const val productHomeRoute = "home|product"
const val productSearchRoute = "search|product"
const val productCartRoute = "cart|product"
const val productFavouriteRoute = "favourite|product"
const val productOrdersRoute = "orders|product"

const val productIdKey = "productIdKey"

fun NavController.navigateToProduct(productId : String, navOptions: NavOptions? = null) {
    val route = when (this.currentDestination?.route){
        homeRoute -> productHomeRoute.also { homeActiveChild = productHomeRoute }
        searchRoute -> productSearchRoute.also { homeActiveChild = productSearchRoute }
        cartRoute -> productCartRoute.also { cartActiveChild = productCartRoute }
        favouriteRoute -> productFavouriteRoute.also { profileActiveChild = productFavouriteRoute }
        ordersRoute -> productOrdersRoute.also { profileActiveChild = productOrdersRoute }
        else -> this.currentDestination?.route!!
    }
    this.navigate(route = route, navOptions = navOptions)

    this.currentBackStackEntry?.savedStateHandle?.set(key = productIdKey, value = productId)
}

fun NavGraphBuilder.productScreen(
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onBackPressed: () -> Unit
) {

    composable(route = productHomeRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""

        BackPressHandler(onBackPressed = onBackPressed)
        ProductScreen(productId = productId,
                      cartViewModel = cartViewModel,
                      favouriteViewModel = favouriteViewModel,
                      productViewModel = productViewModel,
                      onClick = onClick)
    }

    composable(route = productSearchRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""

        BackPressHandler(onBackPressed = onBackPressed)
        ProductScreen(
            productId = productId,
            productViewModel = productViewModel,
            onClick = onClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
    }

    composable(route = productCartRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""

        BackPressHandler(onBackPressed = onBackPressed)
        ProductScreen(
            productId = productId,
            productViewModel = productViewModel,
            onClick = onClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
    }

    composable(route = productFavouriteRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""

        BackPressHandler(onBackPressed = onBackPressed)
        ProductScreen(
            productId = productId,
            productViewModel = productViewModel,
            onClick = onClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
    }

    composable(route = productOrdersRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""

        BackPressHandler(onBackPressed = onBackPressed)
        ProductScreen(
            productId = productId,
            productViewModel = productViewModel,
            onClick = onClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
    }

}
