package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.data_layer.utils.getHeightPercent
import com.example.perfumeshop.data_layer.utils.getWidthPercent


data class CollectionItem(
    val id : String = "id",
    val title : String,
    val titleColor : Color,
    val imageId : Int,
    val query : String,
    val queryType: QueryType
)

@Composable
fun CollectionCard(
    item: CollectionItem,
    onCollectionCardClick: (String, QueryType) -> Unit,
) {

    val wp = getWidthPercent(context = LocalContext.current)
    val hp = getHeightPercent(context = LocalContext.current)

    Card(
        modifier = Modifier
            .clickable {
                onCollectionCardClick(item.query, item.queryType)
            }
            .height(hp * 25)
            .width(hp * 25)
    ) {

        Box (modifier = Modifier.fillMaxSize()){

            Image(
                painter = painterResource(id = item.imageId),
                contentDescription = "collection image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Column(modifier = Modifier.fillMaxSize(),
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Bottom) {
                Text(text = item.title, color = item.titleColor,
                     fontSize = 20.sp, modifier = Modifier.padding(bottom = 10.dp))
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun CollectionCardPreview() {
    val item = CollectionItem(title = "Мужское",
                              titleColor = Color.White,
                              imageId = R.drawable.male_perfume,
                              query = "Male",
                              queryType = QueryType.sex)

    CollectionCard(item = item){ q, t -> }
}