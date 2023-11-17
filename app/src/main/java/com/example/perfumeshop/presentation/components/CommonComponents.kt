package com.example.perfumeshop.presentation.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.data.utils.round
import com.example.perfumeshop.data.utils.toBrandFormat
import com.example.perfumeshop.presentation.theme.FingersShape1
import com.example.perfumeshop.presentation.theme.FingersShape2
import com.example.perfumeshop.presentation.theme.Gold
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
fun Loading() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingAnimation()
    }
}

fun Context.showToast(stringId : Int, duration: Int = Toast.LENGTH_SHORT){
    val toast = Toast.makeText(this, this.getString(stringId), duration)
    toast.show()
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
    val toast = Toast.makeText(this, message, duration)
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
                context.showToast(R.string.incorrect_input_error)
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
    //onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (ProductWithAmount) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,

    onAmountChanged: (String, Int, Int) -> Unit = { _, _, _ ->},

    userScrollEnabled : Boolean = true,
    showNotValidProducts: MutableState<Boolean>? = null,
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
        items(
            items = listOfProductsWithAmounts,
            key = { productWithAmount ->
                productWithAmount.product?.id ?: productWithAmount.hashCode()
            }
        ) { productWithAmount ->

            ProductRow(
                productWithAmount = productWithAmount,
                //onProductClick = onProductClick,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck,
                onAmountChange = { cashAmount, cashlessAmount ->
                    productWithAmount.product?.id?.let { id ->
                        onAmountChanged(id, cashAmount, cashlessAmount)
                    }
                },
                showNotValidProducts = showNotValidProducts
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
    //onProductClick : (String) -> Unit,
    onAddToFavouriteClick : (ProductWithAmount) -> Unit,
    onAddToCartClick : (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick : (ProductWithAmount) -> Unit,
    onRemoveFromCartClick : (ProductWithAmount) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean,
    showNotValidProducts: MutableState<Boolean>? = null,

    onAmountChange : (Int, Int) -> Unit = {_,_->}
) {

    val context = LocalContext.current


    val isInCart = remember {
        mutableStateOf(isInCartCheck(productWithAmount.product?.id ?: ""))
    }

    val isInFavourite = remember {
        mutableStateOf(isInFavouriteCheck(productWithAmount.product?.id ?: ""))
    }

    val amountCashPrice = rememberSaveable {
        mutableStateOf(productWithAmount.cashPriceAmount ?: 1)
    }

    val amountCashlessPrice = rememberSaveable {
        mutableStateOf(productWithAmount.cashlessPriceAmount ?: 1)
    }

    val scale = remember {
        Animatable(1f)
    }

    var saveErrorState by remember {
        mutableStateOf(false)
    }

    if (showNotValidProducts?.value == true) {
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.8f,
                animationSpec = tween(200)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(200)
                //tween(durationMillis = 200)
            )
            saveErrorState = true
            showNotValidProducts.value = false
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(10.dp),
        border = if (
            saveErrorState && amountCashPrice.value == 0 && amountCashlessPrice.value == 0
        )
            BorderStroke(width = 2.dp, color = Color.Red)
        else {
            if (productWithAmount.product?.isOnHand == false)
                BorderStroke(width = 2.dp, color = Color.Red)
            else if (isInCart.value)
                BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.primary)
            else
                BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues)
            .clip(RoundedCornerShape(10.dp))
            .shadow(elevation = 5.dp)
            .scale(if (amountCashPrice.value == 0 && amountCashlessPrice.value == 0) scale.value else 1f)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                //.clickable { onProductClick(productWithAmount.product?.id.toString()) },
            //verticalArrangement = Arr,
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 10.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .wrapContentHeight()
                ) {

                    Text(
                        text = productWithAmount.product?.brand.toString().toBrandFormat(),
                        style = MaterialTheme.typography.bodyLarge
                    )


                    productWithAmount.product?.type?.let { type ->
                        val typeText = try {
                            ProductType.valueOf(type).toRus()
                        } catch (e : Exception){
                            ProductType.NotSpecified.toRus()
                        }

                        Text(
                            text = typeText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    val volumeDouble = productWithAmount.product?.volume?.round(1)

                    val volume = try {
                        val volumeInt = volumeDouble?.toInt()
                        if (volumeInt == 0)
                            null
                        else if (volumeInt == 60 && productWithAmount.product?.type == ProductType.Compact.name)
                            "3x20"
                        else
                            volumeInt?.toString()
                    } catch (e: Exception) {
                        volumeDouble?.toString()
                    }

                    if (volume != null)
                        Text(
                            text = volume + "мл.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                }

                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .padding(top = 5.dp, end = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    MoreOption(brand = productWithAmount.product?.brand.toString())

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(
                        modifier = Modifier.size(30.dp),
                        onClick = {
                            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == true)
                                context.showToast(R.string.you_are_not_authorized_error)
                            else {
                                if (isInFavourite.value)
                                    onRemoveFromFavouriteClick(productWithAmount)
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
                                context.showToast(R.string.you_are_not_authorized_error)
                            else if (productWithAmount.product?.isOnHand == true) {
                                if (isInCart.value) {
                                    onRemoveFromCartClick(productWithAmount)
                                } else
                                    onAddToCartClick(
                                        ProductWithAmount(
                                            product = productWithAmount.product,
                                            cashPriceAmount = amountCashPrice.value,
                                            cashlessPriceAmount = amountCashlessPrice.value
                                        )
                                    )

                                isInCart.value = !isInCart.value
                            } else if (isInCart.value) {
                                onRemoveFromCartClick(productWithAmount)
                                isInCart.value = !isInCart.value
                            } else {
                                context.showToast(R.string.is_not_on_hand)
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
            }

            if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false) {

                Divider()
                Spacer(modifier = Modifier.height(5.dp))

                repeat(2) { ind ->

                    val priceAnnotatedStrings =
                        buildPriceAnnotationStrings(productWithAmount = productWithAmount)
                    val prices = listOf(
                        (productWithAmount.cashPriceAmount ?: 1),
                        (productWithAmount.cashlessPriceAmount ?: 1)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 10.dp, bottom = 5.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                        //verticalArrangement = Arrangement.SpaceBetween,
                        //horizontalAlignment = Alignment.End
                    ) {

                        Text(
                            modifier = Modifier,
                            text = priceAnnotatedStrings[ind],
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (!showEditableAmount) {
                            Text(
                                modifier = Modifier,
                                text = prices[ind].toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            if (productWithAmount.product?.isOnHand == false) {
                                if (ind == 1)
                                    Text(
                                        modifier = Modifier.padding(bottom = 5.dp),
                                        text = "Нет в наличии",
                                        style = MaterialTheme.typography.bodyLarge,
                                        softWrap = true,
                                        lineHeight = 15.sp,
                                        color = Color.Red
                                    )
                            }
                            else
                                PlusMinus(
                                    initValue = prices[ind],
                                    onValueChange = { changedAmount ->
                                        if (ind == 0)
                                            amountCashPrice.value = changedAmount
                                        else
                                            amountCashlessPrice.value = changedAmount

                                        onAmountChange(
                                            amountCashPrice.value,
                                            amountCashlessPrice.value
                                        )
                                    }
                                )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun buildPriceAnnotationStrings(productWithAmount: ProductWithAmount) : List<AnnotatedString> {
    val cashPrice = buildAnnotatedString {
        withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
            val first = "$ " + productWithAmount.product?.cashPrice?.round(2).toString()
            append(first)
        }
    }

    val cashlessPrice = buildAnnotatedString {
        withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
            val first = "$ " + productWithAmount.product?.cashlessPrice?.round(2).toString()
            append(first)
        }
    }

    return listOf(cashPrice, cashlessPrice)
}


@Composable
fun MoreOption(brand: String) {

    val context = LocalContext.current

    val wp = getWidthPercent(context = context)

    var showFullProductName by remember {
        mutableStateOf(false)
    }

    Text(
        modifier = Modifier
            .size(25.dp)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = CircleShape
            )
            .clip(shape = CircleShape)
            //.shadow(elevation = 5.dp)
            .clickable {
                showFullProductName = true
            },
        text = "?",
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
    )

    DropdownMenu(
        modifier = Modifier
            .width(wp * 80)
            .wrapContentHeight()
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(5.dp)
            ),
        expanded = showFullProductName,
        onDismissRequest = { showFullProductName = false }) {

        Column(
            modifier = Modifier
                .wrapContentWidth()
            //.wrapContentHeight()

        ) {
            Text(
                text = brand,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                style = MaterialTheme.typography.bodySmall
            )
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
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier
                .size(25.dp)
                .padding(end = 3.dp)
                .clip(CircleShape),
            onClick = {

                try {
                    if (valueState.value.isEmpty()) valueState.value = "0"
                    var amount = valueState.value.toInt()
                    if (amount > 0) amount--
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

        LaunchedEffect(key1 = valueState.value, key2 = isFocused){
            Log.d("DKOSJIO", "PlusMinus: ${valueState.value} ${isFocused}")
            if (valueState.value.isEmpty() && !isFocused)
                valueState.value = "0"
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
                .padding(start = 3.dp)
                .clip(CircleShape),
            onClick = {
                try {
                    if (valueState.value.isEmpty()) valueState.value = "0"
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


