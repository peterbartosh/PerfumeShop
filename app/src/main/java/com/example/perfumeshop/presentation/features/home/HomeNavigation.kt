package com.example.perfumeshop.presentation.features.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.app.components.BackPressHandler


const val homeRoute = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null){
    this.navigate(route = homeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    onSearchClick: (String, QueryType) -> Unit,
    onBackPressed : () -> Unit
) {

    composable(route = homeRoute) {
        BackPressHandler(onBackPressed = onBackPressed)
        HomeScreen(onSearchClick = onSearchClick)
    }
}