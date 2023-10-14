package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.models.Product
import com.example.perfumeshop.data.repositories.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "FavouriteViewModel"
@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

//    private var _userProducts = MutableStateFlow<MutableList<Product>>(value = mutableListOf())
//    var userProducts: StateFlow<List<Product>> = _userProducts

    val userProducts = SnapshotStateList<Product>()

    var isLoading  by mutableStateOf(false)
    var isSuccess  by mutableStateOf(false)
    var isFailure  by mutableStateOf(false)

    //var isCashPrice by mutableStateOf(true)


//    init {
//        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
//            loadUserProducts()
//    }

    fun clear(){
        super.onCleared()
    }

    override fun onCleared() {
        Log.d("VM_LFC_TEST", "onCleared: cartVM cleared")
        super.onCleared()
    }

    fun clearContent(){
        userProducts.clear()
    }

    fun addToFavourite(product: Product) = viewModelScope.launch {
        userProducts.add(product)
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null)
                repository.addFavouriteObj(product.id.toString(), uid)
        }
    }

    fun removeFromFavourite(productId: String) = viewModelScope.launch {
        userProducts.removeIf { p -> p.id == productId }
        FirebaseAuth.getInstance().uid.let { uid ->
            if (uid != null)
                repository.deleteFavouriteObj(productId, uid)
        }

    }

    fun isInFavourite(productId : String) =
        userProducts.find { p -> p.id!! == productId } != null

    fun loadUserProducts() {
        val auth = FirebaseAuth.getInstance()
        val id = auth.uid

        userProducts.clear()

        if (auth.currentUser?.isAnonymous != true && !id.isNullOrEmpty())
            viewModelScope.launch {
                isLoading = true
                isSuccess = false
                isFailure = false

                repository.getUserFavouriteProducts(id).catch { e ->
                        Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                        isFailure = true
                    }.collect { product ->
                        userProducts.add(product)
                    }
                isLoading = false
                isSuccess = !isFailure
            }

    }
}