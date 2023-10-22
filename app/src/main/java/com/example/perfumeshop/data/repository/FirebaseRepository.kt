package com.example.perfumeshop.data.repository

import android.util.Log
import com.example.perfumeshop.data.model.CartObj
import com.example.perfumeshop.data.model.FavouriteObj
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.OrderProduct
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.model.Review
import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.utils.QueryType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val TAG = "FireRepository"

class FireRepository @Inject constructor(
        private val productsCollection: CollectionReference,
        private val usersCollection: CollectionReference,
        private val cartCollection: CollectionReference,
        private val favouriteCollection: CollectionReference,
        private val ordersCollection: CollectionReference,
        private val ordersProductsCollection: CollectionReference,
        private val blackListCollection: CollectionReference,
        private val reviewsCollection: CollectionReference
    ) {

    private suspend inline fun <reified T> Query.queryToFlow(methodNane : String) : Flow<T> {
        return this.get()
            .addOnCompleteListener { task ->
                Log.d(TAG, "$methodNane: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }
            .await().toObjects(T::class.java).asFlow()
    }

    // -------------------------------------------------------------
    // common save operation
    suspend fun saveToFirebase(
        item: Any,
        collectionName: String,
        updateId : Boolean = true,
    ) : Result<String>? {

        var result : Result<String>? = null

        val doc = FirebaseFirestore.getInstance().collection(collectionName)
            .add(item)
            .addOnCompleteListener{ task ->
                result = if (task.isSuccessful)
                    Result.success("")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "getProduct: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }.await()

        if (!updateId) {
            Log.d("FINAL_LOG", "getProduct: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()}")
            return if (result?.exceptionOrNull() == null) Result.success("") else result
        }

        doc.update("id", doc.id)
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success(doc.id)
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "getProduct: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }.await()

//        if (result?.exceptionOrNull()?.message == "null exception")
//            result = Result.success(doc.id)

        Log.d("FINAL_LOG", "getProduct: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()}")

        return if (result?.exceptionOrNull() == null) Result.success(doc.id) else result
    }

    // -------------------------------------------------------------
    // products collection operations
    suspend fun getProduct(productId: String?) : Product? {
        return if (!productId.isNullOrEmpty()) {
            val snapshot = productsCollection.document(productId)
                .get()
                .addOnCompleteListener { task ->
                    Log.d(TAG, "getProduct: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
                }
                .await()
            if (snapshot.exists()) snapshot.toObject(Product::class.java) else null
        } else null
    }

    suspend fun getQueryProducts(
        query : String,
        queryType: QueryType,
        productsPerPage : Int,
        uploadsAmount : Int
    ) : Flow<Product> {

        val resultQuery = if (queryType == QueryType.brand)
            productsCollection
                .orderBy(queryType.name)
                .startAt(query.lowercase())
                .endAt(query.lowercase() + '\uf8ff')
        else
            productsCollection.whereEqualTo(queryType.name, query)

        return resultQuery.limit(((uploadsAmount + 1) * productsPerPage).toLong()).queryToFlow<Product>("getQueryProducts").drop(uploadsAmount * productsPerPage)
    }

    // -------------------------------------------------------------
    // users collection operations
    suspend fun createUser(user : User) : Result<String>? {
        var result : Result<String>? = null
        user.id?.let { uid ->
            usersCollection.document(uid)
                .set(user)
                .addOnCompleteListener { task ->
                    result = if (task.isSuccessful)
                        Result.success("")
                    else
                        Result.failure(task.exception ?: Exception("null exception"))
                    Log.d(TAG, "createUser: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
                }
                .await()
        }
        return result
    }

    // -------------------------------------------------------------
    // cart collection operations
    suspend fun addCartObj(productWithAmount : ProductWithAmount, userId : String) : Result<String>? {
        var result : Result<String>? = null
        cartCollection.document(userId + "|" + productWithAmount.product?.id.toString()).set(
            CartObj(
                userId = userId,
                productId = productWithAmount.product?.id,
                amount = productWithAmount.amount,
                isCashPrice = productWithAmount.isCashPrice
            )
        ).addOnCompleteListener { task ->
            result = if (task.isSuccessful)
                Result.success("added")
            else
                Result.failure(task.exception ?: Exception("null exception"))

            Log.d(TAG, "addCartObj: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
        }.await()
        return result
    }

    suspend fun deleteCartObj(productId: String, userId: String) : Result<String>? {
        var result : Result<String>? = null

        cartCollection.document("$userId|$productId").delete()
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("deleted")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "deleteCartObj: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")
            }.await()

        return result
    }

    suspend fun updateCartProductAmount(productId: String, userId: String, amount: Int) : Result<String>? {
        var result : Result<String>? = null

        cartCollection.document("$userId|$productId")
            .update("amount", amount)
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("updated")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "updateCartProductAmount: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")
            }.await()

        return result
    }

    suspend fun updateCartProductCashState(productId: String, userId: String, isCashPrice: Boolean) : Result<String>? {
        var result : Result<String>? = null

        cartCollection.document("$userId|$productId")
            .update("is_cash_price", isCashPrice)
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("updated")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "updateCartProductCashState: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")
            }.await()

        return result
    }

    suspend fun clearCart(userId : String?) : Result<String>? {

        var result : Result<String>? = null

        val documentsIds = cartCollection.whereEqualTo("user_id", userId)
            .get().await().documents.map { it.id }

        documentsIds.forEach { id ->
            cartCollection.document(id).delete()
                .addOnCompleteListener{ task ->
                    result = if (task.isSuccessful)
                        Result.success("cleared")
                    else
                        Result.failure(task.exception ?: Exception("null exception"))

                    Log.d(TAG, "clearCart: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")
                }.await()
        }
        return result
    }

    suspend fun getUserCartProducts(userId : String) : Flow<ProductWithAmount> {

        val cart = cartCollection.whereEqualTo("user_id", userId)
            .get()
            .addOnCompleteListener { task ->
                Log.d(TAG, "getUserCartProducts: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }
            .await()
            .toObjects(CartObj::class.java)

        val map = buildMap<String, CartObj> {
            cart.forEach { cartObj ->
                put(cartObj.productId.toString(), cartObj)
            }
        }

        val productIds = map.keys.toList()

        val final =
            if (productIds.isNotEmpty())
                productsCollection.whereIn("id", productIds)
                    .get()
                    .addOnCompleteListener { task ->
                        Log.d(TAG, "getUserCartProducts: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
                    }
                    .await()
                    .toObjects(Product::class.java)
                    .mapNotNull { product ->
                        val id = product.id
                        if (id != null)
                            ProductWithAmount(
                                product = product,
                                amount = map[id]?.amount,
                                isCashPrice = map[id]?.isCashPrice
                            )
                        else null
                    }
            else listOf()

        return final.asFlow()
    }

    // -------------------------------------------------------------
    // favourite collection operations
    suspend fun addFavouriteObj(productWithAmount: ProductWithAmount, userId: String) : Result<String>? {
        var result : Result<String>? = null

        favouriteCollection.document("$userId|${productWithAmount.product?.id.toString()}")
            .set(
                FavouriteObj(
                    userId = userId,
                    productId = productWithAmount.product?.id,
                    amount = productWithAmount.amount,
                    isCashPrice = productWithAmount.isCashPrice
                )
            )
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("added")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "addFavouriteObj: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")

            }.await()

        return result
    }

    suspend fun deleteFavouriteObj(productId: String, userId: String) : Result<String>? {

        var result : Result<String>? = null

        favouriteCollection.document("$userId|$productId")
            .delete()
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("cleared")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(TAG, "deleteFavouriteObj: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")
            }.await()

        return result
    }

    suspend fun clearFavourites(userId : String?) : Result<String>? {

        var result : Result<String>? = null

        val documentsIds = favouriteCollection.whereEqualTo("user_id", userId)
            .get().await().documents.map { it.id }

        documentsIds.forEach { id ->
            favouriteCollection.document(id).delete()
                .addOnCompleteListener{ task ->
                    result = if (task.isSuccessful)
                        Result.success("cleared")
                    else
                        Result.failure(task.exception ?: Exception("null exception"))

                    Log.d(TAG, "clearCart: isSuccess = ${result?.isSuccess}, exceptionMessage = ${result?.exceptionOrNull()?.message}")
                }.await()
        }
        return result
    }

    suspend fun getUserFavouriteProducts(userId : String) : Flow<ProductWithAmount> {

        val favourite = favouriteCollection.whereEqualTo("user_id", userId)
            .get()
            .addOnCompleteListener { task ->
                Log.d(TAG, "getUserFavouriteProducts: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }
            .await()
            .toObjects(FavouriteObj::class.java)

        val map = buildMap<String, FavouriteObj> {
            favourite.forEach { favouriteObj ->
                put(favouriteObj.productId.toString(), favouriteObj)
            }
        }

        val productIds = map.keys.toList()

        val final =
            if (productIds.isNotEmpty())
                productsCollection.whereIn("id", productIds)
                    .get()
                    .addOnCompleteListener { task ->
                        Log.d(TAG, "getUserCartProducts: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
                    }
                    .await()
                    .toObjects(Product::class.java)
                    .mapNotNull { product ->
                        val id = product.id
                        if (id != null)
                            ProductWithAmount(
                                product = product,
                                amount = map[id]?.amount,
                                isCashPrice = map[id]?.isCashPrice
                            )
                        else null
                    }
            else listOf()

        return final.asFlow()
    }

    // -------------------------------------------------------------
    // orders collection operations

    suspend fun getUserOrders() : Flow<Order> {
        return ordersCollection
            .whereEqualTo("user_id", FirebaseAuth.getInstance().uid)
            .queryToFlow("getUserOrders")
    }

    // -------------------------------------------------------------
    // orders_products collection operations
    suspend fun getOrderProducts(orderId : String) : List<OrderProduct> {
        return ordersProductsCollection
            .whereEqualTo("order_id", orderId)
            .get()
            .addOnCompleteListener { task ->
                Log.d(TAG, "getOrderProducts: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }.await().toObjects(OrderProduct::class.java)
    }

    // -------------------------------------------------------------
    // black_list collection operations
    suspend fun isInBlackList(phoneNumber: String) : Boolean {
        val exists = blackListCollection.document(phoneNumber)
            .get().await().exists()
        Log.d(TAG, "isInBlackList: exists = $exists")
        return exists
    }

    // -------------------------------------------------------------
    // reviews collection operations
    suspend fun getProductReviews(productId : String) : Flow<Review> {
        return reviewsCollection.whereEqualTo("product_id", productId).queryToFlow("getProductReviews")
    }
}