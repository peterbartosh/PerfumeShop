package com.example.perfumeshop.presentation.features.cart.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.perfumeshop.R
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.components.SubmitButton
import com.google.firebase.auth.FirebaseAuth

@Preview
@Composable
fun CartSubmitButtonPreview() {
    val showNotValidProducts = remember {
        mutableStateOf(false)
    }
    CartSubmitButton(
        userProducts = emptyList(),
        auth = FirebaseAuth.getInstance(),
        validState = true,
        showNotValidProducts = showNotValidProducts
    ) {}
}

@Composable
fun CartSubmitButton(
    userProducts: List<ProductWithAmount>,
    auth: FirebaseAuth,
    validState: Boolean,
    showNotValidProducts: MutableState<Boolean>,
    onOrderMakeClick: () -> Unit
) {

    val context = LocalContext.current

    SubmitButton(
        validInputsState = validState,
        text = context.getString(R.string.cart_screen_submit_button_text),
    ) {
        if (auth.currentUser?.isAnonymous == false) {
            if (userProducts.any { productWA ->
                    productWA.product?.isOnHand == false
                }) {
                showNotValidProducts.value = true
                context.showToast(R.string.cart_screen_is_not_on_hand_error)
                return@SubmitButton
            }

            if (userProducts.any { productWA ->
                    productWA.cashPriceAmount == 0 &&
                            productWA.cashlessPriceAmount == 0
                }
            ){
                showNotValidProducts.value = true
                context.showToast(R.string.cart_screen_zero_amount_error)
                return@SubmitButton
            }

            onOrderMakeClick()

        }
        else
            context.showToast(R.string.you_are_not_authorized_error)
    }
}