package com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.sql.Timestamp
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {
    private var _userProducts = MutableStateFlow<MutableList<Product>>(value = mutableListOf())
    var userProducts: StateFlow<List<Product>> = _userProducts


    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    init {
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
           loadUserProducts()
    }

    fun clear(){
        super.onCleared()
    }

    fun clearContent(){
        _userProducts.value.clear()
        userProducts = _userProducts
    }

    fun addToCart(product: Product) = viewModelScope.launch{
        _userProducts.value.add(product)
        userProducts = _userProducts
        updateCartInDatabase()
    }

    fun removeFromCart(productId : String) = viewModelScope.launch{
        _userProducts.value.removeIf { p -> p.id == productId }
        userProducts = _userProducts
        updateCartInDatabase()
    }

    fun isInCart(productId : String) : Boolean {
        var answer : Boolean = false
        viewModelScope.launch {
            answer = userProducts.value.find { product -> product.id!! == productId } != null
        }
        return answer
    }

    private fun updateCartInDatabase() = viewModelScope.launch {

        val review = Review(
            id = "FKDSJFDJGSDAFG",
            productId = "0DV00MAmkUfbe9rMNNzM",
            userId = FirebaseAuth.getInstance().uid,
            content = "OOOOOOOOOOOooooooooooooooooooooo",
            authorName = "Владислав Васильевич",
            rating = 3,
            date = Timestamp.valueOf("2018-09-01 09:01:15")
        )
        repository.saveToFirebase(review, "reviews")


//        repeat(10) {
//                repository.saveToFirebase(review, "reviews")
//                Log.d("REPEAT_TEST", "updateCartInDatabase: $it")
//            }



        val id = FirebaseAuth.getInstance().uid
        if (!id.isNullOrEmpty())
            repository.updateFieldInDatabase(
                collectionPath = "users",
                id = id,
                fieldPath = "cart",
                updatedValue = _userProducts.value.map { it.id }
            )
    }

    fun loadUserProducts() {
        val auth = FirebaseAuth.getInstance()
        val id = auth.uid


        if (auth.currentUser?.isAnonymous != true && !id.isNullOrEmpty())
            viewModelScope.launch {
                isFailure = false
                isLoading = true

                repository.getUserCartProducts(id).catch { e ->
                    Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                    isFailure = true
                }.collect { product ->
                    _userProducts.value.add(product)
                }

                userProducts = _userProducts

                isLoading = false

                if (_userProducts.value.isEmpty())
                    Log.d("ERROR_ERROR", "searchQuery: EMPTY")

                if (_userProducts.value.isEmpty() || isFailure)
                    isFailure = true
                else
                    isSuccess = true

            }
    }

}