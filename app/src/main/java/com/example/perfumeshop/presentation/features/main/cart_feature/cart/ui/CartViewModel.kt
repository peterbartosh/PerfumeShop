package com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.TAG
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private val _auth = FirebaseAuth.getInstance()

    val userProducts = SnapshotStateList<ProductWithAmount>()

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)


    fun clearContent(){
        userProducts.clear()
    }

    fun addToCart(productWithAmount: ProductWithAmount) = viewModelScope.launch{
        isLoading = true
        userProducts.add(productWithAmount)
        _auth.uid?.let { uid ->
            repository.addCartObj(productWithAmount = productWithAmount, userId = uid).let { result ->
                //
            }
        }
        isLoading = false
    }

    fun removeFromCart(productId : String) = viewModelScope.launch{
        isLoading = true
        userProducts.removeIf { productWithAmount -> productWithAmount.product?.id == productId }
        _auth.uid?.let { uid ->
            repository.deleteCartObj(productId = productId, userId = uid).let { result ->
                //
            }
        }
        isLoading = false
    }

    fun updateProductAmountInCart(productIndex : Int, amount : Int) = viewModelScope.launch {
        isLoading = true
        try {
            userProducts[productIndex].amount = amount

            userProducts[productIndex].product?.id?.let { productId ->
                _auth.uid?.let { uid ->
                    try {
                        repository.updateCartProductAmount(
                            productId = productId,
                            userId = uid,
                            amount = amount
                        )?.let { result ->
                            //
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "updateProductCashStateInCart: ${e.message}")
                    }
                }
            }
        } catch (e : Exception){
            Log.d(TAG, "updateProductCashStateInCart: ${e.message}")
        }
        isLoading = false
    }

    fun updateProductCashStateInCart(productIndex : Int, isCashPrice : Boolean) = viewModelScope.launch {
        isLoading = true
        try {

            userProducts[productIndex].isCashPrice = isCashPrice

            userProducts[productIndex].product?.id?.let { productId ->
                _auth.uid?.let { uid ->
                    try {
                        repository.updateCartProductCashState(
                            productId = productId,
                            userId = uid,
                            isCashPrice = isCashPrice
                        )?.let { result ->
                            //
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "updateProductCashStateInCart: ${e.message}")
                    }
                }
            }
        } catch (e : Exception){
            Log.d(TAG, "updateProductCashStateInCart: ${e.message}")
        }
        isLoading = false
    }

    fun isInCart(productId : String) : Boolean {
        isLoading = true
        val result = userProducts.find { productWithAmount ->
            productWithAmount.product?.id == productId
        } != null
        isLoading = false
        return result
    }

    fun loadUserProducts() {

        userProducts.clear()

        val uid = _auth.uid
        if (_auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {
            viewModelScope.launch {
                isFailure = false
                isLoading = true

                repository.getUserCartProducts(uid).catch { e ->
                    Log.d(TAG, "updateCartProductAmount: isSuccess = false, exception = $e")
                    isFailure = true
                }.collect { productWithAmount ->
                    userProducts.add(productWithAmount)
                }

                isLoading = false

                isSuccess = !isFailure
            }
        }
    }

}