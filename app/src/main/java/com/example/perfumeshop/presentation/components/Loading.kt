package com.example.perfumeshop.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.theme.FingersShape1
import com.example.perfumeshop.presentation.theme.FingersShape2

@Composable
fun ChildScreenAnimation(content: @Composable () -> Unit) {
    AnimatedVisibility(
        visibleState = MutableTransitionState(
            initialState = false
        ).apply { targetState = true },
        modifier = Modifier,
        enter = slideInVertically(initialOffsetY = { -40 }) +
                expandVertically(expandFrom = Alignment.Top) +
                fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
    ) {
        content()
    }
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieLoadingAnimation()
    }
}

@Composable
private fun LottieLoadingAnimation() {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading)
    )

    val dynamicProperties = rememberLottieDynamicProperties(

        // loading text

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "L",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "O",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "A",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "D",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "I",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "N",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.onBackground.toArgb(),
            keyPath = arrayOf(
                "LOADING Outlines",
                "G",
                "Fill 1"
            )
        ),

        // hand/thump

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Hand/Thump",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Hand/Thump",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Hand/Thump",
                "Shape 3",
                "Fill 1"
            )
        ),

        // shadow

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = MaterialTheme.colorScheme.secondaryContainer.toArgb(),
            keyPath = arrayOf(
                "shadow",
                "Shape 1",
                "Fill 1"
            )
        ),

        // fingers

        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Index_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Index_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Middle_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Middle_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "Ring_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "Ring_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape1.toArgb(),
            keyPath = arrayOf(
                "little_finger",
                "Shape 1",
                "Fill 1"
            )
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR,
            value = FingersShape2.toArgb(),
            keyPath = arrayOf(
                "little_finger",
                "Shape 2",
                "Fill 1"
            )
        ),
        //
    )

    LottieAnimation(
        modifier = Modifier
            .size(200.dp),
        iterations = Int.MAX_VALUE,
        composition = composition,
        dynamicProperties = dynamicProperties
    )

}


