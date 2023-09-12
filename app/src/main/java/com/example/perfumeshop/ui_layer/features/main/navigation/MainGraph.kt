package com.example.perfumeshop.ui_layer.features.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.cart.navigation.cartActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.cart.navigation.cartGraph
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeGraph
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileActiveChild
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileGraph
import com.example.perfumeshop.ui_layer.features.main.children.settings.navigation.settingsScreen


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
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
){

        homeGraph(cartViewModel = cartViewModel,
                  favouriteViewModel = favouriteViewModel,
                  onSearchClick = navigateSearch,
                  onProductClick = navigateProduct)

        cartGraph(cartViewModel = cartViewModel,
                  favouriteViewModel = favouriteViewModel,
                  onProductClick = navigateProduct)

        profileGraph(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onOptionClick = navigateOption,
                     onProductClick = navigateProduct)

        settingsScreen(onClick = {},
                       onThemeChange = onThemeChange)


}