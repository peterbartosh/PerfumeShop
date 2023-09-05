package com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation.ordersRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.ui.ProductProfileViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.ui.ProductProfileScreen


const val profileFavouriteProductRoute = "profile favourite product"
const val profileOrderProductRoute = "profile order product"


fun NavController.navigateToProductProfile(navOptions: NavOptions? = null) {
    val route = if (this.currentDestination?.route.toString() == ordersRoute) profileOrderProductRoute else profileFavouriteProductRoute
    this.navigate(route = route, navOptions = navOptions)
}

fun NavGraphBuilder.productProfileScreen(onClick : () -> Unit) {
    composable(route = profileFavouriteProductRoute) {
        val viewModel = hiltViewModel<ProductProfileViewModel>()
        ProductProfileScreen(productId = "productId", viewModel = viewModel, onClick = onClick)
    }

    composable(route = profileOrderProductRoute) {
        val viewModel = hiltViewModel<ProductProfileViewModel>()
        ProductProfileScreen(productId = "productId", viewModel = viewModel, onClick = onClick)
    }
}


