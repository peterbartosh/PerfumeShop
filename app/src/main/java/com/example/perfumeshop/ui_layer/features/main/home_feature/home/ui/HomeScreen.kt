package com.example.perfumeshop.ui_layer.features.main.home_feature.home.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.theme.Gold
import com.example.perfumeshop.ui_layer.theme.Pink
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.LazyProductList
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.ProductRow
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.SearchForm
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onSearchClick: (String, QueryType) -> Unit
) {

    val scrollState = rememberLazyStaggeredGridState()

    val collections = listOf(
        CollectionItem(title = "Мужское",
                       id = "1",
                       titleColor = Gold,
                       imageId = R.drawable.male_perfume,
                       query = "Male",
                       queryType = QueryType.sex),
        CollectionItem(title = "Женское",
                       id = "2",
                       titleColor = Pink,
                       imageId = R.drawable.female_perfume,
                       query = "Female",
                       queryType = QueryType.sex),
        CollectionItem(title = "Мужское",
                       id = "3",
                       titleColor = Gold,
                       imageId = R.drawable.male_perfume,
                       query = "Male",
                       queryType = QueryType.sex),
        CollectionItem(title = "Женское",
                       id = "4",
                       titleColor = Pink,
                       imageId = R.drawable.female_perfume,
                       query = "Female",
                       queryType = QueryType.sex),
        CollectionItem(title = "Мужское",
                       id = "5",
                       titleColor = Gold,
                       imageId = R.drawable.male_perfume,
                       query = "Male",
                       queryType = QueryType.sex),
        CollectionItem(title = "Женское",
                       id = "6",
                       titleColor = Pink,
                       imageId = R.drawable.female_perfume,
                       query = "Female",
                       queryType = QueryType.sex)
    )

    Column(modifier = Modifier.fillMaxSize()) {

        SearchForm { query -> onSearchClick(query, QueryType.brand) }

        LazyVerticalStaggeredGrid(
            contentPadding = PaddingValues(3.dp),
            modifier = Modifier.wrapContentHeight(),
            state = scrollState,
            userScrollEnabled = true,
            columns = StaggeredGridCells.Adaptive(minSize = 150.dp)
        ) {
            items(
                items = collections,
                //key = { product -> product.id!! }
            ) { collection ->

                CollectionCard(item = collection, onCollectionCardClick = onSearchClick)

            }
        }
    }
}



