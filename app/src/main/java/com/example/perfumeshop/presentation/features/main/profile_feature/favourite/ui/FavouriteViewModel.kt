package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui


import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.FavouriteObj
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Implements favourites functionality, which needed throughout the application.
That's why it is scoped to an App() composable.
 */

const val TAG = "FavouriteViewModel"

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository
) : ViewModel() {

    // if content changes, triggers recomposition.
    val userProducts = SnapshotStateList<ProductWithAmount>()

    private var _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState : StateFlow<UiState> = _uiState

    fun isInFavourites(productId : String) =
        userProducts.find { productWithAmount ->
            productWithAmount.product?.id == productId
        } != null

    fun addProduct(productWithAmount: ProductWithAmount) = viewModelScope.launch(Dispatchers.IO)  {
        userProducts.add(productWithAmount)
        roomRepository.insertFavoriteProduct(productWithAmount)
    }

    fun removeProduct(productWithAmount: ProductWithAmount) = viewModelScope.launch(Dispatchers.IO)  {
        userProducts.removeIf { productWA ->
            productWithAmount.product?.id == productWA.product?.id
        }
        roomRepository.deleteFavoriteProduct(productWithAmount)
    }

    fun updateProductAmount(ind: Int, cashAmount: Int, cashlessAmount: Int) = viewModelScope.launch(Dispatchers.IO)  {
        userProducts[ind].amountCash = cashAmount
        userProducts[ind].amountCashless = cashlessAmount
        userProducts[ind].product?.id?.let { id ->
            roomRepository.updateFavouriteProductAmount(id, cashAmount, cashlessAmount)
        }
    }

    fun clearData() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("WORK_TAG", "clearData: favs cleared")
        userProducts.clear()
        roomRepository.deleteAllInFavorites()
    }


    fun loadProductsFromRemoteDatabase() = viewModelScope.launch(Dispatchers.IO) {
        val uid = auth.uid

        if (auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {
            _uiState.value = UiState.Loading()

            userProducts.clear()

            val favs = mutableListOf<FavouriteObj>()

            fireRepository.getFavouritesFromDatabase(uid)
                .catch { e ->
                    Log.d(TAG, "loadProductFromRemoteDatabase: $e ${e.message}")
                    _uiState.value = UiState.Failure(e)
                }.collect { cartObj ->
                    favs.add(cartObj)
                }

            launch {
                roomRepository.deleteAllInFavorites()
            }.join()

            favs.map { favouriteObj ->
                launch {
                    roomRepository.insertFavoriteProduct(
                        ProductWithAmount(
                            product = Product(id = favouriteObj.productId),
                            amountCash = favouriteObj.cashPriceAmount ?: 1,
                            amountCashless = favouriteObj.cashlessPriceAmount ?: 1
                        )
                    )
                }
            }.joinAll()

            val findResult = favs.map { favouriteObj ->
                async {
                    val foundProduct = fireRepository.getProduct(favouriteObj.productId)
                    val foundProductWithAmount = ProductWithAmount(
                        product = foundProduct,
                        amountCash = favouriteObj.cashPriceAmount ?: 1,
                        amountCashless = favouriteObj.cashlessPriceAmount ?: 1
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
                fireRepository.deleteFavouritesFromDatabase(uid)
            }
            deleteJob.join()

            val saveResult = fireRepository.saveFavouritesToDatabase(userProducts.toList(), uid)
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