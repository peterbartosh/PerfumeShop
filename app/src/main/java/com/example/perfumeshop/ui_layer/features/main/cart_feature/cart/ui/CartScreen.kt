package com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.perfumeshop.R
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.components.SubmitButton
import com.example.perfumeshop.ui_layer.components.showToast
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.LazyProductList
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit
) {
        val context = LocalContext.current

        val priceTypeState = remember {
            mutableStateOf(false)
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (cartViewModel.isSuccess) {
                LazyProductList(
                    onProductClick = onProductClick,
                    listOfProductsWithAmounts = cartViewModel.userProducts,
                    onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                    onAddToCartClick = cartViewModel::addToCart,
                    onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                    onRemoveFromCartClick = cartViewModel::removeFromCart,
                    isInCartCheck = cartViewModel::isInCart,
                    priceTypeState = priceTypeState,
                    isInFavouriteCheck = favouriteViewModel::isInFavourite
                )
            }
            if (cartViewModel.isFailure)
                Text(text = stringResource(id = R.string.error_occured))
            else if (cartViewModel.isLoading)
                LoadingIndicator()
            else if (!cartViewModel.isLoading && cartViewModel.userProducts.isEmpty())
                CartIsEmpty()


            SubmitButton(text = "Перейти к оформлению заказа") {
                if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
                    onOrderMakeClick()
                else
                    showToast(context, "Вы не авторизованы")
            }
    }
}


@Composable
fun CartIsEmpty() {
        Text(text = stringResource(id = R.string.nothing_found))
}