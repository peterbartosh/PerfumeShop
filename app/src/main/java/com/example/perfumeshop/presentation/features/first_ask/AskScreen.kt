package com.example.perfumeshop.presentation.features.first_ask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AskLoginScreen(onLoginClick: () -> Unit, onSkipClick: () -> Unit) {

    Column(horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center,
           modifier = Modifier.fillMaxSize()) {

        //Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = R.drawable.registration_image),
            contentDescription = "reg img",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(top = 30.dp)
                //.height(200.dp)
                //.width(300.dp)
        )
        Text(
            text = stringResource(id = R.string.registration_label),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = stringResource(R.string.registration_body),
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(modifier = Modifier.wrapContentHeight()) {

            Button(onClick = onLoginClick,
                   modifier = Modifier
                       .wrapContentHeight()
                       .wrapContentWidth(),
                   contentPadding = PaddingValues(all = 10.dp),
                   shape = RoundedCornerShape(10.dp),
                   colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.Black)
            ) {
                Text(text = "Регистрация", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = {
                        FirebaseAuth.getInstance().signInAnonymously()
                        onSkipClick()
                   },
                   modifier = Modifier
                       .wrapContentHeight()
                       .wrapContentWidth(),
                   border = BorderStroke(1.dp, Color.LightGray),
                   contentPadding = PaddingValues(all = 10.dp),
                   shape = RoundedCornerShape(10.dp),
                   colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.LightGray)
            ) {
                Text(text = "Пропустить", fontSize = 13.sp)
            }
        }
    }

}