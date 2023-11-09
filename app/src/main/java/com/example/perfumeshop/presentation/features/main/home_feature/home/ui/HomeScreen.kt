package com.example.perfumeshop.presentation.features.main.home_feature.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.home_feature.search.ui.SearchForm
import com.example.perfumeshop.presentation.theme.Gold

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onSearchClick: (String, QueryType) -> Unit
) {

    val context = LocalContext.current

    val scrollState = rememberLazyStaggeredGridState()

    val collections = listOf(
        CollectionItem(
            title = "Оригиналы",
            id = "1",
            titleColor = Gold,
            imageId = R.drawable.originals,
            query = ProductType.Original.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Тестеры",
            id = "2",
            titleColor = Gold,
            imageId = R.drawable.testers,
            query = ProductType.Tester.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Пробники",
            id = "3",
            titleColor = Color.Black,
            imageId = R.drawable.probe,
            query = ProductType.Probe.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Авто",
            id = "4",
            titleColor = Color.Black,
            imageId = R.drawable.auto,
            query = ProductType.Auto.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Диффузоры",
            id = "5",
            titleColor = Gold,
            imageId = R.drawable.diffuser,
            query = ProductType.Diffuser.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Компакт",
            id = "6",
            titleColor = Gold,
            imageId = R.drawable.male_perfume,
            query = ProductType.Compact.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Лицензионное",
            id = "7",
            titleColor = Color.Black,
            imageId = R.drawable.licensed,
            query = ProductType.Licensed.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Люкс",
            id = "8",
            titleColor = Color.Black,
            imageId = R.drawable.lux,
            query = ProductType.Lux.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Селективы",
            id = "9",
            titleColor = Gold,
            imageId = R.drawable.male_perfume,
            query = ProductType.Selectives.name,
            queryType = QueryType.type
        ),

        CollectionItem(
            title = "Евро А+",
            id = "10",
            titleColor = Gold,
            imageId = R.drawable.female_perfume,
            query = ProductType.EuroA.name,
            queryType = QueryType.type
        ),


//        CollectionItem(
//            title = "Люкс",
//            id = "7",
//            titleColor = Pink,
//            imageId = R.drawable.lux,
//            query = ProductType.Lux.name,
//            queryType = QueryType.type
//        )
    )

    Column(modifier = Modifier.fillMaxSize()) {

        SearchForm { query ->
            if (!isUserConnected(context)){
                showToast(context, "Ошибка.\nВы не подключены к сети.")
            } else {
                onSearchClick(query, QueryType.brand)
            }
        }

        LazyVerticalStaggeredGrid(
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.wrapContentHeight(),
            state = scrollState,
            userScrollEnabled = true,
            columns = StaggeredGridCells.Adaptive(minSize = 150.dp)
        ) {
            items(
                items = collections,
                key = { coll -> coll.id }
            ) { collection ->

                CollectionCard(item = collection, onCollectionCardClick = { q, qt ->
                    if (!isUserConnected(context)){
                        showToast(context, "Ошибка.\nВы не подключены к сети.")
                    } else {
                        onSearchClick(q, qt)
                    }
                })

            }
        }
    }
}



