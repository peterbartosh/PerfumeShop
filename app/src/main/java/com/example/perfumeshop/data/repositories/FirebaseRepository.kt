package com.example.perfumeshop.data.repositories

import android.util.Log
import com.example.perfumeshop.data.models.CartObj
import com.example.perfumeshop.data.models.FavouriteObj
import com.example.perfumeshop.data.models.Order
import com.example.perfumeshop.data.models.OrderProduct
import com.example.perfumeshop.data.models.Product
import com.example.perfumeshop.data.models.ProductWithAmount
import com.example.perfumeshop.data.models.Review
import com.example.perfumeshop.data.models.User
import com.example.perfumeshop.data.utils.QueryType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val TAG = "FireRepository"

suspend inline fun <reified T> Query.queryToFlow() : Flow<T> {
    return this.get().await().toObjects(T::class.java).asFlow()
}

// add more

class FireRepository @Inject constructor(
    private val queryProducts: Query,
    private val queryUsers: Query,
    private val queryReview: Query,
    private val queryOrders : Query,
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

    private val blackListCollection = FirebaseFirestore.getInstance()
        .collection("black_list")

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
        val uid = user.id
        if (uid != null)
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener { Log.d(TAG, "createUser: SUCCESS") }
                .addOnFailureListener { Log.d(TAG, "createUser: FAILED") }
                .await()
    }

    suspend fun checkBlackList(phoneNumber: String) : Boolean {
        val exists = blackListCollection.document(phoneNumber)
            .get().await().exists()
        Log.d(TAG, "checkBlackList: $exists")
        return exists
    }

    suspend fun getUserOrders() : Flow<Order> {
        return queryOrders.whereEqualTo("user_id",
                                        FirebaseAuth.getInstance().uid).queryToFlow()
    }

    suspend fun getOrderProducts(orderId : String) : List<OrderProduct> {
        return ordersProductsCollection.whereEqualTo("order_id", orderId)
            .get().await().toObjects(OrderProduct::class.java)
    }

    suspend fun getQueryProducts(
        query : String,
        queryType: QueryType,
        N : Int,
        uploadsAmount : Int
    ) : Flow<Product> {

        val resultQuery = if (queryType == QueryType.brand)
                productsCollection
                .orderBy(queryType.name)
                .startAt(query.lowercase())
                .endAt(query.lowercase() + '\uf8ff')
        else
            productsCollection.whereEqualTo(queryType.name, query)

        val flow = resultQuery.limit(((uploadsAmount + 1) * N).toLong()).queryToFlow<Product>()

        return flow.drop(uploadsAmount * N)
    }

    suspend fun getProductReviews(productId : String) : Flow<Review> {
        return queryReview.whereEqualTo("product_id", productId).queryToFlow()
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
        val productsIds = favouriteCollection.whereEqualTo("user_id", userId).get().await()
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
        val documentsIds = cartCollection.whereEqualTo("user_id", userId)
            .get().await().documents.map { it.id }

        documentsIds.forEach { id ->
            cartCollection.document(id).delete().addOnCompleteListener{
                if (!it.isSuccessful) isSuccess = false
            }.await()
        }
        return isSuccess
    }

    suspend fun getUserCartProducts(userId : String) : Flow<ProductWithAmount> {

        val cart = cartCollection.whereEqualTo("user_id", userId).get().await()
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