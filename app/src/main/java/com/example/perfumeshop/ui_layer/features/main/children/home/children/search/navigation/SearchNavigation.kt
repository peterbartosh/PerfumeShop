package com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.ui.ProductHomeViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchViewModel


const val searchRoute = "home search"


fun NavController.navigateToSearch(navOptions: NavOptions? = null) {
    this.navigate(searchRoute, navOptions)
}

fun NavGraphBuilder.searchScreen(onProductHomeClick : () -> Unit) {
    composable(route = searchRoute) {
        val viewModel = hiltViewModel<SearchViewModel>()
        val productHomeViewModel = hiltViewModel<ProductHomeViewModel>()
        SearchScreen(query = viewModel.query.value,
                     queryType = viewModel.queryType.value,
                     viewModel = viewModel,
                     onProductClick = { productId ->
                         productHomeViewModel.updateProductId(productId)
                         onProductHomeClick.invoke()
                     })
    }
}