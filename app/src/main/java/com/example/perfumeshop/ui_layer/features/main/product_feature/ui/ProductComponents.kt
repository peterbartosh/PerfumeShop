package com.example.perfumeshop.ui_layer.features.main.product_feature.ui

import android.os.Build
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.example.perfumeshop.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.utils.getDateTimeString
import com.example.perfumeshop.ui_layer.theme.Gold
import java.sql.Timestamp
import java.time.LocalDateTime


@Composable
fun ReviewLabel(reviewsExpanded : MutableState<Boolean>)  {


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(start = 10.dp, end = 10.dp)
            .background(Gold)

    ) {
        Text(text = "Отзывы", color = Color.Black, modifier = Modifier.padding(start = 5.dp))
        Icon(imageVector = if (reviewsExpanded.value)
            Icons.Default.KeyboardArrowUp
        else
            Icons.Default.KeyboardArrowDown,
             modifier = Modifier.clickable { reviewsExpanded.value = !reviewsExpanded.value },
             contentDescription = null)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(recourses : List<String>, isFullScreenMode : MutableState<Boolean>, hp : Dp) {

    val pagerState = rememberPagerState()

    val heightValue by remember(isFullScreenMode.value) {
        mutableStateOf(if (isFullScreenMode.value) 80 else 40)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(hp * heightValue)
        ) {
            HorizontalPager(
                pageCount = recourses.size,
                state = pagerState,
                key = { recourses[it] },
                pageSize = PageSize.Fill
            ) { index ->
                AsyncImage(
                    model = recourses[index],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { isFullScreenMode.value = true }
                )
            }


            if (isFullScreenMode.value)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(modifier = Modifier.size(40.dp),
                               onClick = { isFullScreenMode.value = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = Gold,
                            contentDescription = "close icon"
                        )
                    }
                }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewList(listOfReviews : List<Review>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        //listOfReviews.forEach { review ->
        repeat(5) {
            ReviewRow()
            Divider(modifier = Modifier.fillMaxWidth(0.8f))
            // }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun ReviewRow(review: Review = Review(authorName = "Владислав Александрович",
                                      rating = 3,
                                      date = null,
                                      content = "OOOOOOOOOOOOOOOOOOOOO" +
                                              "OOOOOOOOOOOOOOOOOOOOOOO" +
                                              "OOOOOOOOOOOOOOOOOOOOOO" +
                                              "OOOOOOOOOOOOOOOOOO" +
                                              "OOOOOOOOOOOOOOOOOOOOOO")) {

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(3.dp)
            ) {
                Text(text = review.authorName ?: "Не найдно", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(5.dp))
                RatingBar(ratingVal = review.rating ?: 5)
            }

            Text(
                text = getDateTimeString(review.date),
                modifier = Modifier.padding(3.dp),
                fontSize = 12.sp
            )

            Log.d("TIMESTAMP_TEST", "ReviewRow: ${getDateTimeString(review.date)}")

        }

        Text(text = review.content.toString(), fontSize = 16.sp,
             modifier = Modifier
                 .fillMaxWidth()
                 .height(60.dp)
                 .padding(5.dp))

    }


}

@Composable
fun RatingBar(ratingVal : Int) {
    Row(
        modifier = Modifier.wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(id = R.drawable.star_anum),
                contentDescription = "star",
                tint = if (i <= ratingVal) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun RatingBarAnim(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }

    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_anum),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}

