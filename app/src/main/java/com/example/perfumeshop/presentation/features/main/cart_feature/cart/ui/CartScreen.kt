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
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.LazyProductList
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    //onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit
) {
    val context = LocalContext.current

    val uiState = cartViewModel.uiState.collectAsState()
    val userProducts by cartViewModel.userProducts.collectAsState()

    val validState by remember(userProducts.size) {
        mutableStateOf(
            userProducts.isNotEmpty()
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
                            cartViewModel.cartFunctionality.clearData().onJoin
                            context.showToast(R.string.content_cleared)
                        },
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Очистить корзину",
                )
            }

            when (uiState.value){
                is UiState.Success ->
                    if (userProducts.isNotEmpty())
                        LazyProductList(
                            //onProductClick = onProductClick,
                            listOfProductsWithAmounts = userProducts,
                            onAddToFavouriteClick = cartViewModel.favouriteFunctionality::addProduct,
                            onAddToCartClick = cartViewModel.cartFunctionality::addProduct,
                            onRemoveFromFavouriteClick = cartViewModel.favouriteFunctionality::removeProduct,
                            onRemoveFromCartClick = cartViewModel.cartFunctionality::removeProduct,
                            isInCartCheck = cartViewModel.cartFunctionality::isInCart,
                            isInFavouriteCheck = cartViewModel.favouriteFunctionality::isInFavourites,
                            onAmountChanged = cartViewModel.cartFunctionality::updateProductAmount,
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
            text = context.getString(R.string.cart_screen_submit_button_text),
        ) {
            if (cartViewModel.auth.currentUser?.isAnonymous == false) {
                if (userProducts.any { productWA ->
                        productWA.product?.isOnHand == false
                    }
                ){
                    showNotValidProducts.value = true
                    context.showToast(R.string.cart_screen_is_not_on_hand_error)
                    return@SubmitButton
                }

                if (userProducts.any { productWA ->
                        productWA.cashPriceAmount == 0 ||
                                productWA.cashlessPriceAmount == 0
                    }
                ){
                    showNotValidProducts.value = true
                    context.showToast(R.string.cart_screen_zero_amount_error)
                    return@SubmitButton
                }

                onOrderMakeClick()

            }
            else
                context.showToast(R.string.you_are_not_authorized_error)
        }
    }
}