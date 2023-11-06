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
        suspend fun isAppBlocked() =
            FirebaseFirestore.getInstance()
                .collection("main_app_blocker")
                .document("blocker_id")
                .get().await().getBoolean("is_blocked_for_maintenance")
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
    ) : Result<String>? {
        var result : Result<String>? = null

        if (docId == null) return Result.failure(NullPointerException("id == null"))

        FirebaseFirestore.getInstance().collection(collectionName)
            .document(docId).update(fieldToUpdate, newValue)
            .addOnCompleteListener { task ->
                result = if (task.isSuccessful) Result.success("s")
                else Result.failure(task.exception ?: Exception("null exception"))
            }.await()

        return result
    }

    suspend fun saveToDatabase(
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
    ) : Flow<Product> {

        Log.d("SLAS", "$query ${queryType.name} ${priorities.joinToString { it.toString() }} $isAscending")

        var resultQuery = productsCollection as Query

        if (initQueryType.name == QueryType.type.name)
            resultQuery = resultQuery.whereEqualTo("type", initQuery)

        _filter?.let { filter ->
            resultQuery = resultQuery.where(filter)
                //.orderBy("cash_price")
        }

        if (queryType.name != QueryType.brand.name) {
            val direction =
                if (isAscending) Query.Direction.ASCENDING else Query.Direction.DESCENDING
            when (priorities.joinToString { it.toString() }) {
                "0" -> {
                    Log.d("SIAHISIAD", "0 - 1")
                    resultQuery = resultQuery.orderBy("cash_price", direction)
                    Log.d("SIAHISIAD", "0 - 2")
                }

                "1" -> {
                    Log.d("SIAHISIAD", "1 - 1")
                    resultQuery = resultQuery.orderBy("volume", direction)
                    Log.d("SIAHISIAD", "1 - 2")
                }

                "01" -> {
                    Log.d("SIAHISIAD", "01 - 1")
                    resultQuery = resultQuery
                        .orderBy("cash_price", direction)
                        .orderBy("volume", direction)
                    Log.d("SIAHISIAD", "01 - 2")
                }

                "10" -> {
                    Log.d("SIAHISIAD", "10 - 1")
                    resultQuery = resultQuery
                        .orderBy("volume", direction)
                        .orderBy("cash_price", direction)
                    Log.d("SIAHISIAD", "10 - 2")
                }

                else -> {}
            }
        }

        // mb order by cashprice startat minval end at max val

        if (queryType.name == QueryType.brand.name) {
            resultQuery = resultQuery
                .orderBy("brand")
                .startAt(query.lowercase())
                .endAt(query.lowercase() + '\uf8ff')

            return resultQuery.queryToFlow("getQueryProducts")

        }


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

    suspend fun phoneNumberIsNotUsedYet(phoneNumber: String) : Boolean {
        return usersCollection.whereEqualTo("phone_number", phoneNumber).get().await().isEmpty
    }

    // -------------------------------------------------------------
    // cart collection operations

    suspend fun getCartFromDatabase(userId: String) : Flow<CartObj> {
        return withContext(Dispatchers.IO){
            cartCollection.whereEqualTo("user_id", userId).queryToFlow("getCartFromDatabase")
        }
    }

    suspend fun saveCartToDatabase(cart: List<ProductWithAmount>, userId: String) : Result<String> {
        return withContext(Dispatchers.IO) {
            cart
                .map { async { addCartObj(it, userId) } }
                .awaitAll()
                .find { it?.isFailure == true  } ?: Result.success("")
        }
    }

    suspend fun deleteCartFromDatabase(userId: String) : Result<String> {
        return withContext(Dispatchers.IO){
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
    }

    private suspend fun addCartObj(productWithAmount : ProductWithAmount, userId : String) : Result<String>? {
        var result : Result<String>? = null
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

            Log.d(TAG, "addCartObj: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
        }.await()
        return result
    }

    // -------------------------------------------------------------
    // favourite collection operations

    suspend fun getFavouritesFromDatabase(userId: String) : Flow<FavouriteObj> {
        return withContext(Dispatchers.IO){
            favouriteCollection.whereEqualTo("user_id", userId).queryToFlow("getFavouriteFromDatabase")
        }
    }

    suspend fun saveFavouritesToDatabase(favs: List<ProductWithAmount>, userId: String) : Result<String> {
        return withContext(Dispatchers.IO) {
            favs
                .map {
                    async { addFavouriteObj(it, userId) }
                }
                .awaitAll()
                .find { it?.isFailure == true  } ?: Result.success("")
        }
    }

    private suspend fun addFavouriteObj(productWithAmount : ProductWithAmount, userId : String) : Result<String>? {
        var result : Result<String>? = null
        favouriteCollection.document(userId + "|" + productWithAmount.product?.id.toString()).set(
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

            Log.d(TAG, "addFavouriteObj: isSuccess = ${task.isSuccessful}, exceptionMessage = ${task.exception?.message}")
        }.await()
        return result
    }

    suspend fun deleteFavouritesFromDatabase(userId: String) : Result<String> {
        return withContext(Dispatchers.IO){
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

}