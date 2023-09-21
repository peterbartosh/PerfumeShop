package com.example.perfumeshop.data_layer.repositories

import android.util.Log
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.models.User
import com.example.perfumeshop.data_layer.utils.ProductType
import com.example.perfumeshop.data_layer.utils.QueryType
import com.google.common.collect.ImmutableList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal fun Query.filterPrice(range : ClosedFloatingPointRange<Float>) : Query {
    return this
        .whereGreaterThan("price", range.start)
        .whereLessThan("price", range.endInclusive)
}

internal fun Query.filterIsOnHand() : Query {
    return this.whereIn("is_on_hand", listOf(true))
}

internal fun Query.filterVolume(list : List<Int>) : Query {
    return this.whereIn("volume", list)
}

// add more


class FireRepository @Inject constructor(
    private val queryProducts: Query,
    private val queryUsers: Query,
    private val queryReview: Query,
    private val queryOrders : Query,
    private val queryHot : Query,
) {


    suspend fun saveToFirebase(item: Any, collectionName: String) : Pair<Boolean, Exception?> {
        var state : Pair<Boolean, Exception?> = Pair(false, null)
        val doc = FirebaseFirestore.getInstance().collection(collectionName)
            .add(item)
            .addOnFailureListener{ state = Pair(false, it)
                Log.d("SAVE_TO_DATAB", "saveToFirebase: ${it.message}")}
            .await()
        doc.update("id", doc.id)
            .addOnSuccessListener { state = Pair(true, null)
                Log.d("SAVE_TO_DATAB", "saveToFirebase: SUCC")}
            .addOnFailureListener{ state = Pair(false, it)
                Log.d("SAVE_TO_DATAB", "saveToFirebase: FAIL")
    }.await()
        return state
    }

    fun deleteFromDatabase(id: String, collectionName: String, onSuccess: () -> Unit) {

        CoroutineScope(Job()).launch {
            FirebaseFirestore.getInstance()
                .collection(collectionName)
                .document(id)
                .delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        onSuccess.invoke()
                    }
                }
        }
    }

    suspend fun deleteProducts(
        productType: ProductType,
        volumes : ImmutableList<Int>,
        onSuccess: () -> Unit) = withContext(Dispatchers.IO){

            val productsCollection = FirebaseFirestore.getInstance()
                .collection("products")

            val docIds = productsCollection
                .whereIn("type", listOf(productType.name))
                .apply {
                    if (volumes.isNotEmpty())
                        this.whereIn("volume", volumes)
                    else
                        this
                }
                .get().await().documents.map { it.id }

            for (id in docIds) productsCollection.document(id).delete()
    }

    suspend fun createProducts(products: ImmutableList<Product>) = withContext(Dispatchers.IO){
        val productsCollection = FirebaseFirestore.getInstance()
            .collection("products")

        for (product in products) {
            val id = product.id
            if (id != null) productsCollection.document(id).set(product)
        }
    }

    fun updateFieldInDatabase(
        collectionPath : String,
        id : String,
        fieldPath : String,
        updatedValue : Any) {

            FirebaseFirestore.getInstance()
                .collection(collectionPath)
                .document(id)
                .update(fieldPath, updatedValue)
                .addOnCompleteListener {
                    Log.d("DATABASE_TASK", "updateFieldInDatabase: SUCCESS")
                }.addOnFailureListener {
                    Log.d("DATABASE_TASK", "updateFieldInDatabase: FAILED")
                }
    }

    suspend fun getUserOrders() : Flow<Order> = flow {
        queryOrders.whereIn("user_id", listOf(FirebaseAuth.getInstance().uid))
            .get().await().documents.forEach{ ds ->
                ds.toObject(Order::class.java)?.let { emit(it) }
        }
    }

    suspend fun getHotCollection() : Flow<Product> = flow {
        queryHot.get().await().documents.forEach { ds ->
            ds.toObject(Product::class.java)?.let { emit(it) }
        }
    }

    suspend fun getQueryProducts(query : String, queryType: QueryType) : Flow<Product> = flow{
        queryProducts.whereIn(queryType.name,
                              listOf(query, query.lowercase(), query.first().uppercase() + query.substring(1)))
            .get().await().documents.forEach{ ds ->
                ds.toObject(Product::class.java)?.let { emit(it) }
            }
    }

    suspend fun getProductsWithFilter(
        // queryByBrand : String? = null,
        minValue : Float? = null,
        maxValue : Float? = null,
        isOnHand : Boolean? = null,
        volumes : List<Int>? = null
    ) : Flow<Product> = flow{
        queryProducts.let { q ->
            if (maxValue != null || minValue != null)
                q.filterPrice((minValue ?: 0f)..(maxValue ?: 10000f))
            if (isOnHand != null)
                q.filterIsOnHand()
            if (volumes != null)
                q.filterVolume(volumes)
            q.get().await().documents.forEach { ds ->
                ds.toObject(Product::class.java)?.let { emit(it) }
            }
        }
    }

    suspend fun getProductReviews(productId : String) : Flow<Review> = flow {
        queryReview.whereIn("product_id", listOf(productId))
            .get().await().documents.forEach{ ds ->
                ds.toObject(Review::class.java)?.let { emit(it) }
            }
    }

    suspend fun getProduct(productId: String) : Product? {
        return try {
            queryProducts.whereIn("id", listOf(productId))
                .get().await().first().toObject(Product::class.java)
        } catch (e : Exception) {
            Log.d("ERROR_ERROR", "getProduct: ${e.message}")
            null
        }
    }

    suspend fun getUserFavouriteProducts(userId : String) : Flow<Product> = flow {

        val userFavsIds = queryUsers.whereIn("id", listOf(userId))
            .get().await().documents.first().toObject(User::class.java)?.favourite

        userFavsIds?.let { ids ->
            queryProducts.whereIn("id", ids).get().await().documents
                .forEach{ ds ->
                    ds.toObject(Product::class.java)?.let { emit(it) }
                }
        }
    }

    suspend fun getUserCartProducts(userId : String) : Flow<Product> = flow {

        Log.d("UID_TEST", userId)

        val userCartIds = queryUsers.whereIn("id", listOf(userId))
            .get().await().documents.first().toObject(User::class.java)?.cart

        Log.d("UID_TEST", userCartIds.toString())

        userCartIds?.let { ids ->
            queryProducts.whereIn("id", ids).get().await().documents
                .forEach{ ds ->
                    ds.toObject(Product::class.java)?.let { emit(it) }
                }
        }
    }


}
