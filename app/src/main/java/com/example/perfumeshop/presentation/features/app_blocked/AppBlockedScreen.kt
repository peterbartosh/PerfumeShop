package com.example.perfumeshop.presentation.features.app_blocked

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.data.utils.isUserConnected
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.components.Loading

/*
If admin enables work mode with admin app, this screen is showing.
Further interactions available only when work mode is turned-off.
 */
@Composable
fun AppBlockedScreen(
    navigateHome: () -> Unit,
    navigateAsk: () -> Unit,
    appBlockedViewModel: AppBlockedViewModel
) {

    val context = LocalContext.current

    val wp = context.getWidthPercent()

    var refreshClicked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = refreshClicked){
        if (isUserConnected(context)) {
            appBlockedViewModel.refresh(navigateHome, navigateAsk)
                .invokeOnCompletion {
                    refreshClicked = false
                }
        } else context.showToast(R.string.connection_lost_error)
    }

    if (!refreshClicked) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 20.dp),
                    text = stringResource(id = R.string.app_blocked_upper),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Image(
                    modifier = Modifier
                        .width(wp * 35)
                        .wrapContentHeight()
                        .padding(bottom = 10.dp),
                    painter = painterResource(id = R.drawable.app_blocked_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )

                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = stringResource(id = R.string.app_blocked_bottom),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                modifier = Modifier
                    .size(70.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                onClick = { refreshClicked = true }
            ) {
                Icon(
                    modifier = Modifier
                        .size(50.dp),
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "refresh icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    } else Loading()
}