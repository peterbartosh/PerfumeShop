package com.example.perfumeshop.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.getWidthPercent

@Preview
@Composable
fun MoreInfoOptionPreview() {

}

@Composable
fun MoreInfoOption(brand: String) {

    val context = LocalContext.current

    val wp = context.getWidthPercent()

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




