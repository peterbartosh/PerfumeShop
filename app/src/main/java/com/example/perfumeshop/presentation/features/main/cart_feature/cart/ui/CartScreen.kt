package com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.home_feature.search.ui.LazyProductList
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit
) {
        val context = LocalContext.current

        val validState = remember(!cartViewModel.userProducts.isEmpty()) {
            mutableStateOf(!cartViewModel.userProducts.isEmpty())
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (cartViewModel.isSuccess) {
                LazyProductList(
                    parentScreen = "cart",
                    onProductClick = onProductClick,
                    listOfProductsWithAmounts = cartViewModel.userProducts,
                    updateChangedAmount = cartViewModel::updateProductAmountInCart,
                    onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                    onAddToCartClick = cartViewModel::addToCart,
                    onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                    onRemoveFromCartClick = cartViewModel::removeFromCart,
                    isInCartCheck = cartViewModel::isInCart,
                    isCashPriceState = null,
                    isInFavouriteCheck = favouriteViewModel::isInFavourite
                )
            }
            if (cartViewModel.isFailure)
                Text(text = stringResource(id = R.string.error_occured))
            else if (cartViewModel.isLoading)
                LoadingIndicator()
            else if (!cartViewModel.isLoading && cartViewModel.userProducts.isEmpty())
                NothingFound()


            SubmitButton(
                validInputsState = validState,
                text = "Перейти к оформлению заказа",
            ) {
                if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
                    onOrderMakeClick()
                else
                    showToast(context, "Вы не авторизованы")
            }
    }
}


@Composable
fun NothingFound() {
        Text(text = stringResource(id = R.string.nothing_found))
}