package com.example.perfumeshop.data.repository

import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.room.LocalDao
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity
import javax.inject.Inject

class RoomRepository @Inject constructor(private val localDao: LocalDao) {

    // registration request table

    suspend fun addRegistrationRequest(registrationRequestEntity: RegistrationRequestEntity) =
        localDao.insertRegistrationRequest(registrationRequestEntity)

    suspend fun getRegistrationRequest(phoneNumber: String) =
        localDao.getRegistrationRequest(phoneNumber)

    suspend fun clearAllRegistrationRequests() = localDao.deleteAllRegistrationRequests()

    // cart

    fun getCartProducts() = localDao.getCartProducts()

    //suspend fun getCartProd(id: String) = localDao.getProd(id)

    suspend fun insertCartProduct(productWithAmount: ProductWithAmount) {
        productWithAmount.toCartProductEntity()?.let { cartProductEntity ->
            localDao.insertCartProduct(cartProductEntity)
        }
    }

    suspend fun updateCartProductAmount(id: String, cashAmount: Int, cashlessAmount: Int) =
        localDao.updateCartProductAmount(id, cashAmount, cashlessAmount)


    suspend fun deleteCartProduct(productWithAmount: ProductWithAmount) {
        productWithAmount.product?.id?.let { id ->
                localDao.deleteCartProduct(productId = id)
        }
    }

    suspend fun deleteAllInCart() = localDao.deleteAllInCart()

    // favourite

    fun getFavouriteProducts() = localDao.getFavoriteProducts()

    suspend fun insertFavoriteProduct(productWithAmount: ProductWithAmount) {
        productWithAmount.toFavouriteProductEntity()?.let { favouriteProductEntity ->
            localDao.insertFavoriteProduct(favouriteProductEntity)
        }
    }

    suspend fun updateFavouriteProductAmount(id: String, cashAmount: Int, cashlessAmount: Int) =
        localDao.updateFavouriteProductAmount(id, cashAmount, cashlessAmount)


    suspend fun deleteFavoriteProduct(productWithAmount: ProductWithAmount){
        productWithAmount.product?.id?.let { id ->
            localDao.deleteFavoriteProduct(id)
        }
    }

    suspend fun deleteAllInFavorites() = localDao.deleteAllInFavorites()

    // cast

    private fun ProductWithAmount.toCartProductEntity() : CartProductEntity? {
        val id = this.product?.id ?: return null
        return CartProductEntity(
            productId = id,
            amountCash = this.amountCash ?: 1,
            amountCashless = this.amountCashless ?: 1
        )
    }

    private fun ProductWithAmount.toFavouriteProductEntity() : FavouriteProductEntity? {
        val id = this.product?.id ?: return null
        return FavouriteProductEntity(
            productId = id,
            amountCash = this.amountCash ?: 1,
            amountCashless = this.amountCashless ?: 1
        )
    }
}