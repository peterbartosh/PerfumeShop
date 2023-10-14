package com.example.perfumeshop.presentation.features.main.cart_feature.order_making.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.models.Order
import com.example.perfumeshop.data.models.OrderProduct
import com.example.perfumeshop.data.models.ProductWithAmount
import com.example.perfumeshop.data.repositories.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderMakingViewModel @Inject constructor(private val repository: FireRepository,
                                     private val emailSender: EmailSender) : ViewModel(){

    val isLoading : MutableState<Boolean> = mutableStateOf(false)
    val isSuccess : MutableState<Boolean> = mutableStateOf(false)
    //val e : MutableState<Exception?> = mutableStateOf(null)


    fun confirmOrder(
        order: Order,
        productWithAmounts: List<ProductWithAmount>,
        onFailed : () -> Unit,
        onSuccess : () -> Unit
    ) = viewModelScope.launch() {

            isLoading.value = true

        async(Dispatchers.Default) {

            val sendTaskState = emailSender.sendOrderEmail(
                order = order,
                products = productWithAmounts,
                scope = this
            )

            val saveOrderTaskState =
                repository.saveToFirebase(item = order, collectionName = "orders")

            val generatedOrderId =
                if (saveOrderTaskState.first)
                    saveOrderTaskState.second as String
                else
                    null


            var saveOrderProductsTaskState: Pair<Boolean, Any?> = Pair(true, null)

            productWithAmounts.map { productWithAmount ->
                OrderProduct(
                    orderId = generatedOrderId,
                    productId = productWithAmount.product?.id,
                    amount = productWithAmount.amount,
                    isCashPrice = productWithAmount.isCashPrice
                )
            }.forEach { orderProduct ->
                val state = repository.saveToFirebase(
                    item = orderProduct,
                    collectionName = "orders_products",
                    updateId = false
                )
                if (!state.first)
                    saveOrderProductsTaskState =
                        Pair(false, state.second ?: Exception("null exception"))
            }

            val uid = FirebaseAuth.getInstance().uid

            val clearState = repository.clearCart(uid)

            isSuccess.value =
                saveOrderTaskState.first && sendTaskState.first && clearState && saveOrderProductsTaskState.first

            if (!isSuccess.value)
                Log.d(
                    "ERROR_ERROR",
                    (saveOrderProductsTaskState.second ?: saveOrderTaskState.second
                    ?: sendTaskState.second).toString()
                )
            }.await()

            isLoading.value = false

            if (isSuccess.value){
                onSuccess()
            } else {
                onFailed()
            }
        }

}