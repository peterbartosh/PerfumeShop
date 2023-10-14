package com.example.perfumeshop.presentation.features.main.home_feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.home_feature.home.ui.HomeScreen


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