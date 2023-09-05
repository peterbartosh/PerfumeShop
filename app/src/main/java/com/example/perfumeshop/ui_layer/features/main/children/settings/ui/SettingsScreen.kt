package com.example.perfumeshop.ui_layer.features.main.children.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.ui.theme.DarkPurple
import com.example.perfumeshop.ui.theme.Gold


@Composable
fun SettingsScreen(onThemeChange : (Boolean) -> Unit) {


    Surface(modifier = Modifier.fillMaxSize()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(100.dp))

            Row(horizontalArrangement = Arrangement.Center) {
                ThemeButton(onClick = { onThemeChange(true) }, color = DarkPurple,
                            painter = painterResource(id = R.drawable.night))

                Spacer(modifier = Modifier.width(30.dp))

                ThemeButton(onClick = { onThemeChange(false) }, color = Gold,
                            painter = painterResource(id = R.drawable.sun))
            }

            //SettingsSections(isDarkTheme = isDarkTheme)
        }

    }

}

@Composable
fun ThemeButton(onClick : () -> Unit, color : Color, painter : Painter) {
    Button(
        onClick = onClick,
           enabled = true,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.size(100.dp)
    ) {
        Icon(painter = painter, contentDescription = null,
             modifier = Modifier.size(70.dp), tint = Color.White)
    }
}



@Composable
fun SettingsSections(isDarkTheme : Boolean) {





//    OptionsSection(
//        sectionTitle = "Общие настройки", listOf(
//            Option("Тема", OptionType.Theme, null, Icons.Outlined.ArrowForward),
//            Option("О приложении", OptionType.About, null, Icons.Outlined.ArrowForward)
//        ), onOptionClick = onOptionClick
//    )
//
//    OptionsSection(
//        sectionTitle = "Контакты", listOf(
//            Option(
//                "Телефон",
//                OptionType.PhoneNumber,
//                Icons.Default.Phone,
//                "+111111111111_link"
//            ),
//            Option(
//                "E-mail",
//                OptionType.Gmail,
//                Icons.Default.Email,
//                "goldpardum@gmail.com_link"
//            ),
//            Option(
//                "Telegram",
//                OptionType.Telegram,
//                painterResource(id = R.drawable.telegram_icon),
//                "telegram_link"
//            ),
//            Option(
//                "WhatsApp",
//                OptionType.WhatsApp,
//                painterResource(id = R.drawable.whatsapp_icon),
//                "whatsapp_link"
//            )
//        ), onOptionClick = onThemeClick
//    )
}
