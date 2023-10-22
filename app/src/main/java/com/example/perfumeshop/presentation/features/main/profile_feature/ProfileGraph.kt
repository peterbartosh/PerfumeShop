package com.example.perfumeshop.presentation.features.main.profile_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.edit_profile.navigation.editProfileScreen
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation.favouriteScreen
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.orders.navigation.ordersScreen
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileScreen

var profileActiveChild by mutableStateOf(profileRoute)

fun NavGraphBuilder.profileGraph(
    onOptionClick: (OptionType) -> Unit,
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onBackPressed: () -> Unit
){
        profileScreen(
            onOptionClick = onOptionClick,
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onBackPressed = onBackPressed
        )

        editProfileScreen(
            onClick = {},
            onBackPressed = onBackPressed
        )

        favouriteScreen(
            onProductClick = onProductClick,
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            onBackPressed = onBackPressed
        )

        ordersScreen(
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            onProductClick = onProductClick,
            onBackPressed = onBackPressed
        )

}