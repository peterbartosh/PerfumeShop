package com.example.perfumeshop.data_layer.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.navigation.cartGraphRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.navigation.cartGraphStartDestination
import com.example.perfumeshop.ui_layer.features.main.children.home.children.home.navigation.homeRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeGraphRoute
import com.example.perfumeshop.ui_layer.features.main.children.home.navigation.homeGraphStartDestination
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileGraphRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileGraphStartDestination
import com.google.firebase.firestore.FirebaseFirestore
import java.util.function.Predicate
import kotlin.random.Random


fun getPredicateByQuery(queryType: QueryType, query : String) : Predicate<Product> {
            return when(queryType){
                QueryType.collection -> Predicate{ p ->
                    if (p.collection == null) false
                    else
                    p.collection?.trim()?.lowercase() == query.trim().lowercase() ||
                            p.collection?.trim()?.lowercase()
                                ?.contains(query.trim().lowercase()) ?: false ||
                            query.trim().lowercase().contains(p.collection?.trim()?.lowercase()!!)

                }
                QueryType.type -> Predicate{ p -> p.type == query}
                QueryType.brand -> Predicate{ p ->
                    if (p.brand == null) false
                    else
                        p.brand?.trim()?.lowercase() == query.trim().lowercase() ||
                                p.brand?.trim()?.lowercase()
                                    ?.contains(query.trim().lowercase()) ?: false ||
                                query.trim().lowercase().contains(p.brand?.trim()?.lowercase()!!)

                }
                QueryType.volume -> Predicate{ p ->
                    p.volume in query.split('v')[0].toInt()..query.split('v')[1].toInt()
                }
                QueryType.price -> Predicate{ p ->
                    p.price!! >= query.split('p')[0].toDouble() &&
                            p.price!! <= query.split('p')[1].toDouble()
                }
                QueryType.sex -> Predicate{ p ->
                    query.map { ch -> sexEntities[ch.digitToInt()] }.any { sex -> p.sex == sex }
                }
                QueryType.is_on_hand -> Predicate{ p -> p.isOnHand!! }
            }
        }


fun saveToFirebase(item: Any, collectionName: String) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection(collectionName)

    if (item.toString().isNotEmpty()){
        dbCollection.add(item)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //navController.popBackStack()
                        }


                    }.addOnFailureListener {
                        Log.d("Error", "SaveToFirebase:  ${it.message} ")
                    }

            }


    }else {



    }
}



fun deleteFromDatabase(id: String, collectionName: String, onSuccess: () -> Unit) {
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


fun updateInDatabase(
    id: String,
    bookToUpdate: Map<String, Comparable<*>?>,
    context: Context
) {

    FirebaseFirestore.getInstance()
        .collection("books")
        .document(id)
        .update(bookToUpdate)
        .addOnCompleteListener {
            Toast.makeText(context, "User Updated Successfully!",
                           Toast.LENGTH_LONG).show()
           // navController.navigate(ReaderScreens.HomeScreen.name)

        }.addOnFailureListener{
            Toast.makeText(context, "Error updating document",
                           Toast.LENGTH_LONG).show()
            Log.w("ERROR_ERROR", "Error updating document" , it)
        }
}



fun createProduct() {
    for (i in 10..200) {
        val product = Product(type = "type$i", volume = i*5, brand = "Tom Ford", collection = "Mustang",
                              price = Random.nextDouble(5.0, 40.0), sex = Sex.Male, isOnHand = true)
        saveToFirebase(product, collectionName = "products")
    }
}

@Composable
fun getWidthPercent(context: Context): Dp {
    val displayMetrics = context.resources.displayMetrics
    return ((displayMetrics.widthPixels / displayMetrics.density) / 100).dp
}

@Composable
fun getHeightPercent(context: Context): Dp {
    val displayMetrics = context.resources.displayMetrics
    return ((displayMetrics.heightPixels / displayMetrics.density) / 100).dp
}


var homeGraphR : NavGraph? = null
var cartGraphR : NavGraph? = null
var profileGraphR : NavGraph? = null


fun NavController.setNestedGraphStartDest(){
    when (this.currentDestination?.route?.split(' ')?.first()) {
        homeRoute -> homeGraphR?.setStartDestination(this.currentDestination?.route.toString())
        cartRoute -> cartGraphR?.setStartDestination(this.currentDestination?.route.toString())
        profileRoute -> profileGraphR?.setStartDestination(this.currentDestination?.route.toString())
    }
}

fun NavController.navigateToGraph(
    graphRoute : String,
    saveStartDest : Boolean = true,
    navOptions: NavOptionsBuilder.() -> Unit
){


//    if (saveStartDest)
//        currentNestedGraph?.setStartDestination(startDestRoute = this.currentDestination?.route.toString())

//    if (saveStartDest)
//        this.setNestedGraphStartDest()

    Log.d(
        "TTTTTOOOOO",
        homeGraphStartDestination.value + " " + cartGraphStartDestination.value + " " + profileGraphStartDestination.value
    )

    this.navigate(route = graphRoute)
}