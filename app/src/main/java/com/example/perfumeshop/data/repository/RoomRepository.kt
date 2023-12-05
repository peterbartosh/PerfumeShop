package com.example.perfumeshop.data.repository

import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.room.LocalDao
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity
import com.example.perfumeshop.data.room.entities.UserDataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepository @Inject constructor(private val localDao: LocalDao) {

    // user data

    suspend fun getUserData() = localDao.getUserData()

    suspend fun initializeUserData(user: User) = withContext(Dispatchers.IO){
        user.toUserDataEntity()?.let { userDataEntity ->
            localDao.insertUserData(userDataEntity)
        }
    }
    suspend fun updateUserData(user: User) = withContext(Dispatchers.IO) {
        user.toUserDataEntity()?.let { userDataEntity ->
            localDao.updateUserData(userDataEntity)
        }
    }
    suspend fun clearUserData() = withContext(Dispatchers.IO){
        localDao.deleteUserData()
    }

    // registration requests

    suspend fun addRegistrationRequest(
        registrationRequestEntity: RegistrationRequestEntity
    ) = withContext(Dispatchers.IO) {
        localDao.insertRegistrationRequest(registrationRequestEntity)
    }
    suspend fun getRegistrationRequest(phoneNumber: String) = withContext(Dispatchers.IO) {
        localDao.getRegistrationRequest(phoneNumber)
    }

    suspend fun clearAllRegistrationRequests() = withContext(Dispatchers.IO){
        localDao.deleteAllRegistrationRequests()
    }

    // cart

    fun getCartProducts() = localDao.getCartProducts().flowOn(Dispatchers.IO)

    suspend fun getCartProductsAsList() = withContext(Dispatchers.IO){
        localDao.getCartProductsAsList()
    }

    fun getCartProductsAmount() = localDao.getCartProductsAmount().flowOn(Dispatchers.IO)

    suspend fun isInCart(productId: String) = withContext(Dispatchers.IO){
        localDao.isInCart(productId) == 1
    }

    suspend fun addCartProduct(productWithAmount: ProductWithAmount) = withContext(Dispatchers.IO){
        productWithAmount.toCartProductEntity()?.let { cartProductEntity ->
            localDao.insertCartProduct(cartProductEntity)
        }
    }

    suspend fun addAllInCart(productWithAmounts: List<ProductWithAmount>) = withContext(Dispatchers.IO){
        localDao.insertAllInCart(
            productWithAmounts.mapNotNull { it.toCartProductEntity() }
        )
    }

    suspend fun updateCartProductAmount(
        id: String,
        cashAmount: Int,
        cashlessAmount: Int
    ) = withContext(Dispatchers.IO) {
        localDao.updateCartProductAmount(id, cashAmount, cashlessAmount)
    }

    suspend fun updateCartProductIsOnHand(
        productId: String,
        isOnHand: Boolean
    ) = withContext(Dispatchers.IO){
        localDao.updateCartProductIsOnHand(productId, isOnHand)
    }

    suspend fun updateCartProductPrices(
        productId: String,
        cashPrice: Double,
        cashlessPrice: Double
    ) = withContext(Dispatchers.IO){
        localDao.updateCartProductPrices(productId, cashPrice, cashlessPrice)
    }


    suspend fun deleteCartProduct(productWithAmount: ProductWithAmount) = withContext(Dispatchers.IO){
        productWithAmount.product?.id?.let { id ->
                localDao.deleteCartProduct(productId = id)
        }
    }

    suspend fun deleteAllInCart() = withContext(Dispatchers.IO) {
        localDao.deleteAllInCart()
    }
    // favourites

    fun getFavouriteProducts() = localDao.getFavoriteProducts().flowOn(Dispatchers.IO)

    suspend fun getFavouriteProductsAsList() = withContext(Dispatchers.IO){
        localDao.getFavouriteProductsAsList()
    }

    suspend fun isInFavourites(productId: String) = withContext(Dispatchers.IO){
        localDao.isInFavourites(productId) == 1
    }

    suspend fun addFavoriteProduct(productWithAmount: ProductWithAmount) = withContext(Dispatchers.IO){
        productWithAmount.toFavouriteProductEntity()?.let { favouriteProductEntity ->
            localDao.insertFavoriteProduct(favouriteProductEntity)
        }
    }

    suspend fun addAllInFavourites(productWithAmounts: List<ProductWithAmount>) = withContext(Dispatchers.IO){
        localDao.insertAllInFavorites(
            productWithAmounts.mapNotNull { it.toFavouriteProductEntity() }
        )
    }

    suspend fun updateFavouriteProductAmount(
        id: String,
        cashAmount: Int,
        cashlessAmount: Int
    ) = withContext(Dispatchers.IO) {
        localDao.updateFavouriteProductAmount(id, cashAmount, cashlessAmount)
    }

    suspend fun updateFavouriteProductIsOnHand(
        productId: String,
        isOnHand: Boolean
    ) = withContext(Dispatchers.IO){
        localDao.updateFavouriteProductIsOnHand(productId, isOnHand)
    }

    suspend fun updateFavouriteProductPrices(
        productId: String,
        cashPrice: Double,
        cashlessPrice: Double
    ) = withContext(Dispatchers.IO){
        localDao.updateFavouriteProductPrices(productId, cashPrice, cashlessPrice)
    }

    suspend fun deleteFavoriteProduct(productWithAmount: ProductWithAmount) = withContext(Dispatchers.IO){
        productWithAmount.product?.id?.let { id ->
            localDao.deleteFavoriteProduct(id)
        }
    }

    suspend fun deleteAllInFavorites() = withContext(Dispatchers.IO) {
        localDao.deleteAllInFavorites()
    }

    // cast

    private fun ProductWithAmount.toCartProductEntity() : CartProductEntity? {
        val id = this.product?.id ?: return null
        return CartProductEntity(
            productId = id,
            type = product?.type,
            brand = product?.brand,
            volume = product?.volume,
            cashPrice = product?.cashPrice,
            cashlessPrice = product?.cashlessPrice,
            isOnHand = product?.isOnHand,
            cashPriceAmount = cashPriceAmount ?: 1,
            cashlessPriceAmount = cashlessPriceAmount ?: 1
        )
    }

    private fun ProductWithAmount.toFavouriteProductEntity() : FavouriteProductEntity? {
        val id = this.product?.id ?: return null
        return FavouriteProductEntity(
            productId = id,
            type = product?.type,
            brand = product?.brand,
            volume = product?.volume,
            cashPrice = product?.cashPrice,
            cashlessPrice = product?.cashlessPrice,
            isOnHand = product?.isOnHand,
            cashPriceAmount = cashPriceAmount ?: 1,
            cashlessPriceAmount = cashlessPriceAmount ?: 1
        )
    }

    private fun User.toUserDataEntity() : UserDataEntity? {
        val id = this.id ?: return null
        return UserDataEntity(
            id = id,
            firstName = this.firstName,
            secondName = this.secondName,
            phoneNumber = this.phoneNumber,
            home = this.home,
            street = this.street,
            flat = this.flat,
            sex = this.sex
        )
    }
}