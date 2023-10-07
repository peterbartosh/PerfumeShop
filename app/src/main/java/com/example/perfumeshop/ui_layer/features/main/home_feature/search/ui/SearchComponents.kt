package com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.ProductWithAmount
import com.example.perfumeshop.data_layer.utils.getWidthPercent
import com.example.perfumeshop.data_layer.utils.round
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.components.showToast
import com.example.perfumeshop.ui_layer.features.auth.login_register_feature.ui.InputField
import com.example.perfumeshop.ui_layer.theme.Gold
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalComposeUiApi::class)
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
                   label = hint,
                   enabled = !loading,
                   keyboardActions = KeyboardActions {
                       if (!valid) return@KeyboardActions
                       onSearch(searchQueryState.value.trim())
                       searchQueryState.value = ""
                       keyboardController?.hide()
                   })

    }


}


@Composable
fun UploadMoreButton(searchViewModel: SearchViewModel) {
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        if (searchViewModel.searchProducts.size
            == (searchViewModel.uploadsAmount + 1) * N)
        if (!searchViewModel.uploadingMore)
            Button(
                onClick = {
                    searchViewModel.uploadMore()
                },
                contentPadding = PaddingValues(3.dp),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .height(30.dp)
                    .width(70.dp)
            ) {
                Text(text = "Ещё")
            }
        else
            LoadingIndicator()
    }
}

@Composable
fun LazyProductList(
    modifier: Modifier = Modifier,
    listOfProductsWithAmounts : List<ProductWithAmount>,
    updateChangedAmount: (Int, Int) -> Unit = {_,_ ->},
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    isCashPriceState : MutableState<Boolean>?,
    userScrollEnabled : Boolean = true,
    UploadMoreButton :  @Composable () -> Unit = { Box{} }
) {

    Log.d("LazyProductList", "LazyProductList: ${listOfProductsWithAmounts.size}")


    LazyColumn(
        contentPadding = PaddingValues(3.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = rememberLazyListState(),
        userScrollEnabled = userScrollEnabled,
    ) {
        itemsIndexed(listOfProductsWithAmounts,
            key = {i, p -> p.product?.id ?: i}) { ind, productWithAmount ->

            ProductRow(
                productWithAmount = productWithAmount,
                onAmountChange = { value -> updateChangedAmount(ind, value) },
                onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck,
                isCashPriceState = isCashPriceState
            )
        }

        item {
            UploadMoreButton()
        }
    }
}

@Composable
fun ProductRow(
    showEditableAmount : Boolean = true,
    productWithAmount: ProductWithAmount,
    onAmountChange : (Int) -> Unit,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    isCashPriceState : MutableState<Boolean>? = null,
) {

    val context = LocalContext.current

    val isInCart = remember {
        mutableStateOf(isInCartCheck(productWithAmount.product?.id ?: ""))
    }

    val isInFavourite = remember {
        mutableStateOf(isInFavouriteCheck(productWithAmount.product?.id ?: ""))
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onProductClick(productWithAmount.product?.id.toString()) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .wrapContentHeight()
                    .padding(start = 10.dp)
            ) {
                Text(text = productWithAmount.product?.brand.toString(), fontSize = 13.sp)

                Text(text = productWithAmount.product?.volume.toString(), fontSize = 10.sp)

                Divider()

                if (isCashPriceState == null)
                    Row {
                        Text(text = productWithAmount.product?.cashPrice?.round(2).toString(), fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = productWithAmount.product?.cashlessPrice?.round(2).toString(), fontSize = 12.sp)
                    }
                else
                    Text(text = if (isCashPriceState.value)
                        productWithAmount.product?.cashPrice?.round(2).toString()
                    else
                        productWithAmount.product?.cashlessPrice?.round(2).toString(),
                    fontSize = 12.sp)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(end = 10.dp)
            ) {
                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                            showToast(context, "Вы не авторизованы")
                        else {
                            if (isInFavourite.value)
                                onRemoveFromFavouriteClick(productWithAmount.product?.id!!)
                            else
                                onAddToFavouriteClick(productWithAmount.product ?: Product())
                            isInFavourite.value = !isInFavourite.value
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isInFavourite.value)
                            Icons.Outlined.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "fav icon",
                        tint = if (isInFavourite.value) Gold else Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                            showToast(context, "Вы не авторизованы")
                        else {
                            if (isInCart.value) onRemoveFromCartClick(productWithAmount.product?.id!!)
                            else onAddToCartClick(productWithAmount)
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

                    Spacer(modifier = Modifier.width(5.dp))

                    if (showEditableAmount)
                         PlusMinus(initValue = productWithAmount.amount ?: 1, onValueChange = onAmountChange)
                    else
                        Text(text = productWithAmount.amount?.toString() ?: "1", fontSize = 15.sp)



            }
        }
    }
}

