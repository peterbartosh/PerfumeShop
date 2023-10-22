package com.example.perfumeshop.presentation.features.main.profile_feature.orders.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel

@Composable
fun OrdersScreen(
    ordersViewModel: OrdersViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onProductClick : (String) -> Unit
) {


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

            if (ordersViewModel.isSuccess)
                LazyColumn {
                    items(ordersViewModel.ordersList) { order ->
                        OrderRow(
                            order = order,
                            productsWithAmount = ordersViewModel.productsWithAmountMap[order.id] ?: emptyList(),
                            onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                            onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                            isInFavouriteCheck = favouriteViewModel::isInFavourite,
                            onAddToCartClick = cartViewModel::addToCart,
                            onRemoveFromCartClick = cartViewModel::removeFromCart,
                            isInCartCheck = cartViewModel::isInCart,
                            clearCart = cartViewModel::clearContent,
                            onProductClick = onProductClick
                        )
                    }
                }
            if (ordersViewModel.isFailure)
                ErrorOccurred()
            else if (ordersViewModel.isLoading)
                LoadingIndicator()
            else if (!ordersViewModel.isLoading && ordersViewModel.ordersList.isEmpty())
                NothingFound()
        }
    }

}
