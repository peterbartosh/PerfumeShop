package com.example.perfumeshop.data.skeleton

import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Singleton
class FavouriteFunctionality(private val roomRepository: RoomRepository) {

    private val scope = CoroutineScope(Job())

    fun isInFavourites(productId: String) = runBlocking(Dispatchers.IO) {
        roomRepository.isInFavourites(productId)
    }

    fun addProduct(productWithAmount: ProductWithAmount) = scope.launch(Dispatchers.IO)  {
        roomRepository.addFavoriteProduct(productWithAmount)
    }

    fun removeProduct(productWithAmount: ProductWithAmount) = scope.launch(Dispatchers.IO)  {
        roomRepository.deleteFavoriteProduct(productWithAmount)
    }

    fun updateProductAmount(id: String, cashAmount: Int, cashlessAmount: Int) = scope.launch(Dispatchers.IO) {
        roomRepository.updateFavouriteProductAmount(id, cashAmount, cashlessAmount)
    }

    fun clearData() = scope.launch(Dispatchers.IO) {
        roomRepository.deleteAllInFavorites()
    }
}