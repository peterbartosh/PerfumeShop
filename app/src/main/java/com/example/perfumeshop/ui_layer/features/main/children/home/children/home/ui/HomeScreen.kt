package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.data_layer.utils.Sex
import com.example.perfumeshop.ui_layer.theme.Gold
import com.example.perfumeshop.ui_layer.theme.Pink
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.LazyProductList
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.SearchForm
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel

@Composable
fun HomeScreen(
    onSearchClick: (String, QueryType) -> Unit,
    onProductClick: (String) -> Unit,
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    val items = listOf(CollectionItem(title = "Мужское",
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
                                      queryType = QueryType.sex))

    Surface(modifier = Modifier) {

        if (homeViewModel.isSuccess) {

            val scrollState = rememberLazyListState()

            LazyColumn(state = scrollState) {

                item {
                    SearchForm { query -> onSearchClick(query, QueryType.brand) }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }



                item {
                    val rowScrollState = rememberLazyListState()
                    LazyRow(
                        modifier = Modifier.wrapContentHeight(),
                        state = rowScrollState,
                        contentPadding = PaddingValues(5.dp)
                    ) {
                        items(items, key = { item -> item.id }) { item ->
                            CollectionCard(
                                item = item,
                                onCollectionCardClick = onSearchClick
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }

                item {
                    val listOfProducts = homeViewModel.hotListOfProducts.collectAsState().value
                    Log.d("SIZE_SIZE", listOfProducts.size.toString())
                    LazyProductList(
                        modifier = Modifier.height((155 * (listOfProducts.size/2 + 1)).dp),
                        userScrollEnabled = false,
                        listOfProducts = listOfProducts,
                        onProductClick = onProductClick,
                        onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                        onAddToCartClick = cartViewModel::addToCart,
                        onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                        onRemoveFromCartClick = cartViewModel::removeFromCart,
                        isInFavouriteCheck = favouriteViewModel::isInFavourite,
                        isInCartCheck = cartViewModel::isInCart
                    )
                }
            }
        } else LoadingIndicator()
    }

}



