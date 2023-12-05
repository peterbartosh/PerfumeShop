package com.example.perfumeshop.presentation.features.search.components.secondary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.QueryType


@Preview(showBackground = true)
@Composable
fun InputOption(
    ind: Int = 1,
    query: String = "al",
    queryType: QueryType = QueryType.Brand,
    onCloseCLick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        )
    ) {
        val text =
            when (queryType.name) {
                QueryType.Type.name -> "Тип: ${ProductType.valueOf(query).toRus()}"
                QueryType.Brand.name -> "В названии: $query"
                QueryType.TypeVolume.name -> try {
                    val type = query.split(":")[0]
                    val volume = query.split(":")[1]
                    "Тип: ${ProductType.valueOf(type).toRus()} $volume мл"
                } catch (_: Exception) {""}
                else -> ""
            }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.bodySmall
            )

            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            if (ind == 1)
                IconButton(
                    modifier = Modifier.size(25.dp),
                    onClick = onCloseCLick
                ) {
                    Icon(
                        modifier = Modifier.size(15.dp),
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "close icon"
                    )
                }
        }
    }
}