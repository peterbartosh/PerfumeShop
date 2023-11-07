package com.example.perfumeshop.data.repository

import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.room.LocalDao
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity
import com.example.perfumeshop.data.room.entities.UserDataEntity
import javax.inject.Inject

class RoomRepository @Inject constructor(private val localDao: LocalDao) {

    // user data

    suspend fun getUserData() =
        try {
            localDao.getUserData().first().toUser()
        } catch (e : Exception){
            User()
        }

    suspend fun initializeUserData(user: User) = user.toUserDataEntity()?.let { userDataEntity ->
        localDao.insertUserData(userDataEntity)
    }
    suspend fun updateUserData(user: User) = user.toUserDataEntity()?.let { userDataEntity ->
        localDao.updateUserData(userDataEntity)
    }
    suspend fun clearUserData() = localDao.deleteUserData()

    // registration requests

    suspend fun addRegistrationRequest(registrationRequestEntity: RegistrationRequestEntity) =
        localDao.insertRegistrationRequest(registrationRequestEntity)

    suspend fun getRegistrationRequest(phoneNumber: String) =
        localDao.getRegistrationRequest(phoneNumber)

    suspend fun clearAllRegistrationRequests() = localDao.deleteAllRegistrationRequests()

    // cart

    fun getCartProducts() = localDao.getCartProducts()

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

    // favourites

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

    private fun UserDataEntity.toUser() =
        User(
            id = this.id,
            firstName = this.firstName,
            secondName = this.secondName,
            phoneNumber = this.phoneNumber,
            home = this.home,
            street = this.street,
            flat = this.flat,
            sex = this.sex
        )

}