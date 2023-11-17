package com.example.perfumeshop.presentation.features.main.profile_feature.orders.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.OrderStatus
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.components.ProductRow
import com.example.perfumeshop.presentation.components.showToast
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun OrderRow(
    order : Order,
    productsWithAmount: List<ProductWithAmount>,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (ProductWithAmount) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    clearCart : () -> Unit,
    //onProductClick: (String) -> Unit
) {

    val context = LocalContext.current

    var downArrowClicked by rememberSaveable {
        mutableStateOf(false)
    }

    val notFound = "_"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { downArrowClicked = !downArrowClicked },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.padding(start = 10.dp)) {

                    Text(
                        text = " №" + (order.number ?: notFound) + " ",
                        modifier = Modifier
                            .padding(5.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary
                                ), shape = RoundedCornerShape(5.dp)
                            ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )


                    val address = order.address?.split(" ")?.mapIndexed { ind, item ->
                        if (ind == 1)
                            item.firstLetterToUpperCase()
                        else
                            item
                    }?.joinToString(separator = " ")

                    Text(
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp),
                        text = address ?: notFound,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm:ss")
                    val local = order.date?.toDate()?.toInstant()?.atZone(ZoneId.of("UTC+03:00"))?.toLocalDateTime()
                    //Log.d("TIMESTAMP_TEST", local.format(formatter))

                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = local?.format(formatter) ?: notFound,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )



                    Row (
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        val status = OrderStatus.valueOf(order.status.toString())

                        Text(
                            text = status.toRus(),
                            modifier = Modifier.padding(5.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = when (status) {
                                OrderStatus.Succeed -> Color.Green
                                OrderStatus.Canceled -> Color.Red
                                else -> MaterialTheme.colorScheme.onBackground
                            }
                        )

                        //Spacer(modifier = Modifier.width())

                       QuestionBox(status = status)

                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 5.dp, bottom = 15.dp)
                            .clickable {
                                clearCart()

                                productsWithAmount.forEach { productWithAmount ->
                                    onAddToCartClick(productWithAmount)
                                }
                                context.showToast(R.string.cart_updated)
                            },
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "Повтор заказа",
                    )


                    Icon(
                        imageVector = if (downArrowClicked)
                            Icons.Outlined.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,

                        contentDescription = "arrow down icon",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(bottom = 5.dp)
                            //.clickable { downArrowClicked = !downArrowClicked }
                    )
                }
            }

        }

        AnimatedVisibility(visible = downArrowClicked) {
            ProductsList(
                paddingValues = PaddingValues(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp),
                productsWithAmount = productsWithAmount,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                isInFavouriteCheck =isInFavouriteCheck,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInCartCheck = isInCartCheck,
                //onProductClick = onProductClick
            )
        }

//        if (downArrowClicked) {
//            ProductsList(
//                paddingValues = PaddingValues(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 2.dp),
//                productsWithAmount = productsWithAmount,
//                onAddToFavouriteClick = onAddToFavouriteClick,
//                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
//                isInFavouriteCheck =isInFavouriteCheck,
//                onAddToCartClick = onAddToCartClick,
//                onRemoveFromCartClick = onRemoveFromCartClick,
//                isInCartCheck = isInCartCheck,
//                onProductClick = onProductClick
//            )
//        }

    }
}

@Composable
fun QuestionBox(status: OrderStatus) {

    val context = LocalContext.current

    val wp = getWidthPercent(context = context)

    var showAskBar by remember {
        mutableStateOf(false)
    }

    Box {

        Text(
            modifier = Modifier
                .size(25.dp)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = CircleShape
                )
                .clip(shape = CircleShape)
                //.shadow(elevation = 5.dp)
                .clickable {
                    showAskBar = true
                },
            text = "?",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )

        DropdownMenu(
            modifier = Modifier
                .width(wp * 80)
                .wrapContentHeight()
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(5.dp)
                ),
            expanded = showAskBar,
            onDismissRequest = { showAskBar = false }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()

            ) {
                val textInd = when (status){
                    OrderStatus.Processing -> R.string.processing_status
                    OrderStatus.Accepted -> R.string.accepted_status
                    OrderStatus.Delivering -> R.string.delivering_status
                    OrderStatus.Succeed -> R.string.succeed_status
                    OrderStatus.Canceled -> R.string.canceled_status
                }
                Text(
                    text = stringResource(id = textInd),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

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