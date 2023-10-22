package com.example.perfumeshop.presentation.features.main.cart_feature.order_making.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.utils.OrderStatus
import com.example.perfumeshop.data.utils.firstLetterToUpperCase
import com.example.perfumeshop.presentation.components.InputField
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.components.SubmitButton
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.lang.NumberFormatException
import java.util.concurrent.ThreadLocalRandom

@Composable
fun OrderMakingScreen(
    cartViewModel: CartViewModel,
    orderMakingViewModel: OrderMakingViewModel,
    onOrderDone : () -> Unit
) {

    val context = LocalContext.current

    val streetState = remember {
        mutableStateOf(UserData.user?.street ?: "")
    }

    val homeNumberState = remember {
        mutableStateOf(UserData.user?.home ?: "")
    }

    val flatNumberState = remember {
        mutableStateOf(UserData.user?.flat ?: "")
    }

    val validInputsState by remember(streetState.value, homeNumberState.value, flatNumberState.value) {
        mutableStateOf(
            streetState.value.trim().isNotEmpty()
                   && try {
                       homeNumberState.value.trim().toInt()
                       true
                   } catch (nfe : NumberFormatException){
                       Log.d("ERROR_ERROR", "OrderMakingScreen: ${nfe.message}")
                       false
                   }
                   && try {
                       homeNumberState.value.trim().toInt()
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

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, bottom = 20.dp),
                text = "Оформление заказа",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
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
                    keyboardType = KeyboardType.Number,
                    valueState = homeNumberState,
                    label = "Дом",
                    enabled = true
                )

                InputField(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(5.dp),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    valueState = flatNumberState,
                    label = "Кв.",
                    enabled = true
                )
            }
        }

        if (orderMakingViewModel.isLoading) LoadingIndicator()


        // способ оплаты, промокоды, скидки и ...

        SubmitButton(
            text = "Оформить заказ",
            validInputsState = validInputsState
        ) {

            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val connected = manager.activeNetwork

            if (connected == null){
                showToast(context, "Ошибка.\nВы не подключены к сети.")
                return@SubmitButton
            }

            val productWithAmounts = cartViewModel.userProducts

            if (productWithAmounts.isEmpty()) return@SubmitButton

            val order = Order(
                number = ThreadLocalRandom.current().nextInt(100000, 999999).toString(),
                //Random(seed).nextInt(100000, 999999).toString(),
                userId = FirebaseAuth.getInstance().uid,
                address = "ул. " + streetState.value.firstLetterToUpperCase() +
                        ", д. " + homeNumberState.value +
                        ", к. " + flatNumberState.value,
                date = Timestamp.now(),
                status = OrderStatus.Processing.name
            )

            orderMakingViewModel.confirmOrder(
                order = order,
                productWithAmounts = productWithAmounts,
                onFailed = { showToast(context, "Ошибка. Повторите попытку позже") },
                onSuccess = {
                    showToast(context, "Заказ принят")
                    cartViewModel.clearContent()
                    onOrderDone()
                }
            )

        }


    }

}