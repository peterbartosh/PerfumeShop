package com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.CartObj
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.repository.TAG
import com.example.perfumeshop.data.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Implements cart functionality, which needed throughout the application.
That's why it is scoped to an App() composable.
 */

@HiltViewModel
class CartViewModel @Inject constructor(
    val auth: FirebaseAuth,
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository
) : ViewModel() {

    // if content changes, triggers recomposition.
    val userProducts = SnapshotStateList<ProductWithAmount>()

    private var _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState : StateFlow<UiState> = _uiState

    fun isInCart(productId : String) =
        userProducts.find { productWithAmount ->
            productWithAmount.product?.id == productId
        } != null

    fun addProduct(productWithAmount: ProductWithAmount) = viewModelScope.launch(Dispatchers.IO) {
        userProducts.add(productWithAmount)
        roomRepository.insertCartProduct(productWithAmount)
    }

    fun removeProduct(productWithAmount: ProductWithAmount) = viewModelScope.launch(Dispatchers.IO)  {
        userProducts.removeIf { productWA ->
            productWithAmount.product?.id == productWA.product?.id
        }
        roomRepository.deleteCartProduct(productWithAmount)
    }

    fun updateProductAmount(ind: Int, cashAmount: Int, cashlessAmount: Int) = viewModelScope.launch(Dispatchers.IO)  {
        userProducts[ind].amountCash = cashAmount
        userProducts[ind].amountCashless = cashlessAmount
        userProducts[ind].product?.id?.let { id ->
            roomRepository.updateCartProductAmount(id, cashAmount, cashlessAmount)
        }
    }

    fun clearData() = viewModelScope.launch(Dispatchers.IO) {
        userProducts.clear()
        roomRepository.deleteAllInCart()
    }

    fun loadProductsFromRemoteDatabase() = viewModelScope.launch(Dispatchers.IO) {
        val uid = auth.uid

        if (auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {

            _uiState.value = UiState.Loading()

            userProducts.clear()

            val cart = mutableListOf<CartObj>()

            fireRepository.getCartFromDatabase(uid)
                .catch { e ->
                    Log.d(TAG, "loadProductFromRemoteDatabase: $e ${e.message}")
                    _uiState.value = UiState.Failure(e)
                }.collect { cartObj ->
                    cart.add(cartObj)
                }

            cart.map { cartObj ->
                async {
                    roomRepository.insertCartProduct(
                        ProductWithAmount(
                            product = Product(id = cartObj.productId),
                            amountCash = cartObj.cashPriceAmount,
                            amountCashless = cartObj.cashlessPriceAmount
                        )
                    )
                }
            }.awaitAll()

            val findResult = cart.map { cartObj ->
                async {
                    val foundProduct = fireRepository.getProduct(cartObj.productId)
                    val foundProductWithAmount = ProductWithAmount(
                        product = foundProduct,
                        amountCash = cartObj.cashPriceAmount,
                        amountCashless = cartObj.cashlessPriceAmount
                    )
                    userProducts.add(foundProductWithAmount)
                    foundProduct != null
                }
            }.awaitAll().ifEmpty { listOf(true) }.all { it }

            if (_uiState.value is UiState.Loading) {
                    if (findResult)
                        _uiState.value = UiState.Success()
                    else
                        _uiState.value = UiState.Failure(Exception("smth not found"))
            }
        }
    }

    fun saveProductsToRemoteDatabase() = viewModelScope.launch(Dispatchers.IO) {
        val uid = auth.uid

        if (auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {
            _uiState.value = UiState.Loading()

            val deleteJob = launch {
                fireRepository.deleteCartFromDatabase(uid)
            }
            deleteJob.join()
            val saveResult = fireRepository.saveCartToDatabase(userProducts.toList(), uid)
            userProducts.clear()

            if (_uiState.value is UiState.Loading) {
                if (saveResult.isSuccess)
                    _uiState.value = UiState.Success()
                else
                    _uiState.value = UiState.Failure(saveResult.exceptionOrNull())
            }
        }
    }

}