package com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.navigation

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.ui.ProductHomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.ui.ProductHomeScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation.searchRoute


const val homeProductRoute = "home product"
const val searchProductRoute = "home search product"


fun NavController.navigateToProductHome(navOptions: NavOptions? = null) {
    val route = if (this.currentDestination?.route.toString() == searchRoute) searchProductRoute else homeProductRoute
    this.navigate(route = route, navOptions)
}

fun NavGraphBuilder.productHomeScreen() {
    composable(route = homeProductRoute) {
        val viewModel = hiltViewModel<ProductHomeViewModel>()
        ProductHomeScreen(productId = "productId", viewModel = viewModel)
    }

    composable(route = searchProductRoute) {
        val viewModel = hiltViewModel<ProductHomeViewModel>()
        Log.d("LATEINIT_TEST", "productScreen: ")

        ProductHomeScreen(productId = viewModel.productId.value, viewModel = viewModel)
    }
}