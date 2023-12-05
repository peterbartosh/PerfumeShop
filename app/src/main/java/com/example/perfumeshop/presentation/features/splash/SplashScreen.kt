package com.example.perfumeshop.presentation.features.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.perfumeshop.presentation.features.splash.components.AnimatedLogo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateAsk: () -> Unit,
    navigateHome: () -> Unit,
    splashViewModel: SplashViewModel
) {

    val scale = remember {
        Animatable(0f)
    }

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

        splashViewModel.start(navigateHome, navigateAsk)
    }

    AnimatedLogo(scale.value)

}