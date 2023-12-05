package com.example.perfumeshop.presentation.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.theme.arialTypography


data class CollectionItem(
    //val id : String = "id",
    val title : String,
    val imageId : Int,
    val query : String,
    val queryType: QueryType
)

@Composable
fun CollectionCard(
    item: CollectionItem,
    onCollectionCardClick: (String, QueryType) -> Unit,
) {

    val wp = LocalContext.current.getWidthPercent()

    Card(
        modifier = Modifier
            //.height(wp * 40)
            .wrapContentHeight()
            .wrapContentWidth()
            .clip(RoundedCornerShape(15.dp))
            .clickable { onCollectionCardClick(item.query, item.queryType) }
            .padding(10.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier.wrapContentHeight().wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Bottom
            ) {

                Image(
                    painter = painterResource(id = item.imageId),
                    contentDescription = "collection image",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(wp * 40)
                )

                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.primary,
                    style = arialTypography.bodyMedium,
                    //fontFamily = arialFont,
                    //fontStyle = FontStyle.Italic,
                    fontSize = 15.sp, modifier = Modifier.padding(bottom = 10.dp)
                )
            }
        }

    }
}
