package com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.LazyProductList
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast
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

        val validState by remember(!cartViewModel.userProducts.isEmpty()) {
            mutableStateOf(!cartViewModel.userProducts.isEmpty())
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier.fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 10.dp, bottom = 20.dp),
                    text = "Корзина",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )



                if (cartViewModel.isSuccess && cartViewModel.userProducts.isNotEmpty()) {
                    LazyProductList(
                        onProductClick = onProductClick,
                        listOfProductsWithAmounts = cartViewModel.userProducts,
                        onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                        onAddToCartClick = cartViewModel::addToCart,
                        onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                        onRemoveFromCartClick = cartViewModel::removeFromCart,
                        isInCartCheck = cartViewModel::isInCart,

                        onAmountChanged = cartViewModel::updateProductAmountInCart,
                        onCashStateChanged = cartViewModel::updateProductCashStateInCart,

                        isInFavouriteCheck = favouriteViewModel::isInFavourite
                    )
                } else if (cartViewModel.isFailure)
                    ErrorOccurred()
                else if (!cartViewModel.isLoading && cartViewModel.userProducts.isEmpty())
                    NothingFound()
            }

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