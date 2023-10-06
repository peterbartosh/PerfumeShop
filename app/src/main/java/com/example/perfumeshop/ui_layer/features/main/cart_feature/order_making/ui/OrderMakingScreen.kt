package com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.components.SubmitButton
import com.example.perfumeshop.ui_layer.components.showToast
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui.InputField
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.lang.NumberFormatException
import kotlin.random.Random

@Composable
fun OrderMakingScreen(
    cartViewModel: CartViewModel,
    orderMakingViewModel: OrderMakingViewModel,
    onOrderDone : () -> Unit) {

    val context = LocalContext.current

    val displayName = FirebaseAuth.getInstance().currentUser?.displayName

    val sep = ","

    val streetState = remember {
        mutableStateOf(displayName?.split(sep)?.get(3) ?: "")
    }

    val houseNumberState = remember {
        mutableStateOf(displayName?.split(sep)?.get(4) ?: "")
    }

    val flatNumberState = remember {
        mutableStateOf(displayName?.split(sep)?.get(5) ?: "")
    }

    val validInputsState = remember(streetState.value, houseNumberState.value, flatNumberState.value) {
        mutableStateOf(
                        streetState.value.trim().isNotEmpty()
                               && try {
                                   houseNumberState.value.trim().toInt()
                                   true
                               } catch (nfe : NumberFormatException){
                                   Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
                                   false
                               }
                               && try {
                                   houseNumberState.value.trim().toInt()
                                   true
                                } catch (nfe : NumberFormatException){
                                   Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
                                   false
                                }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(modifier = Modifier.wrapContentHeight()) {

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
                    keyboardType = KeyboardType.Number,
                    valueState = houseNumberState,
                    label = "Дом",
                    enabled = true
                )

                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Number,
                    valueState = flatNumberState,
                    label = "Квартира",
                    enabled = true
                )
            }
        }

        if (orderMakingViewModel.isLoading.value) LoadingIndicator()


        // способ оплаты, промокоды, скидки и ...

        SubmitButton(
            text = "Оформить заказ",
            validInputsState = validInputsState
        ) {
            val products = cartViewModel.userProducts

            if (products.isEmpty()) return@SubmitButton

            val order = Order(
                number = Random(33).nextInt(999999).toString(),
                userId = FirebaseAuth.getInstance().uid,
                productIds = cartViewModel.userProducts.map { mapOf(it.product?.id!! to 1) },
                address = "ул. " + streetState.value + ", д. " +
                        houseNumberState.value + ", к. " + flatNumberState.value,
                date = Timestamp.now()
            )

            orderMakingViewModel.confirmOrder(order = order,
                                              products = products.map { productWithAmount ->
                                                  Pair(
                                                       productWithAmount.product ?: Product(),
                                                       productWithAmount.amount ?: 1
                                                  )
                                              })

            if (orderMakingViewModel.isSuccess.value) {
                showToast(context, "Ошибка. Повторите попытку позже")
            } else {
                showToast(context, "Заказ принят")
                onOrderDone()
            }

        }


    }

}
