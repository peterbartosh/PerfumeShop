package com.example.perfumeshop.data_layer.repositories

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.perfumeshop.data_layer.models.CartObj
import com.example.perfumeshop.data_layer.models.FavouriteObj
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.OrderProduct
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.ProductWithAmount
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.models.User
import com.example.perfumeshop.data_layer.utils.ProductType
import com.example.perfumeshop.data_layer.utils.QueryType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val TAG = "FireRepository"

internal fun Query.filterPrice(range : ClosedFloatingPointRange<Float>) : Query {
    return this
        .whereGreaterThan("price", range.start)
        .whereLessThan("price", range.endInclusive)
}

internal fun Query.filterIsOnHand() : Query {
    return this.whereIn("is_on_hand", listOf(true))
}

internal fun Query.filterVolume(list : List<String>) : Query {
    return this.whereIn("volume", list)
}

suspend inline fun <reified T> Query.queryToFlow() : Flow<T> {
    return this.get().await().toObjects(T::class.java).asFlow()
}

fun Query.applyFilter(
    // queryByBrand : String? = null,
    minValue : Float? = null,
    maxValue : Float? = null,
    isOnHand : Boolean? = null,
    volumes : List<String>? = null
) : Query {
    if (maxValue != null || minValue != null)
        this.apply { filterPrice((minValue ?: 0f)..(maxValue ?: 10000f)) }
    if (isOnHand != null)
        this.apply { filterIsOnHand() }
    if (volumes != null)
        this.apply { filterVolume(volumes) }

    return this
}

// add more


