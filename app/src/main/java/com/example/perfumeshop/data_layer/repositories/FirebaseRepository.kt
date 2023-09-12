package com.example.perfumeshop.data_layer.repositories


import android.util.Log
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.models.User
import com.example.perfumeshop.data_layer.utils.QueryType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
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


    suspend fun getProductsWithFilter(
        minValue : Float? = null,
        maxValue : Float? = null,
        isOnHand : Boolean? = null,
        volumes : List<Int>? = null
    ) : Flow<List<Product>?> = flow{

        queryProducts.let { q ->
            if (maxValue != null || minValue != null)
                q.filterPrice((minValue ?: 0f)..(maxValue ?: 10000f))
            if (isOnHand != null)
                q.filterInOnHand()
            if (volumes != null)
                q.filterVolume(volumes)

            emit(q.get().await().documents.map { ds -> ds.toObject(Product::class.java)!! })
        }

    }

    suspend fun getProductReviews(productId : String) : Flow<List<Review>> = flow {
        emit(
            queryReview.whereIn("product_id", mutableListOf(productId))
                .get().await().documents.asFlow().map { ds -> ds.toObject(Review::class.java)!! }.toList()
        )
    }


    suspend fun getAllProductsFromDatabase() : Flow<Product> {
        return queryProducts.get().await().documents.asFlow().map { ds -> ds.toObject(Product::class.java)!! }
    }


    suspend fun getUserCartProducts(userId : String) : Flow<List<Product>>? {

        Log.d("tttttttt", "getUserCartProducts: 1")

        val userProductsIds : List<String>? = try {

             queryUsers.whereIn("id", mutableListOf(userId))
                .get()
                .await()
                .documents
                .asFlow()
                .map { ds -> ds.toObject(User::class.java)!!.products }
                .first()
        } catch (e : Exception) {
            Log.d("ERROR_ERROR", "getUserCartProducts: ${e.message}")
            null
        }

            if (userProductsIds.isNullOrEmpty()) {
                Log.d("tttttttt", "getUserCartProducts: EEE")
                return null
            }

            val userProducts = listOf( userProductsIds.map { id ->
                queryProducts.whereIn("id", mutableListOf(id)).get().await().documents.first()
            }.map { ds -> ds.toObject(Product::class.java)!!}.toList())

        Log.d("tttttttt", "getUserCartProducts: 3")

            return userProducts.asFlow()
    }

    suspend fun getQueryProducts(query : String, queryType: QueryType) : Flow<List<Product>> = flow{
        emit(queryProducts.whereIn(queryType.name,
                                   mutableListOf(query, query.lowercase(), query.first().uppercase() + query.substring(1)))
                 .get().await().documents.asFlow().map {
                ds -> ds.toObject(Product::class.java)!!
        }.toList())
    }


}
