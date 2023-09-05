package com.example.perfumeshop.ui_layer.features.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.data_layer.utils.navigateToGraph
import com.example.perfumeshop.ui_layer.features.main.children.cart.navigation.cartGraph
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeGraph
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeGraphRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileGraph
import com.example.perfumeshop.ui_layer.features.main.children.settings.navigation.settingsScreen

const val mainGraphRoute = "main"

fun NavController.navigateToMainGraph(){
    this.navigateToGraph(graphRoute = mainGraphRoute, saveStartDest = false){}
}

fun NavGraphBuilder.mainGraph(
    onThemeChange : (Boolean) -> Unit,
    navigateSearch : () -> Unit,
    navigateOption: (OptionType) -> Unit,
    navigateProductHome : () -> Unit,
    navigateProductCart : () -> Unit,
    navigateProductProfile : () -> Unit,
){
    navigation(startDestination = homeRoute, route = mainGraphRoute){


        homeGraph(onSearchClick = navigateSearch, onProductHomeClick = navigateProductHome)

        cartGraph(onProductClick = navigateProductCart)

        profileGraph(onOptionClick = navigateOption, onProductClick = navigateProductProfile)

        settingsScreen(onClick = {}, onThemeChange = onThemeChange)

        //setCurrentNestedGraph(build())


    }
}