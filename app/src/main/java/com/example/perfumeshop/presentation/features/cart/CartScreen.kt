package com.example.perfumeshop.presentation.features.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.ProductsLazyList
import com.example.perfumeshop.presentation.features.cart.components.CartSubmitButton
import com.example.perfumeshop.presentation.features.cart.components.UpperSection


@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    //onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit
) {

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


            UpperSection(userProducts = userProducts) {
                cartViewModel.cartFunctionality.clearData()
            }

            when (uiState.value){
                is UiState.Success ->
                    if (userProducts.isNotEmpty())
                        ProductsLazyList(
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

        CartSubmitButton(
            userProducts = userProducts,
            auth = cartViewModel.auth,
            validState = validState,
            showNotValidProducts = showNotValidProducts,
            onOrderMakeClick = onOrderMakeClick
        )
    }
}