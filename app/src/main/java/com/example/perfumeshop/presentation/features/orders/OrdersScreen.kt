package com.example.perfumeshop.presentation.features.orders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.features.orders.components.OrderRow

@Composable
fun OrdersScreen(
    ordersViewModel: OrdersViewModel,
    //onProductClick : (String) -> Unit
) {

    val uiState = ordersViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 10.dp, bottom = 20.dp),
            text = "Ваши заказы",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Column(modifier = Modifier.fillMaxHeight()) {

            when (uiState.value){
                is UiState.Success -> if (ordersViewModel.ordersList.isNotEmpty()){
                    LazyColumn {
                        items(ordersViewModel.ordersList) { order ->
                            OrderRow(
                                order = order,
                                productsWithAmount = ordersViewModel.productsWithAmountMap[order.id] ?: emptyList(),
                                onAddToFavouriteClick = ordersViewModel.favouriteFunctionality::addProduct,
                                onAddToCartClick = ordersViewModel.cartFunctionality::addProduct,
                                onRemoveFromFavouriteClick = ordersViewModel.favouriteFunctionality::removeProduct,
                                onRemoveFromCartClick = ordersViewModel.cartFunctionality::removeProduct,
                                isInFavouriteCheck = ordersViewModel.favouriteFunctionality::isInFavourites,
                                isInCartCheck = ordersViewModel.cartFunctionality::isInCart,
                                clearCart = ordersViewModel.cartFunctionality::clearData,
                                //onProductClick = onProductClick
                            )
                        }
                    }
                } else NothingFound()
                is UiState.Failure -> ErrorOccurred()
                is UiState.Loading -> Loading()
                else -> Box{}
            }
        }
    }

}
