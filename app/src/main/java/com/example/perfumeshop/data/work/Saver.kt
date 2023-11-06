package com.example.perfumeshop.data.work

import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Saver(private val roomRepository: RoomRepository, private val fireRepository: FireRepository) {
    suspend fun saveCartAndFavs() {
        withContext(Dispatchers.IO){
            val auth = FirebaseAuth.getInstance()
            val uid = auth.uid

            if (auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {
                val cart = roomRepository.getCartProducts().map { cartProductEntity ->
                    ProductWithAmount(
                        product = Product(id = cartProductEntity.productId),
                        amountCash = cartProductEntity.amountCash,
                        amountCashless = cartProductEntity.amountCashless
                    )
                }
                val favs = roomRepository.getFavouriteProducts().map { favProductEntity ->
                    ProductWithAmount(
                        product = Product(id = favProductEntity.productId),
                        amountCash = favProductEntity.amountCash,
                        amountCashless = favProductEntity.amountCashless
                    )
                }

                val deleteJob = launch {
                    fireRepository.deleteCartFromDatabase(uid)
                    fireRepository.deleteFavouritesFromDatabase(uid)
                }

                deleteJob.join()

                val saveJob = launch {
                    fireRepository.saveCartToDatabase(cart, uid)
                    fireRepository.saveFavouritesToDatabase(favs, uid)
                }

                saveJob.join()
            }
        }
    }
}