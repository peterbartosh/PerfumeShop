package com.example.perfumeshop.presentation.features.orders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.OrderStatus
import com.example.perfumeshop.data.utils.getWidthPercent


@Composable
fun QuestionBox(status: OrderStatus) {

    val wp = LocalContext.current.getWidthPercent()

    var showAskBar by remember {
        mutableStateOf(false)
    }

    Box {

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
                    showAskBar = true
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
            expanded = showAskBar,
            onDismissRequest = { showAskBar = false }) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()

            ) {
                val textInd = when (status){
                    OrderStatus.Processing -> R.string.processing_status
                    OrderStatus.Accepted -> R.string.accepted_status
                    OrderStatus.Delivering -> R.string.delivering_status
                    OrderStatus.Succeed -> R.string.succeed_status
                    OrderStatus.Canceled -> R.string.canceled_status
                }
                Text(
                    text = stringResource(id = textInd),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

