package com.example.perfumeshop.presentation.features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.user_preferences.PreferencesManager
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.presentation.features.settings.components.FontSizePicker
import com.example.perfumeshop.presentation.features.settings.components.ThemePicker


@Composable
fun SettingsScreen(onUserPreferencesChanged: (UserPreferencesType, Int) -> Unit) {

    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context)
    }

    val chosenTheme = remember {
        mutableStateOf(preferencesManager.getThemeData(defaultValue = 0))
    }

    val chosenFontSizeInd = remember {
        mutableStateOf(preferencesManager.getFontSizeData(defaultValue = 1))
    }

    LaunchedEffect(key1 = chosenTheme.value){
        preferencesManager.saveThemeData(value = chosenTheme.value)
        onUserPreferencesChanged(UserPreferencesType.Theme, chosenTheme.value)
    }

    LaunchedEffect(key1 = chosenFontSizeInd.value){
        preferencesManager.saveFontSizeData(value = chosenFontSizeInd.value)
        onUserPreferencesChanged(UserPreferencesType.FontSize, chosenFontSizeInd.value)
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Настройки",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        Divider(modifier = Modifier.padding(top = 30.dp, bottom = 10.dp))

        Text(
            text = "Выбор темы",
            style = MaterialTheme.typography.labelMedium
        )

        Divider(modifier = Modifier.padding(top = 10.dp, bottom = 15.dp))

        ThemePicker(chosenTheme)

        Divider(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))

        Text(text = "Выбор размера шрифта", style = MaterialTheme.typography.labelMedium)

        Divider(modifier = Modifier.padding(top = 10.dp, bottom = 15.dp))

        FontSizePicker(selectedIndState = chosenFontSizeInd)

        Divider(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))

    }
}
