package com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.LazyProductList
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    //onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit
) {
    val context = LocalContext.current

    val validState by remember(cartViewModel.userProducts.size) {
        mutableStateOf(
            cartViewModel.userProducts.isNotEmpty()
        )
    }

    val showNotValidProducts = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxHeight(0.9f),
//                verticalArrangement = Arrangement.SpaceBetween,
//                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Корзина",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
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
                            cartViewModel.clearData().onJoin
                            showToast(context = context, "Очищено.")
                        },
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Очистить корзину",
                )
            }


            val uiState = cartViewModel.uiState.collectAsState()

            when (uiState.value){
                is UiState.Success ->
                    if (cartViewModel.userProducts.isNotEmpty())
                        LazyProductList(

                            //onProductClick = onProductClick,
                            listOfProductsWithAmounts = cartViewModel.userProducts,
                            onAddToFavouriteClick = favouriteViewModel::addProduct,
                            onAddToCartClick = cartViewModel::addProduct,
                            onRemoveFromFavouriteClick = favouriteViewModel::removeProduct,
                            onRemoveFromCartClick = cartViewModel::removeProduct,
                            isInCartCheck = cartViewModel::isInCart,
                            isInFavouriteCheck = favouriteViewModel::isInFavourites,
                            onAmountChanged = cartViewModel::updateProductAmount,
                            showNotValidProducts = showNotValidProducts
                        )
                    else
                        NothingFound()

                is UiState.Failure -> ErrorOccurred()
                is UiState.Loading -> Loading()
                else -> Box{}
            }
        }

        SubmitButton(
            validInputsState = validState,
            text = "Перейти к оформлению заказа",
        ) {
            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false) {
                if (
                    cartViewModel.userProducts.all { productWA ->
                        productWA.product?.isOnHand == true &&
                                (productWA.amountCash != 0 || productWA.amountCashless != 0)
                    }
                )
                    onOrderMakeClick()
                else {
                    showNotValidProducts.value = true
                    showToast(context, "Ошибка.\nНельзя заказать 0 единиц продукта.")
                }
            }
            else
                showToast(context, "Вы не авторизованы")
        }
    }
}