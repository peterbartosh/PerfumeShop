package com.example.perfumeshop.ui_layer.features.main.children.product.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.cart.navigation.cartActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.searchRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.product.ui.ProductScreen
import com.example.perfumeshop.ui_layer.features.main.children.product.ui.ProductViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileActiveChild


const val productHomeRoute = "home|product"
const val productSearchRoute = "search|product"
const val productCartRoute = "cart|product"
const val productProfileRoute = "profile|product"

const val productIdKey = "productIdKey"

fun NavController.navigateToProduct(productId : String, navOptions: NavOptions? = null) {
    val route = when (this.currentDestination?.route){
        homeRoute -> productHomeRoute.also { homeActiveChild = productHomeRoute }
        searchRoute -> productSearchRoute.also { homeActiveChild = productSearchRoute }
        cartRoute -> productCartRoute.also { cartActiveChild = productCartRoute }
        profileRoute -> productProfileRoute.also { profileActiveChild = productProfileRoute }
        else -> this.currentDestination?.route!!
    }
    this.navigate(route = route, navOptions = navOptions)
    //val vm = this.currentBackStackEntry?.viewModelStore?.get(key = "") as SearchViewModel
    this.currentBackStackEntry?.savedStateHandle?.set(key = productIdKey, value = productId)
}

fun NavGraphBuilder.productScreen(
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    composable(route = productHomeRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""
        ProductScreen(productId = productId,
                      cartViewModel = cartViewModel,
                      favouriteViewModel = favouriteViewModel,
                      productViewModel = productViewModel,
                      onClick = onClick)
    }

    composable(route = productSearchRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""
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
        ProductScreen(
            productId = productId,
            productViewModel = productViewModel,
            onClick = onClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
    }

    composable(route = productProfileRoute) { backStackEntry ->
        val productViewModel = hiltViewModel<ProductViewModel>()
        val productId = backStackEntry.savedStateHandle.get<String>(key = productIdKey) ?: ""
        ProductScreen(
            productId = productId,
            productViewModel = productViewModel,
            onClick = onClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
    }


}
