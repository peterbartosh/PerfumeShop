package com.example.perfumeshop.presentation.features.main.cart_feature.order_making.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.allFirstLettersToUpperCase
import com.example.perfumeshop.data.utils.round

@Composable
fun ProductBox(productWithAmount: ProductWithAmount) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(top = 3.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(5.dp)
        ) {

            Text(
                text = productWithAmount.product?.brand.toString().allFirstLettersToUpperCase(),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                ) {

                    Text(
                        text = "Кол-во (нал): ${productWithAmount.cashPriceAmount ?: 1}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Кол-во (без): ${productWithAmount.cashlessPriceAmount ?: 1}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                val priceStr = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.bodyMedium.toSpanStyle()) {
                        append("Общая цена: ")
                    }
                    withStyle(style = MaterialTheme.typography.bodyLarge.toSpanStyle()) {
                        productWithAmount.product?.cashPrice?.let { cashP ->
                            productWithAmount.product?.cashlessPrice?.let { cashlessP ->
                                val cashSum = (cashP * (productWithAmount.cashPriceAmount ?: 1))
                                val cashlessSum = (cashlessP * (productWithAmount.cashlessPriceAmount ?: 1))
                                append("${(cashSum + cashlessSum).round(2)}")
                            }
                        }
                    }
                }

                Text(text = priceStr)
            }
        }

    }
}