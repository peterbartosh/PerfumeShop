package com.example.perfumeshop.data_layer.repositories

import android.util.Log
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.models.User
import com.example.perfumeshop.data_layer.utils.QueryType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

fun Query.filterPrice(range : ClosedFloatingPointRange<Float>) : Query {
    return this
        .whereGreaterThan("price", range.start)
        .whereLessThan("price", range.endInclusive)
}

fun Query.filterInOnHand() : Query {
    return this.whereIn("is_on_hand", listOf(true))
}

fun Query.filterVolume(list : List<Int>) : Query {
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
                q.filterInOnHand()
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
