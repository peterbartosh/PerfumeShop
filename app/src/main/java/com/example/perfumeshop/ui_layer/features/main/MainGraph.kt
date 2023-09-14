package com.example.perfumeshop.ui_layer.features.main

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.cart_feature.navigation.cartActiveChild
import com.example.perfumeshop.ui_layer.features.main.cart_feature.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.cart_feature.navigation.cartScreen
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.ui_layer.features.main.home_feature.homeGraph
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui.HomeViewModel
import com.example.perfumeshop.ui_layer.features.main.product_feature.navigation.productScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profileActiveChild
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profileGraph
import com.example.perfumeshop.ui_layer.features.main.settings_feature.navigation.settingsScreen


fun getActiveChild(parentRoute : String) : String{
    return when(parentRoute){
        homeRoute -> homeActiveChild
        cartRoute -> cartActiveChild
        profileRoute -> profileActiveChild
        else -> parentRoute
    }
}

fun NavGraphBuilder.mainGraph(
    onThemeChange: (Boolean) -> Unit,
    navigateSearch: (String, QueryType) -> Unit,
    navigateOption: (OptionType) -> Unit,
    navigateProduct: (String) -> Unit,
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
){

        homeGraph(cartViewModel = cartViewModel,
                  favouriteViewModel = favouriteViewModel,
                  onSearchClick = navigateSearch,
                  homeViewModel = homeViewModel,
                  onProductClick = navigateProduct)

        cartScreen(cartViewModel = cartViewModel,
                  favouriteViewModel = favouriteViewModel,
                  onProductClick = navigateProduct)

        profileGraph(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onOptionClick = navigateOption,
                     onProductClick = navigateProduct)

        productScreen(
            onClick = {},
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )

        settingsScreen(onClick = {},
                       onThemeChange = onThemeChange)


}