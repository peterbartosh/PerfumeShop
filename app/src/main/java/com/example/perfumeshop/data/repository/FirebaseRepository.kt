package com.example.perfumeshop.data.repository

import android.util.Log
import com.example.perfumeshop.data.model.CartObj
import com.example.perfumeshop.data.model.FavouriteObj
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.OrderProduct
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.model.User
import com.example.perfumeshop.data.utils.QueryType
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "FireRepository"

class FireRepository @Inject constructor(
        private val productsCollection: CollectionReference,
        private val usersCollection: CollectionReference,
        private val cartCollection: CollectionReference,
        private val favouriteCollection: CollectionReference,
        private val ordersCollection: CollectionReference,
        private val ordersProductsCollection: CollectionReference,
        private val blackListCollection: CollectionReference
) {

    private var _filter : Filter? = null

    companion object {
        suspend fun isAppBlocked() = try {
            withContext(Dispatchers.IO) {
                FirebaseFirestore.getInstance()
                    .collection("main_app_blocker")
                    .document("blocker_id")
                    .get().await().getBoolean("is_blocked_for_maintenance")
            }
        } catch (e : Exception){ false }
    }

    private suspend inline fun <reified T> Query.queryToFlow(methodNane : String) : Flow<T> {
        return this.get()
            .addOnCompleteListener { task ->
                Log.d(TAG, "$methodNane: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
            }
            .await().toObjects(T::class.java).asFlow()
    }

    // -------------------------------------------------------------
    // common operations

    suspend fun updateInDatabase(
        docId: String?,
        collectionName: String,
        fieldToUpdate: String,
        newValue: Any
    ) : Result<String>? = try {
        withContext(Dispatchers.IO) {
            var result: Result<String>? = null

            if (docId == null) Result.failure(NullPointerException("id == null"))
            else {
                FirebaseFirestore.getInstance().collection(collectionName)
                    .document(docId).update(fieldToUpdate, newValue)
                    .addOnCompleteListener { task ->
                        result = if (task.isSuccessful) Result.success("s")
                        else Result.failure(task.exception ?: Exception("null exception"))
                    }.await()

                result
            }
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    suspend fun saveToDatabase(
        item: Any,
        collectionName: String,
        updateId : Boolean = true,
    ) : Result<String>? = try {
        withContext(Dispatchers.IO) {
            var result: Result<String>? = null

            val doc = FirebaseFirestore.getInstance().collection(collectionName)
                .add(item)
                .addOnCompleteListener { task ->
                    result = if (task.isSuccessful)
                        Result.success("")
                    else
                        Result.failure(task.exception ?: Exception("null exception"))
                }.await()

            if (!updateId) {
                if (result?.exceptionOrNull() == null) Result.success("") else result
            } else {

                doc.update("id", doc.id)
                    .addOnCompleteListener { task ->
                        result = if (task.isSuccessful)
                            Result.success(doc.id)
                        else
                            Result.failure(task.exception ?: Exception("null exception"))
                    }.await()

                if (result?.exceptionOrNull() == null) Result.success(doc.id) else result
            }
        }
    } catch (e : Exception){
        Result.failure(e)
    }


    // -------------------------------------------------------------
    // products collection operations
    suspend fun getProduct(productId: String?) : Product? = try {
        withContext(Dispatchers.IO) {
            if (!productId.isNullOrEmpty()) {
                val snapshot = productsCollection.document(productId)
                    .get()
                    .addOnCompleteListener { task ->
                        Log.d(
                            TAG,
                            "getProduct: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}"
                        )
                    }
                    .await()
                if (snapshot.exists()) snapshot.toObject(Product::class.java) else null
            } else null
        }
    } catch (e : Exception){
        null
    }

    fun constructFilter(
        isOnHandOnly: Boolean,
        volumes: List<Double>
    ){
        val filters = mutableListOf<Filter>()

        if (isOnHandOnly)
            filters.add(Filter.equalTo("is_on_hand", true))
        if (volumes.isNotEmpty())
            filters.add(Filter.inArray("volume", volumes))

        _filter = if (filters.isNotEmpty())
            Filter.and(*filters.toTypedArray())
        else
            null
    }

    suspend fun getQueryProducts(
        query : String,
        queryType: QueryType,
        initQuery: String,
        initQueryType: QueryType,
        priorities : List<Int>,
        isAscending : Boolean,
        productsPerPage : Int,
        uploadsAmount : Int
    ) : Flow<Product> = try {

        withContext(Dispatchers.IO) {

            var resultQuery = productsCollection as Query

            if (initQueryType.name == QueryType.type.name)
                resultQuery = resultQuery.whereEqualTo("type", initQuery)

            _filter?.let { filter ->
                resultQuery = resultQuery.where(filter)
            }

            if (queryType.name != QueryType.brand.name) {
                val direction =
                    if (isAscending) Query.Direction.ASCENDING else Query.Direction.DESCENDING

                when (priorities.joinToString { it.toString() }) {
                    "0" -> resultQuery = resultQuery.orderBy("cash_price", direction)

                    "1" -> resultQuery = resultQuery.orderBy("volume", direction)

                    "01" -> resultQuery = resultQuery
                        .orderBy("cash_price", direction)
                        .orderBy("volume", direction)

                    "10" -> resultQuery = resultQuery
                        .orderBy("volume", direction)
                        .orderBy("cash_price", direction)

                    else -> {}
                }
            }

            if (queryType.name == QueryType.brand.name) {
                resultQuery = resultQuery
                    .orderBy("brand")
                    .startAt(query.lowercase())
                    .endAt(query.lowercase() + '\uf8ff')

                resultQuery.queryToFlow("getQueryProducts")
            } else {
                resultQuery.limit(((uploadsAmount + 1) * productsPerPage).toLong())
                    .queryToFlow<Product>("getQueryProducts").drop(uploadsAmount * productsPerPage)
            }
        }
    } catch (fe: FirebaseException){
        emptyFlow()
    }

    // -------------------------------------------------------------
    // users collection operations
    suspend fun createUser(user : User) : Result<String>? = try {
        withContext(Dispatchers.IO) {
            var result: Result<String>? = null
            user.id?.let { uid ->
                usersCollection.document(uid)
                    .set(user)
                    .addOnCompleteListener { task ->
                        result = if (task.isSuccessful)
                            Result.success("")
                        else
                            Result.failure(task.exception ?: Exception("null exception"))
                        Log.d(
                            TAG,
                            "createUser: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}"
                        )
                    }
                    .await()
            }
            result
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    suspend fun getUserData(userId: String) = try {
        withContext(Dispatchers.IO) {
            usersCollection.document(userId).get().await().toObject(User::class.java)
        }
    } catch (_ : Exception){ User() }

    suspend fun updateUserData(user: User) = user.id?.let { userId ->
        try {
            withContext(Dispatchers.IO) {
                usersCollection.document(userId).set(user).await()
            }
        } catch (_ : Exception) {}
    }

    suspend fun phoneNumberIsNotUsedYet(phoneNumber: String) : Boolean = try {
        withContext(Dispatchers.IO) {
            usersCollection.whereEqualTo("phone_number", phoneNumber).get().await().isEmpty
        }
    } catch (e : Exception){
        false
    }

    // -------------------------------------------------------------
    // cart collection operations

    suspend fun getCartFromDatabase(userId: String) : Flow<CartObj> = try {
        withContext(Dispatchers.IO){
            cartCollection.whereEqualTo("user_id", userId).queryToFlow("getCartFromDatabase")
        }
    } catch (e : Exception){
        emptyFlow()
    }

    suspend fun saveCartToDatabase(cart: List<ProductWithAmount>, userId: String) : Result<String> = try {
        withContext(Dispatchers.IO) {
            cart
                .map { async { addCartObj(it, userId) } }
                .awaitAll()
                .find { it?.isFailure == true  } ?: Result.success("")
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    suspend fun deleteCartFromDatabase(userId: String) : Result<String> = try {
        withContext(Dispatchers.IO){
            val productIdsToDelete = cartCollection.whereEqualTo("user_id", userId).get().await().map { it.id }

            val deferreds = productIdsToDelete.map { productId ->
                async {
                    Log.d("DSOHFCIOASSD", "cart: $productId")
                    cartCollection.document(productId).delete()
                        .addOnCompleteListener {
                            Log.d("DSOHFCIOASSD", "cart: isSucc = ${it.isSuccessful}")
                        }
                        .await()
                }
            }
            deferreds.awaitAll()

            Result.success("")
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    private suspend fun addCartObj(productWithAmount : ProductWithAmount, userId : String) : Result<String>? = try {
        withContext(Dispatchers.IO) {
            var result: Result<String>? = null
            cartCollection.document(userId + "|" + productWithAmount.product?.id.toString()).set(
                CartObj(
                    userId = userId,
                    productId = productWithAmount.product?.id,
                    cashPriceAmount = productWithAmount.amountCash,
                    cashlessPriceAmount = productWithAmount.amountCashless
                )
            ).addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("added")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(
                    TAG,
                    "addCartObj: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}"
                )
            }.await()
            result
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    // -------------------------------------------------------------
    // favourite collection operations

    suspend fun getFavouritesFromDatabase(userId: String) : Flow<FavouriteObj> = try {
        withContext(Dispatchers.IO){
            favouriteCollection.whereEqualTo("user_id", userId).queryToFlow("getFavouriteFromDatabase")
        }
    } catch (e : Exception){
        emptyFlow()
    }

    suspend fun saveFavouritesToDatabase(favs: List<ProductWithAmount>, userId: String) : Result<String> = try {
        withContext(Dispatchers.IO) {
            favs
                .map {
                    async { addFavouriteObj(it, userId) }
                }
                .awaitAll()
                .find { it?.isFailure == true  } ?: Result.success("")
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    private suspend fun addFavouriteObj(productWithAmount : ProductWithAmount, userId : String) : Result<String>? = try {
        withContext(Dispatchers.IO) {
            var result: Result<String>? = null
            favouriteCollection.document(userId + "|" + productWithAmount.product?.id.toString())
                .set(
                    FavouriteObj(
                        userId = userId,
                        productId = productWithAmount.product?.id,
                        cashPriceAmount = productWithAmount.amountCash,
                        cashlessPriceAmount = productWithAmount.amountCashless
                    )
                ).addOnCompleteListener { task ->
                result = if (task.isSuccessful)
                    Result.success("added")
                else
                    Result.failure(task.exception ?: Exception("null exception"))

                Log.d(
                    TAG,
                    "addFavouriteObj: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}"
                )
            }.await()
            result
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    suspend fun deleteFavouritesFromDatabase(userId: String) : Result<String> = try {
        withContext(Dispatchers.IO){
            val productIdsToDelete = favouriteCollection.whereEqualTo("user_id", userId).get().await().map { it.id }

            val deferreds = productIdsToDelete.map { productId ->
                async {
                    Log.d("DSOHFCIOASSD", "favs: $productId")
                    favouriteCollection.document(productId).delete()
                        .addOnCompleteListener {
                            Log.d("DSOHFCIOASSD", "favs: isSucc = ${it.isSuccessful}")
                        }
                        .await()
                }
            }
            deferreds.awaitAll()

            Result.success("")
        }
    } catch (e : Exception){
        Result.failure(e)
    }

    // -------------------------------------------------------------
    // orders collection operations

    suspend fun getUserOrders() : Flow<Order> = try {
        withContext(Dispatchers.IO) {
            ordersCollection
                .whereEqualTo("user_id", FirebaseAuth.getInstance().uid)
                .queryToFlow("getUserOrders")
        }
    } catch (e : Exception){
        emptyFlow()
    }

    // -------------------------------------------------------------
    // orders_products collection operations
    suspend fun getOrderProducts(orderId : String) : List<OrderProduct> = try {
        withContext(Dispatchers.IO) {
            ordersProductsCollection
                .whereEqualTo("order_id", orderId)
                .get()
                .addOnCompleteListener { task ->
                    Log.d(
                        TAG,
                        "getOrderProducts: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}"
                    )
                }.await().toObjects(OrderProduct::class.java)
        }
    } catch (e : Exception){
        emptyList()
    }

    // -------------------------------------------------------------
    // black_list collection operations
    suspend fun isInBlackList(phoneNumber: String) : Boolean = try {
        withContext(Dispatchers.IO) {
            blackListCollection.document(phoneNumber).get().await().exists()
        }
    } catch (e : Exception){
        false
    }

}