//
//@Composable
//fun PlusMinus(amountState : MutableState<Int>) {
//
//
//    val valueState = remember(amountState.value) {
//        mutableStateOf(amountState.value.toString())
//    }
//    Row(modifier = Modifier.wrapContentHeight()) {
//        IconButton(
//            modifier = Modifier
//                .size(15.dp)
//                .clip(CircleShape),
//            onClick = {
//                if (amountState.value > 1) amountState.value--
//            }
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.minus_icon),
//                contentDescription = "minus icon"
//            )
//        }
//
//        BasicTextField(
////        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background),
////        shape = RectangleShape,
//            textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
//            modifier = Modifier
//                .width(50.dp)
//                .height(30.dp)
//                .border(BorderStroke(width = 1.dp, color = Color.Black)),
//            value = valueState.value,
//            onValueChange = {
//                if (it.isDigitsOnly() || it.isEmpty())
//                    valueState.value = it
//                try {
//                    amountState.value = it.toInt()
//                } catch (e : Exception) {}
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number,
//                imeAction = ImeAction.Done
//            )
//        )
//
//
////    TextField(
////        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background),
////        shape = RectangleShape,
////        textStyle = TextStyle(fontSize = 10.sp),
////        modifier = Modifier
////            .width(50.dp)
////            .height(40.dp)
////            .border(BorderStroke(width = 1.dp, color = Color.Black)),
////        value = amountState.value.toString(),
////        onValueChange = {
////            try {
////                val newVal = it.toInt()
////                amountState.value = newVal
////            } catch (e : Exception){}
////        },
////        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Default)
////    )
//
//        IconButton(
//            modifier = Modifier
//                .size(15.dp)
//                .clip(CircleShape),
//            onClick = { amountState.value++ }
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.plus_icon),
//                contentDescription = "plus icon"
//            )
//        }
//    }
//}


@Composable
fun PlusMinus(initValue : Int, onValueChange : (Int) -> Unit) {

    val valueState = rememberSaveable {
        mutableStateOf(initValue.toString())
    }

    Row(modifier = Modifier.wrapContentHeight()) {
        IconButton(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape),
            onClick = {

                try {
                    var amount = valueState.value.toInt()
                    if (amount > 1) amount--
                    valueState.value = amount.toString()
                    onValueChange(amount)

                } catch (_: Exception) {}
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.minus_icon),
                contentDescription = "minus icon"
            )
        }

        BasicTextField(
//        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background),
//        shape = RectangleShape,
            textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center),
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                .border(BorderStroke(width = 1.dp, color = Color.Black)),
            value = valueState.value,
            onValueChange = {
                if (it.isDigitsOnly() || it.isEmpty())
                    valueState.value = it
                try {
                    val amount = it.toInt()
                    onValueChange(amount)
                } catch (_ : Exception) {}
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )


        IconButton(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape),
            onClick = {
                try {
                    var amount = valueState.value.toInt()
                    amount++
                    valueState.value = amount.toString()
                    onValueChange(amount)
                } catch (_: Exception) {}
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = "plus icon"
            )
        }
    }
}



//@Preview(showBackground = true)
@Composable
fun SearchOption(
        text : String,
        iconId : Int,
        showState : MutableState<Boolean>
    ) {

    val context = LocalContext.current

    val wp = getWidthPercent(context = context)

    Card(modifier = Modifier
        .wrapContentHeight()
        .width(wp * 31)
        .clickable { showState.value = !showState.value },
         shape = RoundedCornerShape(50.dp),
         colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Gold),
         border = BorderStroke(width = 2.dp, color = Gold)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            //horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                 painter = painterResource(id = iconId),
                 contentDescription = null,
                 modifier = Modifier
                     .size(20.dp)
                     .padding(start = 5.dp)
            )

            Spacer(modifier = Modifier.width(1.dp))


            //Divider(Modifier.width(1.dp))

            Text(
                text = text,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 5.dp),
                textAlign = TextAlign.Center
            )


        }

    }

}

