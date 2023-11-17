package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteScreen
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.profileActiveChild


const val favouriteRoute = "favourites"

fun NavController.navigateToFavouritesRoute(navOptions: NavOptions? = null) {
    profileActiveChild = favouriteRoute
    this.navigate(route = favouriteRoute, navOptions = navOptions)
}

fun NavGraphBuilder.favouriteScreen(
    //onProductClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = favouriteRoute) {
        val favouriteViewModel = hiltViewModel<FavouriteViewModel>()
        BackPressHandler(onBackPressed = onBackPressed)
        FavouriteScreen(
            favouriteViewModel,
           // onProductClick = onProductClick
        )
    }
}