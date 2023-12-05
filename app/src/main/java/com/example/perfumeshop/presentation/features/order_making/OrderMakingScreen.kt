package com.example.perfumeshop.presentation.features.order_making

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.utils.OrderStatus
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.data.utils.round
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.features.order_making.components.ProductBox
import com.google.firebase.Timestamp

@Composable
fun OrderMakingScreen(
    orderMakingViewModel: OrderMakingViewModel,
    onOrderDone : () -> Unit
) {

    val context = LocalContext.current

    val cityState = remember {
        mutableStateOf(orderMakingViewModel.userData.user?.city ?: "")
    }

    val streetState = remember {
        mutableStateOf(orderMakingViewModel.userData.user?.street ?: "")
    }

    val homeNumberState = remember {
        mutableStateOf(orderMakingViewModel.userData.user?.home ?: "")
    }

    val flatNumberState = remember {
        mutableStateOf(orderMakingViewModel.userData.user?.flat ?: "")
    }

    val validInputsState by remember(
        streetState.value, homeNumberState.value,
        flatNumberState.value, cityState.value
    ) {
        mutableStateOf(
            cityState.value.trim().isNotEmpty() &&
            streetState.value.trim().isNotEmpty() &&
            homeNumberState.value.trim().isNotEmpty() &&
            flatNumberState.value.trim().isNotEmpty()

        )
    }

    val uiState = orderMakingViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
        ) {

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
                            if (validInputsState) {
                                orderMakingViewModel
                                    .rememberAddress(
                                        cityState.value,
                                        streetState.value,
                                        homeNumberState.value,
                                        flatNumberState.value
                                    ).onJoin
                                context.showToast(R.string.address_remembered, Toast.LENGTH_LONG)
                            } else {
                                context.showToast(R.string.incorrect_input_error, Toast.LENGTH_LONG)
                            }
                        },
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "Запомнить адрес",
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Оформление заказа",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                valueState = cityState,
                label = "Город",
                enabled = true
            )

            InputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 5.dp),
                valueState = streetState,
                label = "Улица",
                enabled = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Password,
                    valueState = homeNumberState,
                    label = "Дом",
                    enabled = true
                )

                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    valueState = flatNumberState,
                    label = "Кв.",
                    enabled = true
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ваш заказ:",
                    modifier = Modifier.padding(start = 5.dp),
                    style = MaterialTheme.typography.labelLarge
                )

                val totalStr = buildAnnotatedString {

                    withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle()){
                        append("Итого: ")
                    }

                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()){
                        val total = orderMakingViewModel.userProducts.sumOf { productWithAmount ->

                        val cashSum = ((productWithAmount.product?.cashPrice ?: 0.0) * (productWithAmount.cashPriceAmount ?: 1))
                        val cashlessSum = ((productWithAmount.product?.cashlessPrice ?: 0.0) * (productWithAmount.cashlessPriceAmount ?: 1))
                            cashSum + cashlessSum
                        }.round(2)

                        append(total.toString())
                    }
                }

                Text(
                    text = totalStr,
                    modifier = Modifier.padding(end = 5.dp),
                )
            }

            Divider(color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.height(3.dp))

            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                itemsIndexed(
                    items = orderMakingViewModel.userProducts.toList(),
                    key = { i, p -> p.product?.id ?: i }
                ) { _, productWithAmount ->
                    ProductBox(productWithAmount = productWithAmount)
                }
            }

        }


        // промокоды, скидки и ...

        SubmitButton(
            text = "Заказать",
            validInputsState = validInputsState && uiState.value !is UiState.Loading
        ) {

            if (!isUserConnected(context)){
                context.showToast(R.string.connection_lost_error)
                return@SubmitButton
            }

            val productWithAmounts = orderMakingViewModel.userProducts

            if (productWithAmounts.isEmpty()) return@SubmitButton

            val order = Order(
                userId = orderMakingViewModel.auth.uid,
                address = "ул. " + streetState.value.firstLetterToUpperCase() +
                        ", д. " + homeNumberState.value +
                        ", к. " + flatNumberState.value,
                date = Timestamp.now(),
                status = OrderStatus.Processing.name
            )

            orderMakingViewModel.confirmOrder(
                order = order,
                productWithAmounts = productWithAmounts
            ).invokeOnCompletion {
                when (uiState.value){
                    is UiState.Success ->{
                        context.showToast(R.string.order_accepted)
                        orderMakingViewModel.cartFunctionality.clearData()
                        onOrderDone()
                    }
                    is UiState.Failure -> {
                        context.showToast(R.string.try_later_error)
                    }
                    else -> {}
                }
            }

        }
    }
    if (uiState.value is UiState.Loading) Loading()
}