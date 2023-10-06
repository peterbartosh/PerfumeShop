package com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.ui_layer.components.showToast
import com.example.perfumeshop.ui_layer.theme.Gold
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LazyProductList(
    modifier: Modifier = Modifier,
    listOfProducts : List<Product>,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    userScrollEnabled : Boolean = true,
) {

    LazyColumn(
        contentPadding = PaddingValues(3.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = rememberLazyListState(),
        userScrollEnabled = userScrollEnabled,
    ) {
        itemsIndexed(listOfProducts, key = {i, p -> p.id ?: i}) { ind, product ->
            ProductRow(
                product = product,
                onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                isInFavouriteCheck = isInFavouriteCheck)
        }
    }
}


@Composable
fun ProductRow(
    product: Product,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
) {

    val context = LocalContext.current

    val isInFavourite = remember {
        mutableStateOf(isInFavouriteCheck(product.id ?: ""))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onProductClick(product.id.toString()) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .wrapContentHeight()
                    .padding(start = 10.dp)
            ) {
                Text(text = product.brand.toString(), fontSize = 13.sp)

                Text(text = product.volume.toString(), fontSize = 10.sp)

                Divider()
            }


            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(end = 10.dp),
                //verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                            showToast(context, "Вы не авторизованы")
                        else {
                            if (isInFavourite.value) onRemoveFromFavouriteClick(product.id!!)
                            else onAddToFavouriteClick(product)
                            isInFavourite.value = !isInFavourite.value
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isInFavourite.value) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "fav icon",
                        tint = if (isInFavourite.value) Gold else Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

            }
        }
    }
}