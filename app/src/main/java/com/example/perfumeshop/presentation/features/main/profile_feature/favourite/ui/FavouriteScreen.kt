package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.NothingFound

@Composable
fun FavouriteScreen(
    favouriteViewModel: FavouriteViewModel,
    onProductClick: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (favouriteViewModel.isSuccess)
            LazyProductList(
                listOfProducts = favouriteViewModel.userProducts,
                onProductClick = onProductClick,
                onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                isInFavouriteCheck = favouriteViewModel::isInFavourite
            )
        if (favouriteViewModel.isFailure)
            Text(text = stringResource(id = R.string.error_occured))
        else if (favouriteViewModel.isLoading)
            LoadingIndicator()
        else if (!favouriteViewModel.isLoading && favouriteViewModel.userProducts.isEmpty())
            NothingFound()

    }
}