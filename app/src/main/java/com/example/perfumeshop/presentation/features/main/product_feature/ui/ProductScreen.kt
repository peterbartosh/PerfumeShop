package com.example.perfumeshop.presentation.features.main.product_feature.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getHeightPercent
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductScreen(
    productId: String,
    productViewModel: ProductViewModel,
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    val photoUrlIds = listOf(R.string.product_photo_1, R.string.product_photo_2, R.string.product_photo_3)
    val photoUrls = List(3) { stringResource(id = photoUrlIds[it]) }

    val isFullScreen = remember{
        mutableStateOf(false)
    }

    val hp = getHeightPercent(context = LocalContext.current)

    val scrollState = rememberLazyListState()

    val reviewsExpanded = remember {
        mutableStateOf(false)
    }


    LazyColumn(state = scrollState) {

        item {
            Pager(recourses = photoUrls, isFullScreenMode = isFullScreen, hp = hp)
        }

        if (!isFullScreen.value) {

            item {
                ReviewLabel(reviewsExpanded = reviewsExpanded)
            }

            if (reviewsExpanded.value)
                item {
                    if (productViewModel.isSuccess)
                        ReviewList(listOfReviews = productViewModel.productReviews.collectAsState().value)
                    else if (productViewModel.isLoading)
                        LoadingIndicator()
                    else if (productViewModel.productReviews.collectAsState().value.isEmpty())
                        Text(text = "Отзывов не найдено")
                    else
                        Text(text = "ERROR")
                }
        }
    }

}
