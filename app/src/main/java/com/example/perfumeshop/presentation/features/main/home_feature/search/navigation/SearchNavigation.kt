package com.example.perfumeshop.presentation.features.main.home_feature.search.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.search.ui.SearchScreen
import com.example.perfumeshop.presentation.features.main.home_feature.search.ui.SearchViewModel


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
   // onProductClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = searchRoute) { backStackEntry ->

        val searchViewModel = hiltViewModel<SearchViewModel>()

        val query = backStackEntry.savedStateHandle.get<String>(queryKey) ?: ""
        val queryType =
            backStackEntry.savedStateHandle.get<QueryType>(queryTypeKey) ?: QueryType.brand

        BackPressHandler(onBackPressed = onBackPressed)
        SearchScreen(
            initQuery = query,
            initQueryType = queryType,
            searchViewModel = searchViewModel,
                //onProductClick = onProductClick
        )
    }
}

