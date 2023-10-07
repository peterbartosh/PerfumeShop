package com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.ProductWithAmount
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {
//    private var _userProducts = MutableStateFlow<MutableList<ProductWithAmount>>(value = mutableListOf())
//    var userProducts: StateFlow<List<ProductWithAmount>> = _userProducts

    val userProducts = SnapshotStateList<ProductWithAmount>()

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)


//    init {
//        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
//           loadUserProducts()
//    }

    fun clear(){
        super.onCleared()
    }

    fun clearContent(){
        userProducts.clear()

    }

    fun addToCart(productWithAmount: ProductWithAmount) = viewModelScope.launch{
        userProducts.add(productWithAmount)
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null)
                repository.addCartObj(productWithAmount = productWithAmount, userId = uid)
        }
    }

    fun removeFromCart(productId : String) = viewModelScope.launch{
        userProducts.removeIf { productWithAmount -> productWithAmount.product?.id == productId }
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null)
                repository.deleteCartObj(productId = productId, userId = uid)
        }
    }

    fun updateProductAmountInCart(productIndex : Int, amount : Int) = viewModelScope.launch {
        userProducts[productIndex].amount = amount
        val productId = userProducts[productIndex].product?.id
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null && productId != null)
                repository.updateCartProductAmount(productId = productId, userId = uid, amount = amount)
        }
    }

    fun isInCart(productId : String) : Boolean {
        var answer = false
        viewModelScope.launch {
            answer = userProducts.find { productWithAmount ->
                productWithAmount.product?.id!! == productId } != null
        }
        return answer
    }

    fun loadUserProducts() {
        val auth = FirebaseAuth.getInstance()
        val id = auth.uid

        if (auth.currentUser?.isAnonymous != true && !id.isNullOrEmpty()) {
            viewModelScope.launch {
                isFailure = false
                isLoading = true

                repository.getUserCartProducts(id).catch { e ->
                    Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                    isFailure = true
                }.collect { productWithAmount ->
                    userProducts.add(productWithAmount)
                }

                isLoading = false

                if (!isFailure) isSuccess = true
            }
        }
    }

}