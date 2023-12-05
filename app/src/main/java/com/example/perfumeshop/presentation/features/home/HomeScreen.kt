package com.example.perfumeshop.presentation.features.home

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.features.home.components.CollectionCard
import com.example.perfumeshop.presentation.features.home.components.CollectionItem
import com.example.perfumeshop.presentation.features.search.components.SearchField

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
            //id = "1",
            imageId = R.drawable.original_icon,
            query = ProductType.Original.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Тестеры 55мл",
            //id = "21",
            imageId = R.drawable.testers,
            query = ProductType.Tester.name + ":" + "55",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Тестеры 60мл",
            //id = "22",
            imageId = R.drawable.testers,
            query = ProductType.Tester.name + ":" + "60",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Тестеры 65мл",
            //id = "23",
            imageId = R.drawable.testers,
            query = ProductType.Tester.name + ":" + "65",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Тестеры 110мл",
            //id = "2",
            imageId = R.drawable.testers,
            query = ProductType.Tester.name + ":" + "110",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Тестеры 115мл",
            //id = "2",
            imageId = R.drawable.testers,
            query = ProductType.Tester.name + ":" + "115",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Тестеры 125мл",
            //id = "2",
            imageId = R.drawable.testers,
            query = ProductType.Tester.name + ":" + "125",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Пробники 30мл",
            //id = "3",
            imageId = R.drawable.probe,
            query = ProductType.Probe.name + ":" + "30",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Пробники 35мл",
            //id = "3",
            imageId = R.drawable.probe,
            query = ProductType.Probe.name + ":" + "35",
            queryType = QueryType.TypeVolume
        ),

        CollectionItem(
            title = "Авто",
            //id = "4",
            imageId = R.drawable.auto,
            query = ProductType.Auto.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Диффузоры",
            //id = "5",
            imageId = R.drawable.diffuser,
            query = ProductType.Diffuser.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Компакт",
            //id = "6",
            imageId = R.drawable.male_perfume,
            query = ProductType.Compact.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Лицензионное",
            //id = "7",
            imageId = R.drawable.licensed,
            query = ProductType.Licensed.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Люкс",
            //id = "8",
            imageId = R.drawable.lux,
            query = ProductType.Lux.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Селективы",
            //id = "9",
            imageId = R.drawable.male_perfume,
            query = ProductType.Selectives.name,
            queryType = QueryType.Type
        ),

        CollectionItem(
            title = "Евро А+",
            //id = "10",
            imageId = R.drawable.female_perfume,
            query = ProductType.EuroA.name,
            queryType = QueryType.Type
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

        SearchField { query ->
            if (!isUserConnected(context)){
                context.showToast(R.string.connection_lost_error)
            } else {
                onSearchClick(query, QueryType.Brand)
            }
        }

        LazyVerticalStaggeredGrid(
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.wrapContentHeight(),
            state = scrollState,
            userScrollEnabled = true,
            columns = StaggeredGridCells.Fixed(2)
        ) {
            items(
                items = collections,
                key = { coll -> coll.hashCode() }
            ) { collection ->

                CollectionCard(item = collection, onCollectionCardClick = { q, qt ->
                    if (!isUserConnected(context)){
                        context.showToast(R.string.connection_lost_error)
                    } else {
                        onSearchClick(q, qt)
                    }
                })

            }
        }
    }
}



