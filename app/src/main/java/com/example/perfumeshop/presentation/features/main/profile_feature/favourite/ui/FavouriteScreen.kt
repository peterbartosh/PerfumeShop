package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.LazyProductList
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.showToast

@Composable
fun FavouriteScreen(
    favouriteViewModel: FavouriteViewModel,
    //onProductClick: (String) -> Unit
) {

    val context = LocalContext.current
    val uiState = favouriteViewModel.uiState.collectAsState()
    val userProducts by favouriteViewModel.userProducts.collectAsState()

    Column(
        modifier = Modifier.fillMaxHeight(),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
            text = "Избранное",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(end = 10.dp)
                    .clickable {
                        favouriteViewModel.favouriteFunctionality.clearData().onJoin
                        context.showToast(R.string.content_cleared)
                    },
                textDecoration = TextDecoration.Underline,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                text = "Очистить избранное",
            )
        }

        when (uiState.value){
            is UiState.Success -> if (userProducts.isNotEmpty())
                LazyProductList(
                    listOfProductsWithAmounts = userProducts,
                    //onProductClick = onProductClick,
                    onAddToFavouriteClick = favouriteViewModel.favouriteFunctionality::addProduct,
                    onAddToCartClick = favouriteViewModel.cartFunctionality::addProduct,
                    onRemoveFromFavouriteClick = favouriteViewModel.favouriteFunctionality::removeProduct,
                    onRemoveFromCartClick = favouriteViewModel.cartFunctionality::removeProduct,
                    isInFavouriteCheck = favouriteViewModel.favouriteFunctionality::isInFavourites,
                    isInCartCheck = favouriteViewModel.cartFunctionality::isInCart,
                    onAmountChanged = favouriteViewModel.favouriteFunctionality::updateProductAmount
                )
            else
                NothingFound()
            is UiState.Failure ->
                ErrorOccurred()
            is UiState.Loading ->
                Loading()
            else -> Box{}
        }
    }
}