package com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.data_layer.utils.updateFieldInDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private var _userProducts = MutableStateFlow<MutableList<Product>>(value = mutableListOf())
    var userProducts: StateFlow<List<Product>> = _userProducts


    var isLoading  by mutableStateOf(false)
    var isSuccess  by mutableStateOf(false)
    var isFailure  by mutableStateOf(false)
    //var isInitialized  by mutableStateOf(false)

    //var initSearchQuery by mutableStateOf(true)

    init {
        loadUserProducts()
    }

    fun clear(){
        super.onCleared()
    }

    fun addToFavourite(product: Product){
        _userProducts.value.add(product)
        userProducts = _userProducts
        updateFavouriteInDatabase()

        //Log.d("USER_PRODS_TEST", userProducts.value.toString())
    }

    fun removeFromFavourite(productId: String){
        _userProducts.value.removeIf { p -> p.id == productId }
        userProducts = _userProducts
        updateFavouriteInDatabase()
        //Log.d("USER_PRODS_TEST", userProducts.value.toString())

    }

    fun isInFavourite(productId : String) : Boolean =
        _userProducts.value.find { p -> p.id!! == productId } != null

    fun updateFavouriteInDatabase() = viewModelScope.launch {
        val id = FirebaseAuth.getInstance().uid
        if (!id.isNullOrEmpty())
            updateFieldInDatabase(
                collectionPath = "users",
                id = id,
                fieldPath = "favourite",
                updatedValue = _userProducts.value.map { it.id }
            )
    }

    private fun loadUserProducts() {
        val auth = FirebaseAuth.getInstance()
        val id = auth.uid

        if (auth.currentUser?.isAnonymous != true && !id.isNullOrEmpty())
            viewModelScope.launch {
                isFailure = false
                isLoading = true

                repository.getUserFavouriteProducts(id).catch { e ->
                        Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                        isFailure = true
                    }.collect { product ->
                        _userProducts.value.add(product)
                    }

                userProducts = _userProducts

                isLoading = false

                if (_userProducts.value.isEmpty() || isFailure)
                    isFailure = true
                else
                    isSuccess = true

            }
    }
}