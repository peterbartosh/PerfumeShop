package com.example.perfumeshop.presentation.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.perfumeshop.R
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.user_preferences.PreferencesManager
import com.example.perfumeshop.data.utils.getVolume
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.data.utils.round
import com.example.perfumeshop.data.utils.toBrandFormat
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.SegmentButton
import com.example.perfumeshop.presentation.theme.Gold
import com.example.perfumeshop.presentation.theme.FingersShape1
import com.example.perfumeshop.presentation.theme.FingersShape2
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChildScreenAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = slideInVertically(initialOffsetY = { -40 }) +
                expandVertically(expandFrom = Alignment.Top) +
                fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}

@Composable
fun ErrorComp(imageInd : Int, textInd : Int) {
    val context = LocalContext.current

    val wp = getWidthPercent(context = context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier
                .width(wp * 35)
                .wrapContentHeight()
                .padding(bottom = 10.dp),
            painter = painterResource(id = imageInd),
            contentDescription = "nothing found",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )

        Text(
            text = stringResource(id = textInd),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}
@Preview(showBackground = true)
@Composable
fun NothingFound() {
    ErrorComp(imageInd = R.drawable.nothing_found, textInd = R.string.nothing_found)
}

@Composable
fun ErrorOccurred() {
    ErrorComp(imageInd = R.drawable.error_occured, textInd = R.string.error_occured)
}

@Composable
fun LoadingAnimation() {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading)
    )

    val dynamicProperties = rememberLottieDynamicProperties(

        // loading text

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "L",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "O",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "A",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "D",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "I",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "N",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "G",
                "Fill 1"
            )
        ),

        // hand/thump

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Hand/Thump",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Hand/Thump",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Hand/Thump",
                "Shape 3",
                "Fill 1"
            )
        ),

        // shadow

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
            keyPath = arrayOf(
                "shadow",
                "Shape 1",
                "Fill 1"
            )
        ),

        // fingers

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Index_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Index_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Middle_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Middle_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Ring_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Ring_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "little_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "little_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        //
    )

    LottieAnimation(
        modifier = Modifier
            .size(200.dp),
        iterations = Int.MAX_VALUE,
        composition = composition,
        dynamicProperties = dynamicProperties
    )

}

@Composable
fun LoadingIndicator(progress : MutableState<Float> = mutableStateOf(0.0f)) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LoadingAnimation()

//        Text(text = "Loading...")
//        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

fun showToast(context : Context, text : String){
    val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
    toast.show()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 5.dp, end = 5.dp),
    valueState: MutableState<String>,
    onValueChange : (String) -> Unit = { valueState.value = it},
    label: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable() (() -> Unit)? = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder : @Composable () -> Unit = {}
) {

    OutlinedTextField(
        value = valueState.value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
        singleLine = isSingleLine,
        textStyle = textStyle,
        modifier = modifier.height(66.dp),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        placeholder = placeholder,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

}


@Composable
fun SubmitButton(
    text: String,
    validInputsState: Boolean? = null,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    Button(
        onClick = {
            if (validInputsState != false)
                onClick()
            else
                showToast(context, "Введены некорректные данные!")

        },
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Gold),
        enabled = validInputsState ?: true,
        shape = CircleShape
    ) {
        Text(text = text, modifier = Modifier.padding(5.dp), style = MaterialTheme.typography.bodyMedium)
    }

}


@Composable
fun LazyProductList(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(start = 5.dp, end = 5.dp),
    listOfProductsWithAmounts : List<ProductWithAmount>,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,

    onAmountChanged: (Int, Int) -> Unit = { _, _ ->},
    onCashStateChanged : (Int, Boolean) -> Unit = { _,_ -> },

    userScrollEnabled : Boolean = true,
    UploadMoreButton :  @Composable () -> Unit = { Box{} }
) {

    //Spacer(modifier = Modifier.height(5.dp))

    LazyColumn(
        contentPadding = PaddingValues(7.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues),
        state = rememberLazyListState(),
        userScrollEnabled = userScrollEnabled,
    ) {
        itemsIndexed(
            items = listOfProductsWithAmounts,
            key = { ind, productWithAmount ->
                productWithAmount.product?.id ?: ind
            }
        ) { ind, productWithAmount ->

            ProductRow(
                productWithAmount = productWithAmount,
                onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck,
                onAmountChange = { value -> onAmountChanged(ind, value) },
                onCashStateChanged = { value -> onCashStateChanged(ind, value == 0) }
            )
        }

        item {
            UploadMoreButton()
        }
    }
}

