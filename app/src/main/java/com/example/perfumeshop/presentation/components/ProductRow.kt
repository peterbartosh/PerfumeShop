package com.example.perfumeshop.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.round
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.data.utils.toBrandFormat
import com.google.firebase.auth.FirebaseAuth

@Preview
@Composable
fun ProductRowPreview() {
    ProductRow(
        productWithAmount = ProductWithAmount(
            product = null,
            cashPriceAmount = 10,
            cashlessPriceAmount = 15
        ),
        onAddToFavouriteClick = {},
        onAddToCartClick = {},
        onRemoveFromFavouriteClick = {},
        onRemoveFromCartClick = {},
        isInFavouriteCheck = { true },
        isInCartCheck = { true }
    )
}


@Composable
fun ProductRow(
    paddingValues: PaddingValues = PaddingValues(2.dp),
    showEditableAmount : Boolean = true,
    productWithAmount: ProductWithAmount,
    //onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (ProductWithAmount) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    showNotValidProducts: MutableState<Boolean>? = null,
    onAmountChange : (Int, Int) -> Unit = {_,_->}
) {

    val context = LocalContext.current


    val isInCart = remember {
        mutableStateOf(isInCartCheck(productWithAmount.product?.id ?: ""))
    }

    val isInFavourite = remember {
        mutableStateOf(isInFavouriteCheck(productWithAmount.product?.id ?: ""))
    }

    val amountCashPrice = rememberSaveable {
        mutableStateOf(productWithAmount.cashPriceAmount ?: 1)
    }

    val amountCashlessPrice = rememberSaveable {
        mutableStateOf(productWithAmount.cashlessPriceAmount ?: 1)
    }

    val scale = remember {
        Animatable(1f)
    }

    var saveErrorState by remember {
        mutableStateOf(false)
    }

    if (showNotValidProducts?.value == true) {
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.8f,
                animationSpec = tween(200)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(200)
                //tween(durationMillis = 200)
            )
            saveErrorState = true
            showNotValidProducts.value = false
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(10.dp),
        border = if (
            saveErrorState && amountCashPrice.value == 0 && amountCashlessPrice.value == 0
        )
            BorderStroke(width = 2.dp, color = Color.Red)
        else {
            if (productWithAmount.product?.isOnHand == false)
                BorderStroke(width = 2.dp, color = Color.Red)
            else if (isInCart.value)
                BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
            else
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues)
            .clip(RoundedCornerShape(10.dp))
            .shadow(elevation = 5.dp)
            .scale(if (amountCashPrice.value == 0 && amountCashlessPrice.value == 0) scale.value else 1f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            //.clickable { onProductClick(productWithAmount.product?.id.toString()) },
            //verticalArrangement = Arr,
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .wrapContentHeight()
                ) {

                    Text(
                        text = productWithAmount.product?.brand.toString().toBrandFormat(),
                        style = MaterialTheme.typography.bodyLarge
                    )


                    productWithAmount.product?.type?.let { type ->
                        val typeText = try {
                            ProductType.valueOf(type).toRus()
                        } catch (e : Exception){
                            ProductType.NotSpecified.toRus()
                        }

                        Text(
                            text = typeText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    val volumeDouble = productWithAmount.product?.volume?.round(1)

                    val volume = try {
                        val volumeInt = volumeDouble?.toInt()
                        if (volumeInt == 0)
                            null
                        else if (volumeInt == 60 && productWithAmount.product?.type == ProductType.Compact.name)
                            "3x20"
                        else
                            volumeInt?.toString()
                    } catch (e: Exception) {
                        volumeDouble?.toString()
                    }

                    if (volume != null)
                        Text(
                            text = volume + "мл.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                }

                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .padding(top = 5.dp, end = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MoreInfoOption(brand = productWithAmount.product?.brand.toString())

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = {
                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                context.showToast(R.string.you_are_not_authorized_error)
                            else {
                                if (isInFavourite.value)
                                    onRemoveFromFavouriteClick(productWithAmount)
                                else
                                    onAddToFavouriteClick(productWithAmount)

                                isInFavourite.value = !isInFavourite.value
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = if (isInFavourite.value)
                                Icons.Outlined.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = "fav icon",
                            tint = if (isInFavourite.value)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = {

                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                context.showToast(R.string.you_are_not_authorized_error)
                            else if (productWithAmount.product?.isOnHand == true) {
                                if (isInCart.value) {
                                    onRemoveFromCartClick(productWithAmount)
                                } else
                                    onAddToCartClick(
                                        ProductWithAmount(
                                            product = productWithAmount.product,
                                            cashPriceAmount = amountCashPrice.value,
                                            cashlessPriceAmount = amountCashlessPrice.value
                                        )
                                    )

                                isInCart.value = !isInCart.value
                            } else if (isInCart.value) {
                                onRemoveFromCartClick(productWithAmount)
                                isInCart.value = !isInCart.value
                            } else {
                                context.showToast(R.string.is_not_on_hand)
                            }

                        }
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = if (isInCart.value)
                                Icons.Filled.ShoppingCart
                            else
                                Icons.Outlined.ShoppingCart,
                            contentDescription = "cart icon",
                            tint = if (isInCart.value)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false) {

                Divider()
                Spacer(modifier = Modifier.height(5.dp))

                repeat(2) { ind ->

                    val priceAnnotatedStrings =
                        buildPriceAnnotationStringsForProduct(productWithAmount = productWithAmount)
                    val prices = listOf(
                        (productWithAmount.cashPriceAmount ?: 1),
                        (productWithAmount.cashlessPriceAmount ?: 1)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 10.dp, bottom = 5.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                        //verticalArrangement = Arrangement.SpaceBetween,
                        //horizontalAlignment = Alignment.End
                    ) {

                        Text(
                            modifier = Modifier,
                            text = priceAnnotatedStrings[ind],
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (!showEditableAmount) {
                            Text(
                                modifier = Modifier,
                                text = prices[ind].toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            if (productWithAmount.product?.isOnHand == false) {
                                if (ind == 1)
                                    Text(
                                        modifier = Modifier.padding(bottom = 5.dp),
                                        text = "Нет в наличии",
                                        style = MaterialTheme.typography.bodyLarge,
                                        softWrap = true,
                                        lineHeight = 15.sp,
                                        color = Color.Red
                                    )
                            }
                            else
                                PlusMinus(
                                    initValue = prices[ind],
                                    onValueChange = { changedAmount ->
                                        if (ind == 0)
                                            amountCashPrice.value = changedAmount
                                        else
                                            amountCashlessPrice.value = changedAmount

                                        onAmountChange(
                                            amountCashPrice.value,
                                            amountCashlessPrice.value
                                        )
                                    }
                                )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun buildPriceAnnotationStringsForProduct(productWithAmount: ProductWithAmount) : List<AnnotatedString> {
    val cashPrice = buildAnnotatedString {
        withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
            val first = "$ " + productWithAmount.product?.cashPrice?.round(2).toString()
            append(first)
        }
    }

    val cashlessPrice = buildAnnotatedString {
        withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
            val first = "$ " + productWithAmount.product?.cashlessPrice?.round(2).toString()
            append(first)
        }
    }

    return listOf(cashPrice, cashlessPrice)
}

