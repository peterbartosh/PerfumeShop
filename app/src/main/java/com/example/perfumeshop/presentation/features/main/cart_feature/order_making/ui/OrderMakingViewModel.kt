package com.example.perfumeshop.presentation.features.main.cart_feature.order_making.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.OrderProduct
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "OrderMakingViewModel"
@HiltViewModel
class OrderMakingViewModel @Inject constructor(
        private val repository: FireRepository,
        private val emailSender: EmailSender
    ) : ViewModel(){

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)

    fun confirmOrder(
        order: Order,
        productWithAmounts: List<ProductWithAmount>,
        onFailed : () -> Unit,
        onSuccess : () -> Unit
    ) = viewModelScope.launch() {

        isLoading = true

        val deferred = async(Dispatchers.Default) {

            val saveOrderResult = repository.saveToFirebase(item = order, collectionName = "orders")

            val generatedOrderId = saveOrderResult?.getOrNull()

            Log.d(TAG, "confirmOrder: $generatedOrderId")

            val saveProductsDeferreds = mutableListOf<Deferred<Result<String>?>>()

            productWithAmounts.map { productWithAmount ->
                OrderProduct(
                    orderId = generatedOrderId,
                    productId = productWithAmount.product?.id,
                    amount = productWithAmount.amount,
                    isCashPrice = productWithAmount.isCashPrice
                )
            }.forEach { orderProduct ->
                val defResult = async {
                    repository.saveToFirebase(
                        item = orderProduct,
                        collectionName = "orders_products",
                        updateId = false
                    )
                }
                saveProductsDeferreds.add(defResult)
            }


            val sendDeferred = async {
                emailSender.sendOrderEmail(
                    order = order,
                    products = productWithAmounts,
                    scope = this
                )
            }

            saveProductsDeferreds.add(sendDeferred)

            val sendAndSaveProductsResults = saveProductsDeferreds.awaitAll()

            val uid = FirebaseAuth.getInstance().uid
            val clearResult = repository.clearCart(uid)

            val allResults = mutableListOf<Result<String>?>()
            allResults.addAll(sendAndSaveProductsResults)
            allResults.add(clearResult)
            allResults.add(saveOrderResult)

            isSuccess = allResults.all { it?.isSuccess == true || it?.exceptionOrNull() == null }

            if (!isSuccess) {
                Log.d(
                    TAG,
                    "confirmOrder: ${
                        allResults.find { it?.exceptionOrNull() != null }?.exceptionOrNull()
                    }"
                )
            }
        }

        deferred.await()

        isLoading = false

        if (isSuccess) onSuccess() else onFailed()
    }
}