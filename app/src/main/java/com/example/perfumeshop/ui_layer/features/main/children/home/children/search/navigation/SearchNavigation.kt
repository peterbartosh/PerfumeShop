package com.example.perfumeshop.ui_layer.features.main.children.home.children.search.navigation

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchScreen
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel


const val searchRoute = "search"

const val queryKey = "query"
const val queryTypeKey = "queryType"

fun NavController.navigateToSearch(query : String, queryType : QueryType, navOptions: NavOptions? = null) {
    homeActiveChild = searchRoute
    this.navigate(route = searchRoute, navOptions = navOptions)
    this.currentBackStackEntry?.savedStateHandle?.set(queryKey, query)
    this.currentBackStackEntry?.savedStateHandle?.set(queryTypeKey, queryType)
}

fun NavGraphBuilder.searchScreen(
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {
    composable(route = searchRoute) { backStackEntry ->

        val searchViewModel = hiltViewModel<SearchViewModel>()
        val query = backStackEntry.savedStateHandle.get<String>(queryKey) ?: ""
        val queryType =
            backStackEntry.savedStateHandle.get<QueryType>(queryTypeKey) ?: QueryType.brand

        Log.d("QUERY_TEST", query + " " + queryType.name)

        if (homeActiveChild != searchRoute) {
            searchViewModel.isFailure = false
            searchViewModel.isSuccess = false
            searchViewModel.isLoading = false
            searchViewModel.initSearchQuery = true
        }

        SearchScreen(
            query = query,
            queryType = queryType,
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            viewModel = searchViewModel,
            onProductClick = onProductClick
        )
    }
}

