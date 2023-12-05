package com.example.perfumeshop.presentation.features.cart.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.round
import com.example.perfumeshop.data.utils.showToast

@Preview
@Composable
fun UpperSectionPreview() {
    Column() {
        UpperSection(userProducts = emptyList()) {}
    }
}

@Composable
fun UpperSection(
    userProducts: List<ProductWithAmount>,
    clearData: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val totalStr = buildAnnotatedString {

            withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle()){
                append("Итого: ")
            }

            withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()){
                val total = userProducts.sumOf { productWithAmount ->

                    val cashSum = ((productWithAmount.product?.cashPrice ?: 0.0) * (productWithAmount.cashPriceAmount ?: 1))
                    val cashlessSum = ((productWithAmount.product?.cashlessPrice ?: 0.0) * (productWithAmount.cashlessPriceAmount ?: 1))
                    cashSum + cashlessSum
                }.round(2)

                append(total.toString())
            }
        }

        Text(
            text = totalStr,
            modifier = Modifier.padding(start = 5.dp),
        )

        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(end = 10.dp)
                .clickable {
                    clearData()
                    context.showToast(R.string.content_cleared)
                },
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            text = "Очистить корзину",
        )
    }
}