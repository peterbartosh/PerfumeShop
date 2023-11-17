package com.example.perfumeshop.presentation.features.main.home_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeScreen
import com.example.perfumeshop.presentation.features.main.home_feature.search.navigation.searchScreen

var homeActiveChild by mutableStateOf(homeRoute)

fun NavGraphBuilder.homeGraph(
    onSearchClick: (String, QueryType) -> Unit,
    //onProductClick: (String) -> Unit,
    onBackPressed : () -> Unit
){
        homeScreen(
            onSearchClick = onSearchClick,
            onBackPressed = onBackPressed
        )

        searchScreen(
           // onProductClick = onProductClick,
            onBackPressed = onBackPressed
        )

}