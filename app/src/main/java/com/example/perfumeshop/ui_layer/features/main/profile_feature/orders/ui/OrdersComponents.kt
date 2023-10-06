package com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data_layer.models.Order

@Composable
fun OrderRow(order : Order, onProductClick : (String) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable { onProductClick(order.productIds?.get(0)?.keys?.first() ?: "Not found" ) },
        border = BorderStroke(width = 1.dp, color = Color.Black)) {

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            Text(text = order.id ?: "Not found", modifier = Modifier.padding(5.dp))
        }

    }
}