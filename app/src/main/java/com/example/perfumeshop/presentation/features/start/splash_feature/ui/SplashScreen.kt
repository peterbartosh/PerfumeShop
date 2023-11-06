package com.example.perfumeshop.presentation.features.start.splash_feature.ui


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.user.UserData
import com.example.perfumeshop.data.utils.getWidthPercent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navigateAsk: () -> Unit,
    navigateHome: () -> Unit
) {

    val context = LocalContext.current

    val scale = remember {
        Animatable(0f)
    }

//    var isBlockedState by remember {
//        val isBlocked : MutableState<Boolean?> = mutableStateOf(null)
//        isBlocked
//    }

    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                }
            )
        )
        delay(300L)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser?.isAnonymous == true || !currentUser?.email.isNullOrEmpty()) {
            navigateHome()
            if (!currentUser?.email.isNullOrEmpty())
                UserData.loadUserData()
        } else
            navigateAsk()


    }

    val wp = getWidthPercent(context = context)

    Column(
        modifier = Modifier
            .fillMaxSize(),
            //.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Surface(
            modifier = Modifier
                .size(wp * 80)
                .scale(scale.value),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Column(
                modifier = Modifier.padding(1.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Gold Parfum",
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Парфюмерия и косметика оптом",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }


}