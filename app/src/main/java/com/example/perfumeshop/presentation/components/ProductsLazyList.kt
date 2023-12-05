package com.example.perfumeshop.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.Constants

@Preview
@Composable
fun ProductsLazyListPreview() {
    ProductsLazyList(
        listOfProductsWithAmounts = listOf(ProductWithAmount(
            product = null,
            cashPriceAmount = 10,
            cashlessPriceAmount = 15
        )),
        onAddToFavouriteClick = {},
        onAddToCartClick = {},
        onRemoveFromFavouriteClick = {},
        onRemoveFromCartClick = {},
        isInFavouriteCheck = { true },
        isInCartCheck = { true }
    )
}

@Composable
fun ProductsLazyList(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(start = 5.dp, end = 5.dp),
    listOfProductsWithAmounts : List<ProductWithAmount>,
    //onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (ProductWithAmount) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,

    onAmountChanged: (String, Int, Int) -> Unit = { _, _, _ ->},

    userScrollEnabled : Boolean = true,
    showNotValidProducts: MutableState<Boolean>? = null,
    uploadMoreData: () -> Unit = {}
) {

    //Spacer(modifier = Modifier.height(5.dp))

    LazyColumn(
        contentPadding = PaddingValues(7.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues),
        state = rememberLazyListState(),
        userScrollEnabled = userScrollEnabled,
    ) {
        itemsIndexed(
            items = listOfProductsWithAmounts,
            key = { _, productWithAmount ->
                productWithAmount.product?.id ?: productWithAmount.hashCode()
            }
        ) { ind, productWithAmount ->

            if (ind == listOfProductsWithAmounts.lastIndex && (ind + 1) % Constants.ITEMS_PER_PAGE_AMOUNT == 0)
                LaunchedEffect(key1 = true) {
                    uploadMoreData()
                }

            ProductRow(
                productWithAmount = productWithAmount,
                //onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck,
                onAmountChange = { cashAmount, cashlessAmount ->
                    productWithAmount.product?.id?.let { id ->
                        onAmountChanged(id, cashAmount, cashlessAmount)
                    }
                },
                showNotValidProducts = showNotValidProducts
            )
        }
    }
}
