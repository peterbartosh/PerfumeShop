package com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.google.firebase.auth.FirebaseAuth
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

    //var isCashPrice by mutableStateOf(true)


    init {
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
            loadUserProducts()
    }

    fun clear(){
        super.onCleared()
    }

    override fun onCleared() {
        Log.d("VM_LFC_TEST", "onCleared: cartVM cleared")
        super.onCleared()
    }

    fun clearContent(){
        _userProducts.value.clear()
        userProducts = _userProducts
    }

    fun addToFavourite(product: Product) = viewModelScope.launch {
        _userProducts.value.add(product)
        //userProducts = _userProducts
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null)
                repository.addFavouriteObj(product.id.toString(), uid)
        }

        //Log.d("USER_PRODS_TEST", userProducts.value.toString())
    }

    fun removeFromFavourite(productId: String) = viewModelScope.launch {
        _userProducts.value.removeIf { p -> p.id == productId }
        //userProducts = _userProducts
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null)
                repository.deleteFavouriteObj(productId, uid)
        }

    }

    fun isInFavourite(productId : String) : Boolean =
        _userProducts.value.find { p -> p.id!! == productId } != null

    fun loadUserProducts() {
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

                if (!isFailure) isSuccess = true

                //Log.d("ERROR_ERROR", "loadUserProducts: $isSuccess $isFailure $isLoading")

            }
    }
}