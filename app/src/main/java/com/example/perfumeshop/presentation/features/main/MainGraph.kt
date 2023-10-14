package com.example.perfumeshop.presentation.features.main

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.cart_feature.cartGraph
import com.example.perfumeshop.presentation.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.homeGraph
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.product_feature.navigation.productScreen
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profileActiveChild
import com.example.perfumeshop.presentation.features.main.profile_feature.profileGraph
import com.example.perfumeshop.presentation.features.main.settings_feature.navigation.settingsScreen


fun getActiveChild(parentRoute : String) : String{
    return when(parentRoute){
        homeRoute -> homeActiveChild
        cartRoute -> cartActiveChild
        profileRoute -> profileActiveChild
        else -> parentRoute
    }
}

//@Composable
//inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
//    navController: NavHostController,
//): T {
//    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
//    val parentEntry = remember(this) {
//        navController.getBackStackEntry(navGraphRoute)
//    }
//    return hiltViewModel(parentEntry)
//}

fun NavGraphBuilder.mainGraph(
    onThemeChange: (Boolean) -> Unit,
    navigateSearch: (String, QueryType) -> Unit,
    navigateOption: (OptionType) -> Unit,
    navigateProduct: (String) -> Unit,
    navigateToOrderMaking: () -> Unit,
    navigateToCart: () -> Unit,
    //homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onBackPressed: () -> Unit
){

        homeGraph(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onSearchClick = navigateSearch,
            onProductClick = navigateProduct,
            onBackPressed = onBackPressed
        )

        cartGraph(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onProductClick = navigateProduct,
            onOrderDone = navigateToCart,
            onOrderMakeClick = navigateToOrderMaking,
            onBackPressed = onBackPressed
        )

        profileGraph(
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onOptionClick = navigateOption,
            onProductClick = navigateProduct,
            onBackPressed = onBackPressed
        )

        productScreen(
            onClick = {},
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel,
            onBackPressed = onBackPressed
        )

        settingsScreen(
            onClick = {},
            onThemeChange = onThemeChange,
            onBackPressed = onBackPressed
        )


}