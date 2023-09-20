package com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.ui.ProfileScreen


const val profileRoute = "profile"

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    this.navigate(route = profileRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileScreen(onOptionClick: (OptionType) -> Unit,
cartViewModel: CartViewModel, favouriteViewModel: FavouriteViewModel) {
    composable(route = profileRoute) {
        ProfileScreen(onOptionClick = onOptionClick,
                      cartViewModel = cartViewModel,
                      favouriteViewModel = favouriteViewModel)
    }
}