class FireRepository @Inject constructor(
    private val queryProducts: Query,
    private val queryUsers: Query,
    private val queryReview: Query,
    private val queryOrders : Query,
    private val queryHot : Query,
) {

    private val productsCollection = FirebaseFirestore.getInstance()
        .collection("products")

    private val usersCollection = FirebaseFirestore.getInstance()
        .collection("users")

    private val cartCollection = FirebaseFirestore.getInstance()
        .collection("cart")

    private val favouriteCollection = FirebaseFirestore.getInstance()
        .collection("favourite")

    private val ordersProductsCollection = FirebaseFirestore.getInstance()
        .collection("orders_products")

    suspend fun saveToFirebase(
        item: Any,
        collectionName: String,
        updateId : Boolean = true
    ) : Pair<Boolean, Any?> {

        var state : Pair<Boolean, Any?> = Pair(false, null)

        val doc = FirebaseFirestore.getInstance().collection(collectionName)
            .add(item)
            .addOnFailureListener{
                state = Pair(false, it)
                Log.d(TAG, "saveToFirebase: ${it.message}")}
            .await()

        if (state.second == null && !updateId) state = Pair(true, null)

        if (updateId)
            doc.update("id", doc.id)
                .addOnSuccessListener {
                    state = Pair(true, null)
                    Log.d(TAG, "saveToFirebase: SUCC")}
                .addOnFailureListener{
                    state = Pair(false, it)
                    Log.d(TAG, "saveToFirebase: FAIL")
            }.await()

        if (state.second == null) state = Pair(true, doc.id)

        return state
    }

    suspend fun createUser(user : User){
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.id)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "createUser: SUCCESS") }
            .addOnFailureListener { Log.d(TAG, "createUser: FAILED") }
            .await()
    }

    suspend fun getUserOrders() : Flow<Order> {
        return queryOrders.whereIn("user_id", listOf(FirebaseAuth.getInstance().uid)).queryToFlow()
    }

    suspend fun getOrderProducts(orderId : String) : List<OrderProduct> {
        return ordersProductsCollection.whereIn("order_id", listOf(orderId))
            .get().await().toObjects(OrderProduct::class.java)
    }

    fun getQueryProducts(
        query : String,
        queryType: QueryType
    ) : Query {

        return queryProducts
            .orderBy(queryType.name)
            .startAt(query.lowercase())
            .endAt(query.lowercase() + '\uf8ff')
    }

    suspend fun getProductReviews(productId : String) : Flow<Review> {
        return queryReview.whereIn("product_id", listOf(productId)).queryToFlow()
    }

    suspend fun getProduct(productId: String?) : Product? {
        return if (!productId.isNullOrEmpty()) {
            val snapshot = productsCollection.document(productId).get().await()
            if (snapshot.exists()) snapshot.toObject(Product::class.java) else null
        } else null
    }

    suspend fun addFavouriteObj(productId: String, userId: String){
        favouriteCollection.document("$userId|$productId")
            .set(FavouriteObj(userId = userId, productId))
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("SUCC", "addFavouriteObj: SUCC")
                else
                    Log.d("ERROR_ERROR", "addFavouriteObj: Failed")
            }.await()
    }

    suspend fun deleteFavouriteObj(productId: String, userId: String){
        favouriteCollection.document("$userId|$productId")
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("SUCC", "deleteFavouriteObj: SUCC")
                else
                    Log.d("ERROR_ERROR", "deleteFavouriteObj: Failed")
            }.await()
    }

    suspend fun getUserFavouriteProducts(userId : String) : Flow<Product> {
        val productsIds = favouriteCollection.whereIn("user_id", listOf(userId)).get().await()
            .toObjects(FavouriteObj::class.java)
            .map { it.productId }

        return if (productsIds.isNotEmpty())
            productsCollection.whereIn("id", productsIds)
            .get().await().toObjects(Product::class.java).asFlow()
        else
            emptyFlow()
    }


    suspend fun addCartObj(productWithAmount : ProductWithAmount, userId : String){
        cartCollection.document(userId + "|" + productWithAmount.product?.id.toString()).set(
            CartObj(
                userId = userId,
                productId = productWithAmount.product?.id,
                amount = productWithAmount.amount,
                isCashPrice = productWithAmount.isCashPrice
                )
        ).addOnCompleteListener {
            if (it.isSuccessful)
                Log.d("SUCC", "addCartObj: SUCC")
            else
                Log.d("ERROR_ERROR", "addCartObj: Failed")

        }.await()
    }

    suspend fun deleteCartObj(productId: String, userId: String){
        cartCollection.document("$userId|$productId").delete()
            .addOnCompleteListener {
                if (it.isSuccessful)
                    Log.d("SUCC", "deleteCartObj: SUCC")
                else
                    Log.d("ERROR_ERROR", "deleteCartObj: Failed")
            }.await()
    }

    suspend fun updateCartProductAmount(productId: String, userId: String, amount: Int){
        cartCollection.document("$userId|$productId")
            .update("amount", amount)
            .addOnCompleteListener {
            if (it.isSuccessful)
                Log.d("SUCC", "deleteCartObj: SUCC")
            else
                Log.d("ERROR_ERROR", "deleteCartObj: Failed")
        }.await()
    }

    suspend fun clearCart(userId : String?) : Boolean {

        var isSuccess = true
        val documentsIds = cartCollection.whereIn("user_id", listOf(userId))
            .get().await().documents.map { it.id }

        documentsIds.forEach { id ->
            cartCollection.document(id).delete().addOnCompleteListener{
                if (!it.isSuccessful) isSuccess = false
            }.await()
        }
        return isSuccess
    }

    suspend fun getUserCartProducts(userId : String) : Flow<ProductWithAmount> {

        val cart = cartCollection.whereIn("user_id", listOf(userId)).get().await()
            .toObjects(CartObj::class.java)

        Log.d("getUserCartProducts", "getUserCartProducts: ${cart.map { it.productId }}")

        val map = buildMap<String, CartObj> {
            cart.forEach { cartObj ->
                put(cartObj.productId.toString(), cartObj)
            }
        }

        Log.d("getUserCartProducts", "getUserCartProducts: ${map.map { it.key + " : " + it.value.userId }}")

        val productIds = map.keys.toList()

        Log.d("getUserCartProducts", "getUserCartProducts: $productIds")


        val final = if (productIds.isNotEmpty())
            productsCollection.whereIn("id", productIds).get().await().toObjects(Product::class.java)
                .mapNotNull { product ->
                    val id = product.id
                    Log.d("getUserCartProducts", "getUserCartProducts: $id")
                    if (id != null)
                        ProductWithAmount(
                            product = product,
                            amount = map[id]?.amount,
                            isCashPrice = map[id]?.isCashPrice
                        )
                    else null
            }
            else listOf()

        Log.d("getUserCartProducts", "getUserCartProducts: $final")


        return final.asFlow()
    }

}