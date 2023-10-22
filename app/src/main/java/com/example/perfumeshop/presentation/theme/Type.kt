package com.example.perfumeshop.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
@Composable
fun getTypography(fontSize : Int) : Typography {
    return Typography(
        titleLarge = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
//            when (fontSize){
//                0 -> 19.sp
//                1 -> 22.sp
//                2 -> 25.sp
//                else -> 19.sp
//            },
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 0.7.sp
        ),
        titleMedium = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = when (fontSize){
                0 -> 18.sp
                1 -> 21.sp
                2 -> 24.sp
                else -> 18.sp
            },
            lineHeight = 27.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 0.7.sp
        ),
        titleSmall = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = when (fontSize){
                0 -> 17.sp
                1 -> 20.sp
                2 -> 23.sp
                else -> 17.sp
            },
            lineHeight = 27.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 0.7.sp
        ),
        labelLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = when (fontSize){
                0 -> 16.sp
                1 -> 19.sp
                2 -> 22.sp
                else -> 16.sp
            },
            lineHeight = 27.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 0.7.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = when (fontSize){
                0 -> 15.sp
                1 -> 18.sp
                2 -> 21.sp
                else -> 15.sp
            },
            lineHeight = 27.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 0.7.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = when (fontSize){
                0 -> 14.sp
                1 -> 17.sp
                2 -> 20.sp
                else -> 14.sp
            },
            textAlign = TextAlign.Center,
            lineHeight = 27.sp,
            letterSpacing = 0.7.sp
        ),
        bodyLarge = TextStyle(
            fontStyle = FontStyle.Italic,
            //fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            fontSize = when (fontSize){
                0 -> 13.sp
                    1 -> 16.sp
                    2 -> 19.sp
                else -> 13.sp
            },
            //textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = when (fontSize){
                0 -> 12.sp
                1 -> 15.sp
                2 -> 18.sp
                else -> 12.sp
            },
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            //color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground
        ),
        bodySmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = when (fontSize){
                0 -> 12.sp
                1 -> 13.sp
                2 -> 14.sp
                else -> 11.sp
            },
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            //color = MaterialTheme.colorScheme.background
        )
    )
}

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)