@Composable
fun ProductRow(
    paddingValues: PaddingValues = PaddingValues(2.dp),
    showEditableAmount : Boolean = true,
    productWithAmount: ProductWithAmount,
    onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,

    onAmountChange : (Int) -> Unit = {},
    onCashStateChanged : (Int) -> Unit = {}
) {

    val context = LocalContext.current

    val isInCart = remember {
        mutableStateOf(isInCartCheck(productWithAmount.product?.id ?: ""))
    }

    val isInFavourite = remember {
        mutableStateOf(isInFavouriteCheck(productWithAmount.product?.id ?: ""))
    }

    // 0 - yes, 1 - no
    val isCashPrice = rememberSaveable {
        mutableStateOf(if (productWithAmount.isCashPrice == true) 0 else 1)
    }

    LaunchedEffect(key1 = isCashPrice.value){
        onCashStateChanged(isCashPrice.value)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isInCart.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues)
            .clip(RoundedCornerShape(10.dp))
            .shadow(elevation = 5.dp)
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
                    .fillMaxWidth(0.7f)
                    .wrapContentHeight()
                    .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = productWithAmount.product?.brand.toString().toBrandFormat("Не найдено"),
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = productWithAmount.product?.brand.toString().getVolume("Не найдено"),
                    //productWithAmount.product?.volume.toString() + " мл.",
                    style = MaterialTheme.typography.bodyMedium
                )

                val cashPrice = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle()) {
                        append(productWithAmount.product?.cashPrice?.round(2).toString())
                    }
                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()){
                        append(" (нал.)")
                    }
                }

                val cashlessPrice = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle()) {
                        append(productWithAmount.product?.cashlessPrice?.round(2).toString())
                    }
                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()){
                        append(" (безнал.)")
                    }
                }


                if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
                    Row {
                        SegmentButton(
                            text = "",
                            ind = 0,
                            defaultInd = 0,
                            borderWidth = 2,
                            selectedInd = isCashPrice,
                            contentPadding = PaddingValues(5.dp),
                            shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                        ){
                            Text(
                                text = cashPrice,
                                //style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        SegmentButton(
                            text = "",
                            ind = 1,
                            defaultInd = 0,
                            borderWidth = 2,
                            selectedInd = isCashPrice,
                            contentPadding = PaddingValues(5.dp),
                            shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                        ){
                            Text(
                                text = cashlessPrice,
                                //style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                    }

            }


            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(end = 10.dp),
                //verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .padding(top = 5.dp, end = 5.dp)
                ) {

                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = {
                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                showToast(context, "Вы не авторизованы")
                            else {
                                if (isInFavourite.value)
                                    productWithAmount.product?.id?.let { id ->
                                        onRemoveFromFavouriteClick(id)
                                    }
                                else
                                    onAddToFavouriteClick(productWithAmount)

                                isInFavourite.value = !isInFavourite.value
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = if (isInFavourite.value)
                                Icons.Outlined.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = "fav icon",
                            tint = if (isInFavourite.value)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = {
                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                showToast(context, "Вы не авторизованы")
                            else {
                                if (isInCart.value) {
                                    productWithAmount.product?.id?.let { pid ->
                                        onRemoveFromCartClick(pid)
                                    }
                                }
                                else
                                    onAddToCartClick(
                                        ProductWithAmount(
                                            product = productWithAmount.product,
                                            amount = productWithAmount.amount,
                                            isCashPrice = isCashPrice.value == 0
                                        )
                                    )

                                isInCart.value = !isInCart.value
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = if (isInCart.value)
                                Icons.Filled.ShoppingCart
                            else
                                Icons.Outlined.ShoppingCart,
                            contentDescription = "cart icon",
                            tint = if (isInCart.value)
                                       MaterialTheme.colorScheme.primary
                                   else
                                       MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (showEditableAmount)
                    PlusMinus(
                        initValue = productWithAmount.amount ?: 1,
                        onValueChange = onAmountChange
                    )
                else
                    Text(text = productWithAmount.amount?.toString() ?: "1", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}


@Composable
fun PlusMinus(
    initValue : Int,
    onValueChange : (Int) -> Unit
) {

    val context = LocalContext.current

    val valueState = rememberSaveable {
        mutableStateOf(initValue.toString())
    }

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(bottom = 5.dp)
    ) {

        IconButton(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .padding(end = 3.dp),
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
                contentDescription = "minus icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        var isFocused by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = valueState.value){
            if (valueState.value.isEmpty() && !isFocused)
                valueState.value = "1"
        }

        val preferencesManager = remember {
            PreferencesManager(context)
        }

        val textColor = remember(preferencesManager.getThemeData(0)) {
            mutableStateOf(
                if (preferencesManager.getThemeData(0) == 1)
                    Color(0xFF212121)
                else
                    Color(0xFFFDF5E2)
            )
        }

        BasicTextField(
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                //.wrapContentWidth()
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(5.dp)
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
                //.padding(start = 5.dp, end = 5.dp),
            textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center, color = textColor.value),
            value = valueState.value,
            onValueChange = { newVal ->
                if (newVal.isDigitsOnly() || newVal.isEmpty())
                    valueState.value = newVal
                try {
                    val amount = newVal.toInt()
                    onValueChange(amount)
                } catch (_ : Exception) {}
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        ){ innerTextField ->
                Row(
                    //modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    innerTextField()
                }
        }



        IconButton(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .padding(start = 3.dp),
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
                contentDescription = "plus icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


