package com.example.perfumeshop.ui_layer.features.start.splash_feature.ui


import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.ui_layer.theme.Gold
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navigateAsk : () -> Unit, navigateMain : () -> Unit) {

    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true ){
        scale.animateTo(targetValue = 0.9f,
                        animationSpec = tween(durationMillis = 800,
                                              easing = {
                                                  OvershootInterpolator(8f)
                                                      .getInterpolation(it)
                                              }))
        delay(2000L)



        val currentUser = FirebaseAuth.getInstance().currentUser

        Log.d("CUR_USER", "SplashScreen: ${currentUser == null}")

        //if (currentUser?.isAnonymous == true || currentUser?.phoneNumber.isNullOrEmpty()){
        if (currentUser?.isAnonymous == true || !currentUser?.phoneNumber.isNullOrEmpty()){
            navigateMain.invoke()
            //navController.navigate(Screens.LoginScreen.name)
        }else {
            navigateAsk.invoke()
            //navController.navigate(Screens.HomeScreen.name)
        }
        //navigateAsk.invoke()


        //navigateFront.invoke()



    }

    Surface(modifier = Modifier
        .padding(15.dp)
        .size(330.dp)
        .scale(scale.value),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(width = 2.dp,
                                  color = Color.LightGray)) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MainLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "\" Парфюмерия и косметика оптом \"",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )


        }
    }


}

@Composable
fun MainLogo(modifier: Modifier = Modifier) {
    Text(text = "Gold Parfum",
         modifier = modifier.padding(bottom = 16.dp),
         style = MaterialTheme.typography.bodyMedium,
         color = Gold
    )
}

