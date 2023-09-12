package com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.ui_layer.theme.Gold
import com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui.InputField
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random


@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Поиск по брэндам",
    onSearch: (String) -> Unit = {}) {

    Column (modifier = modifier) {
        val searchQueryState = rememberSaveable { mutableStateOf("") }


        val keyboardController = LocalSoftwareKeyboardController.current

        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()

        }

        InputField(valueState = searchQueryState,
                   labelId = hint,
                   enabled = !loading,
                   keyboardActions = KeyboardActions {
                       if (!valid) return@KeyboardActions
                       onSearch(searchQueryState.value.trim())
                       searchQueryState.value = ""
                       keyboardController?.hide()
                   })

    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyProductList(
    modifier: Modifier = Modifier,
    listOfProducts : List<Product>,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onAddToCartClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    userScrollEnabled : Boolean = true
) {

    val scrollState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        contentPadding = PaddingValues(3.dp),
        modifier = modifier.height(155.dp * (listOfProducts.size/2 + 1)),
        state = scrollState,
        userScrollEnabled = userScrollEnabled,
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp)){
        items(items = listOfProducts,
              //key = { product -> product.id!! }
        ){ product ->

            ProductCard(
                product = product,
                onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck

            )
        }

    }

}

//= Product(id = "wrjqoawWkcad",
//brand = "Gucci",
//collection = "Flora",
//volume = 125,
//price = 350.0,
//isOnHand = true,
//sex = Sex.Female),

