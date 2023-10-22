package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "FavouriteViewModel"
@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private val _auth = FirebaseAuth.getInstance()

    val userProducts = SnapshotStateList<ProductWithAmount>()

    var isLoading  by mutableStateOf(false)
    var isSuccess  by mutableStateOf(false)
    var isFailure  by mutableStateOf(false)


    fun clearContent(){
        userProducts.clear()
    }

    fun updateProductAmount(ind : Int, amount : Int){
        userProducts[ind].amount = amount
    }

    fun updateProductCashState(ind : Int, isCashPrice : Boolean){
        userProducts[ind].isCashPrice = isCashPrice
    }

    fun addToFavourite(productWithAmount: ProductWithAmount) = viewModelScope.launch {
        isLoading = true
        userProducts.add(productWithAmount)
        _auth.uid.let { uid ->
            if (uid != null)
                repository.addFavouriteObj(productWithAmount, uid)?.let { result ->
                    //
                }
        }
        isLoading = false
    }

    fun removeFromFavourite(productId: String) = viewModelScope.launch {
        isLoading = true
        userProducts.removeIf { productWithAmount -> productWithAmount.product?.id == productId }
        _auth.uid?.let { uid ->
             repository.deleteFavouriteObj(productId, uid)?.let { result ->
                 //
             }
        }
        isLoading = false
    }

    fun isInFavourite(productId : String) : Boolean {
        isLoading = true
        val result = userProducts.find { productWithAmount ->
            productWithAmount.product?.id == productId
        } != null
        isLoading = false
        return result
    }

    fun loadUserProducts() {
        val uid = _auth.uid

        userProducts.clear()

        if (_auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty())
            viewModelScope.launch {
                isLoading = true
                isSuccess = false
                isFailure = false

                repository.getUserFavouriteProducts(uid).catch { e ->
                        Log.d(TAG, "searchQuery: ${e.message}")
                        isFailure = true
                    }.collect { productWithAmount ->
                        userProducts.add(productWithAmount)
                    }
                isLoading = false
                isSuccess = !isFailure
            }

    }
}