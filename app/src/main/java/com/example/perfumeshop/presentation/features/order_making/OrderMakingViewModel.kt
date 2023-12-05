package com.example.perfumeshop.presentation.features.order_making

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.OrderProduct
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.room.entities.toProductWithAmount
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.data.utils.generateNumber
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "OrderMakingViewModel"

@HiltViewModel
class OrderMakingViewModel @Inject constructor(
    private val fireRepository: FireRepository,
    private val roomRepository: RoomRepository,
    private val emailSender: EmailSender,
    val cartFunctionality: CartFunctionality,
    val auth: FirebaseAuth,
    val userData: UserData
) : ViewModel(){

    var userProducts = emptyList<ProductWithAmount>()

    private var _uiState = MutableStateFlow<UiState>(UiState.NotStarted())
    val uiState : StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = UiState.Loading()
            launch {
                userProducts =
                    roomRepository.getCartProductsAsList().map { it.toProductWithAmount() }
            }.join()
            _uiState.value = UiState.Success()
        }
    }

    fun rememberAddress(city: String, street: String, home: String, flat: String) = viewModelScope.launch {
        userData.updateUserData(city = city, streetName = street, homeNumber = home, flatNumber = flat)
    }

    fun confirmOrder(
        order: Order,
        productWithAmounts: List<ProductWithAmount>
    ) = viewModelScope.launch() {

        withContext(Dispatchers.Default) {

            _uiState.value = UiState.Loading()

            val saveOrderResult = fireRepository.saveToDatabase(item = order, collectionName = "orders")

            val generatedOrderId = saveOrderResult?.getOrNull()

            if (generatedOrderId == null) delay(200)

            val orderNumber = generateNumber(orderId = generatedOrderId, userId = order.userId)

            order.number = orderNumber

            val updateNumberResult = fireRepository.updateInDatabase(
                docId = generatedOrderId,
                collectionName = "orders",
                fieldToUpdate = "number",
                newValue = orderNumber
            )

            Log.d(TAG, "confirmOrder: $generatedOrderId")

            val saveProductsDeferreds = mutableListOf<Deferred<Result<String>?>>()

            productWithAmounts.map { productWithAmount ->
                OrderProduct(
                    orderId = generatedOrderId,
                    productId = productWithAmount.product?.id,
                    cashPriceAmount = productWithAmount.cashPriceAmount,
                    cashlessPriceAmount = productWithAmount.cashlessPriceAmount
                )
            }.forEach { orderProduct ->
                val defResult = async {
                    fireRepository.saveToDatabase(
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
                    userData = userData,
                    scope = this,
                )
            }

            saveProductsDeferreds.add(sendDeferred)

            val sendAndSaveProductsResults = saveProductsDeferreds.awaitAll()

            val allResults = mutableListOf<Result<String>?>()

            allResults.addAll(sendAndSaveProductsResults)
            //allResults.add(clearResult)
            allResults.add(saveOrderResult)
            allResults.add(updateNumberResult)

            val isSuccess = allResults.all { it?.isSuccess == true || it?.exceptionOrNull() == null }

            _uiState.value = if (isSuccess)
                UiState.Success()
            else
                UiState.Failure(allResults.find { it?.exceptionOrNull() != null }?.exceptionOrNull())
        }
    }
}