//@Preview(showBackground = true)
@Composable
fun ProductCard(
    product: Product,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onAddToCartClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean) {

    val context = LocalContext.current

    val isInCart = remember {
        mutableStateOf(isInCartCheck(product.id ?: Random(35).toString()))
    }

    val isInFavourite = remember {
        mutableStateOf(isInFavouriteCheck(product.id ?: Random(44).toString()))
    }

    Log.d("IS_IN_TEST", "${isInCart.value}  ${isInFavourite.value}")

    Card(modifier = Modifier
        .clickable { onProductClick.invoke(product.id.toString()) }
        .width(150.dp)
        .height(150.dp)
        .padding(3.dp),
         colors = CardDefaults.cardColors(containerColor = Color.White),
         border = BorderStroke(width = 1.dp, color = Color.LightGray),
         shape = RoundedCornerShape(5.dp),
         elevation = CardDefaults.cardElevation(7.dp)) {

        Column(modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
               verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {

            Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentWidth()) {

                Image(
                    painter = painterResource(id = R.drawable.gucci_flora),
                    contentDescription = "perfume image",
                    modifier = Modifier
                        .border(width = 1.dp, color = Color.LightGray)
                        .height(100.dp)
                        .width(100.dp)
                        .padding(5.dp)
                )
                //Spacer(modifier = Modifier.width(40.dp))

                Column(modifier = Modifier.wrapContentSize()) {

                    Spacer(modifier = Modifier.height(10.dp))

                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                Toast.makeText(context, "Вы не авторизованы!", Toast.LENGTH_SHORT)
                                    .show()
                            else {
                                if (isInFavourite.value) onRemoveFromFavouriteClick(product.id!!)
                                else onAddToFavouriteClick(product)
                                isInFavourite.value = !isInFavourite.value
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isInFavourite.value) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "fav icon",
                            tint = if (isInFavourite.value) Gold else Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                Toast.makeText(context, "Вы не авторизованы!", Toast.LENGTH_SHORT)
                                    .show()
                            else {
                                if (isInCart.value)
                                    onRemoveFromCartClick(product.id!!)
                                else
                                    onAddToCartClick(product)
                                isInCart.value = !isInCart.value
                            }
                        }) {
                        Icon(
                            imageVector = if (isInCart.value)
                                Icons.Filled.ShoppingCart
                            else
                                Icons.Outlined.ShoppingCart,
                            contentDescription = "cart icon",
                            tint = if (isInCart.value) Gold else Color.Black
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.wrapContentHeight()) {

                Text(text = product.id.toString(),
                     overflow = TextOverflow.Ellipsis, fontSize = 10.sp)

                Text(
                    text = "Коллекция: ${product.collection}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    fontSize = 7.sp,
                    //style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Пол: ${product.sex}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    fontSize = 7.sp,
                   // style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Объем: ${product.volume}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    fontSize = 7.sp,
                 //   style = MaterialTheme.typography.bodyMedium
                )

            }

//            Image(painter = rememberAsyncImagePainter(model =
//                                                      "https://www.google.com/url?sa=i&url=https%3A%2F%2Fvanille.by%2Fgucci-flora-by-gucci-gracious-tuberose&psig=AOvVaw1E9yBoCNg_-1U0HlIRKQ7l&ust=1692929272446000&source=images&cd=vfe&opi=89978449&ved=0CBAQjRxqFwoTCPDipJGb9IADFQAAAAAdAAAAABAE"),
//                  contentDescription = "image book",
//                  modifier = Modifier
//                      .height(30.dp)
//                      .width(30.dp)
//                      .padding(end = 5.dp))



        }

    }
}



@Composable
fun FilterList(
    rangeSliderState: MutableState<ClosedFloatingPointRange<Float>>,
    showDialog: MutableState<Boolean>,
    selected: MutableState<Boolean>,
    listOfButtonsStates: List<Pair<MutableState<Boolean>, Int>>,
    onApplyButtonClick : () -> Unit
) {

    if (showDialog.value)
         Dialog(onDismissRequest = { showDialog.value = false}) {

        Surface(color = Color.White, modifier = Modifier.wrapContentSize()) {

            Row() {

                // Spacer(modifier = Modifier.width(250.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {

                    FilterOption(text = "Только в наличии", height = 50.dp, selected = selected)

                    PriceSlider(
                        maxValue = 10f,
                        text = "Цена: ",
                        height = 50.dp,
                        rangeSliderState = rangeSliderState
                    )

                    VolumeOption(listOfButtonsStates = listOfButtonsStates)

                    Spacer(modifier = Modifier.height(50.dp))

                    Button(
                        onClick = onApplyButtonClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(width = 1.dp, color = Gold),
                        modifier = Modifier.wrapContentSize(),
                        contentPadding = ButtonDefaults.ContentPadding
                    ) {
                        Text(text = "Применить", fontSize = 18.sp, color = Color.Black)
                    }

                }
            }
        }
    }
}

@Composable
fun VolumeOption (listOfButtonsStates:  List<Pair<MutableState<Boolean>, Int>>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()) {

        Text(text = "Объем")

        Spacer(modifier = Modifier.width(30.dp))

        listOfButtonsStates.forEach{ buttonState ->
            ValueButton(text = "${buttonState.second} мл", buttonState.first)
        }
    }
}

@Composable
fun ValueButton(text : String, clicked : MutableState<Boolean>) {

    Button(
        onClick = { clicked.value = !clicked.value },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = if (clicked.value) Gold else Color.LightGray),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        Text(text = text, fontSize = 10.sp, color = Color.Black)
    }
}

@Composable
fun FilterOption(selected : MutableState<Boolean>, text: String, height: Dp) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(height)) {
        RadioButton(selected = selected.value, onClick = { selected.value = !selected.value })

        Spacer(modifier = Modifier.width(30.dp))

        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceSlider(rangeSliderState: MutableState<ClosedFloatingPointRange<Float>>, maxValue: Float, text: String, height: Dp) {



    Row(modifier = Modifier
        .fillMaxWidth()
        .height(height))  {

        Text(text = text)

        Spacer(modifier = Modifier.width(30.dp))

        RangeSlider(value = rangeSliderState.value, onValueChange = { rangeSliderState.value = it },
                    modifier = Modifier.fillMaxWidth())

    }

}