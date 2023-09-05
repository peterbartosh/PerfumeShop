package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui.HomeScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.ui.ProductHomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchViewModel


const val homeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(homeRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onSearchClick: () -> Unit,
    onProductHomeClick: () -> Unit
) {
    composable(route = homeRoute) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val searchViewModel = hiltViewModel<SearchViewModel>()
        val productHomeViewModel = hiltViewModel<ProductHomeViewModel>()
        Log.d("LATEINIT_TEST", "homeScreen: ")
        HomeScreen(viewModel = viewModel, onSearchClick = { query, queryType ->
            searchViewModel.updateQuery(query = query, queryType = queryType)
            onSearchClick.invoke()
        }, onProductClick = { productId ->
            productHomeViewModel.updateProductId(productId = productId)
            onProductHomeClick.invoke()
        })
    }
}