@Composable
fun PriceDropDownMenu(
    expanded: MutableState<Boolean>,
    selected: MutableState<Boolean>
) {
    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = !expanded.value }) {


            val width = 100.dp
            val selectedBorderStroke = BorderStroke(width = 3.dp, color = Gold)
            val notSelectedBorderStroke = BorderStroke(width = 1.dp, color = Color.LightGray)
            Row(
                modifier = Modifier
                    .width(width)
                    .border(
                        if (!selected.value) selectedBorderStroke else notSelectedBorderStroke
                    )
                    .clickable {
                        selected.value = false
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Нал.", fontSize = 20.sp,
                     modifier = Modifier.padding(5.dp),
                     textAlign = TextAlign.Center)
            }
            Row(
                modifier = Modifier
                    .width(width)
                    .border(
                        if (selected.value) selectedBorderStroke else notSelectedBorderStroke
                    )
                    .clickable {
                        selected.value = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Безнал.", fontSize = 20.sp,
                     modifier = Modifier.padding(5.dp),
                     textAlign = TextAlign.Center)
            }
        //}
    }
}

@Composable
fun SortDialog(
    showDialog: MutableState<Boolean>,
    states: SnapshotStateList<Boolean>,
    priorities: SnapshotStateList<Int>,
    isAscending: MutableState<Boolean>,
    onApplyButtonClick: () -> Unit
) {

    val labels = listOf("Цене", "Объёму")

    if (showDialog.value)
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(color = Color.White, modifier = Modifier.wrapContentSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {

                    Text(text = "Сортировать по:")

                    repeat(2){ ind ->
                        Row {
                            Spacer(modifier = Modifier.width(10.dp))
                            CustomRadioButton(ind = ind, states = states, priorities = priorities) {
                                states[ind] = !states[ind]
                                if (states[ind])
                                    priorities.add(ind)
                                else
                                    priorities.remove(ind)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = labels[ind])
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row {
                       RadioButton(
                           selected = isAscending.value,
                           onClick = { isAscending.value = !isAscending.value }
                       )
                        Spacer(modifier = Modifier.width(10.dp))

                        Text(text = if (isAscending.value) "По возрастанию" else "По убыванию")
                    }

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
//@Preview(showBackground = true)
@Composable
fun CustomRadioButton(
    ind: Int,
    states : SnapshotStateList<Boolean>,
    priorities : SnapshotStateList<Int>,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick,
        modifier = Modifier.size(20.dp),
        shape = CircleShape,
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        colors = ButtonDefaults.buttonColors(containerColor =
                                             if (states[ind])
                                                 Color.Green
                                             else
                                                 MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(0.dp)
    ) {
        val text = if (states[ind]) (priorities.indexOf(ind) + 1).toString() else ""
        Text(textAlign = TextAlign.Center, text = text)
    }

}

@Composable
fun FilterDialog(
    //rangeSliderState: MutableState<ClosedFloatingPointRange<Float>>,
    moreThanState: MutableState<String>,
    lessThanState: MutableState<String>,
    showDialog: MutableState<Boolean>,
    isOnHandSelected: MutableState<Boolean>,
    states: SnapshotStateList<Boolean>,
    volumes : List<String>,
    onApplyButtonClick: () -> Unit
) {


    if (showDialog.value)
         Dialog(onDismissRequest = { showDialog.value = false}) {

        Surface(color = Color.White, modifier = Modifier.wrapContentSize()) {

           // Row() {

                // Spacer(modifier = Modifier.width(250.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {

                    FilterOption(text = "Только в наличии", height = 50.dp, selected = isOnHandSelected)

                    Row {
                        InputField(
                            modifier = Modifier.width(80.dp),
                            valueState = moreThanState,
                            label = "От",
                            enabled = true
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        InputField(
                            modifier = Modifier.width(80.dp),
                            valueState = lessThanState,
                            label = "До",
                            enabled = true
                        )
                    }

                    VolumeOption(states = states, volumes = volumes)

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
    //    }
    }
}

@Composable
fun VolumeOption (states:  SnapshotStateList<Boolean>, volumes : List<String>) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()) {

        Text(text = "Объем")

        Spacer(modifier = Modifier.width(30.dp))

        for (ind in states.indices)
            ValueButton(ind = ind, text = "${volumes[ind]} мл", states = states)

//        states.forEachIndexed{ ind, buttonState ->
//            ValueButton(ind = ind, text = "$volume мл", states)
//        }
    }
}

@Composable
fun ValueButton(
    ind : Int,
    text : String,
    states : SnapshotStateList<Boolean>
) {

    Button(
        onClick = { states[ind] = !states[ind] },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = if (states[ind]) Gold else Color.LightGray),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        Text(text = text, fontSize = 10.sp, color = Color.Black)
    }
}
@Composable
fun ValueButton(
    text : String,
    clicked : MutableState<Boolean>
) {

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