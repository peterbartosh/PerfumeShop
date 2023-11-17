package com.example.perfumeshop.data.skeleton

import android.util.Log
import com.example.perfumeshop.data.model.CartObj
import com.example.perfumeshop.data.model.FavouriteObj
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.room.entities.ProductEntity
import com.example.perfumeshop.data.room.entities.toProductWithAmount
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class DataManager(
    private val roomRepository: RoomRepository,
    private val fireRepository: FireRepository
) {

    private val scope = CoroutineScope(Job())

    private fun <T: ProductEntity> upsertLocalDatabase(
        productsWithAmount: List<ProductWithAmount>,
        localProducts: List<T>,
        updateProductEntityIsOnHand: suspend (String, Boolean) -> Unit,
        updateProductEntityAmounts: suspend (String, Double, Double) -> Unit
    ) = scope.launch {

        for (productWithAmount in productsWithAmount){
            val productId = productWithAmount.product?.id ?: return@launch
            val isOnHand = productWithAmount.product?.isOnHand ?: return@launch
            val cashPrice = productWithAmount.product?.cashPrice ?: return@launch
            val cashlessPrice = productWithAmount.product?.cashlessPrice ?: return@launch

            localProducts.find { it.productId == productId }?.let { productEntity ->

                if (productWithAmount.product?.isOnHand != productEntity.isOnHand)
                    updateProductEntityIsOnHand(productId, isOnHand)

                if (cashPrice != productEntity.cashPrice || cashlessPrice != productEntity.cashlessPrice)
                    updateProductEntityAmounts(productId, cashPrice, cashlessPrice)

            }
        }
    }

    fun loadProductsFromRemoteDatabase() = scope.launch {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.uid

        if (auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {
            val favs = mutableListOf<FavouriteObj>()
            val cart = mutableListOf<CartObj>()

            fireRepository.getFavouritesFromDatabase(uid)
                .catch { e ->
                    Log.d(TAG, "loadProductFromRemoteDatabase: $e")
                }.collect { favObj ->
                    favs.add(favObj)
                }

            fireRepository.getCartFromDatabase(uid)
                .catch { e ->
                    Log.d(TAG, "loadProductFromRemoteDatabase: $e")
                }.collect { cartObj ->
                    cart.add(cartObj)
                }

            val foundFavsProducts = favs.map { favouriteObj ->
                async {
                    fireRepository.getProduct(favouriteObj.productId)?.let { foundProduct ->
                        ProductWithAmount(
                            product = foundProduct,
                            cashPriceAmount = favouriteObj.cashPriceAmount ?: 1,
                            cashlessPriceAmount = favouriteObj.cashlessPriceAmount ?: 1
                        )
                    }
                }
            }.awaitAll().filterNotNull()

            val foundCartProducts = cart.map { cartObj ->
                async {
                    fireRepository.getProduct(cartObj.productId)?.let { foundProduct ->
                        ProductWithAmount(
                            product = foundProduct,
                            cashPriceAmount = cartObj.cashPriceAmount ?: 1,
                            cashlessPriceAmount = cartObj.cashlessPriceAmount ?: 1
                        )
                    }
                }
            }.awaitAll().filterNotNull()

            val localFavs = async {
                roomRepository.getFavouriteProductsAsList()
            }.await()
            val localCart = async {
                roomRepository.getCartProductsAsList()
            }.await()

            upsertLocalDatabase(
                foundFavsProducts,
                localFavs,
                roomRepository::updateFavouriteProductIsOnHand,
                roomRepository::updateFavouriteProductPrices
            )

            upsertLocalDatabase(
                foundCartProducts,
                localCart,
                roomRepository::updateCartProductIsOnHand,
                roomRepository::updateCartProductPrices
            )
        }
    }

    fun saveProductsToRemote() = scope.launch {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.uid

        if (auth.currentUser?.isAnonymous != true && !uid.isNullOrEmpty()) {

            val favProducts = async {
                roomRepository.getFavouriteProductsAsList().map { it.toProductWithAmount() }
            }.await()

            val cartProducts = async {
                roomRepository.getCartProductsAsList().map { it.toProductWithAmount() }
            }.await()

            fireRepository.saveCartToDatabase(cartProducts, uid)
            fireRepository.saveFavouritesToDatabase(favProducts, uid)

        }
    }
}