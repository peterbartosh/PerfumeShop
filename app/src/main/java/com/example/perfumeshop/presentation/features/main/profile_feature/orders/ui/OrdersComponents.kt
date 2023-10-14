package com.example.perfumeshop.presentation.features.main.profile_feature.orders.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.models.Order
import com.example.perfumeshop.data.models.Product
import com.example.perfumeshop.data.models.ProductWithAmount
import com.example.perfumeshop.presentation.features.main.home_feature.search.ui.ProductRow

@Composable
fun OrderRow(
    order : Order,
    productsWithAmount: List<ProductWithAmount>,
    onAddToFavouriteClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInCartCheck : (String) -> Boolean,
    onProductClick: (String) -> Unit
) {

    var downArrowClicked by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {

        Card(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.size(30.dp)) {}
                Text(text = order.number ?: "Not found", modifier = Modifier.padding(5.dp))
                Icon(
                    imageVector = if (downArrowClicked)
                        Icons.Outlined.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = "arrow down icon",
                    modifier = Modifier.clickable { downArrowClicked = !downArrowClicked }
                )
            }

        }

        if (downArrowClicked) {
            ProductsList(
                productsWithAmount = productsWithAmount,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                isInFavouriteCheck =isInFavouriteCheck,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInCartCheck = isInCartCheck,
                onProductClick = onProductClick
            )
        }

    }



}

@Composable
fun ProductsList(
    productsWithAmount : List<ProductWithAmount>,
    onAddToFavouriteClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInCartCheck : (String) -> Boolean,
    onProductClick: (String) -> Unit
) {

    Column(modifier = Modifier
        .fillMaxWidth(0.9f)
        .wrapContentHeight()) {

        productsWithAmount.forEach { productWithAmount ->

            ProductRow(
                parentScreen = "orders",
                showEditableAmount = false,
                productWithAmount = productWithAmount,
                onAmountChange = {},
                onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck
            )

        }

    }


}