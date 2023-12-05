package com.example.perfumeshop.presentation.features.orders.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.presentation.components.ProductRow

@Composable
fun ProductsList(
    paddingValues: PaddingValues,
    productsWithAmount : List<ProductWithAmount>,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (ProductWithAmount) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    //onProductClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        //horizontalAlignment = Alignment.End
    ) {

        productsWithAmount.forEach { productWithAmount ->

            ProductRow(
                paddingValues = paddingValues,
                showEditableAmount = false,
                productWithAmount = productWithAmount,
                //onProductClick = onProductClick,
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