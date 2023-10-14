package com.example.perfumeshop.presentation.theme

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    secondary = Gold,
    tertiary = Gold
)

private val LightColorScheme = lightColorScheme(
    primary = Gold,
    secondary = Gold,
    tertiary = Gold

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)



@Composable
fun PerfumeShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Gold.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("DarkThemeOn", Context.MODE_PRIVATE)

    fun saveData(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("themeState", value)
        editor.apply()
    }

    fun getData(defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean("themeState", defaultValue) ?: defaultValue
    }
}

//
//
//interface UserSettings {
//    val isDarkStream: StateFlow<Boolean>
//    var isDark: Boolean
//}
//
//class UserSettingsImpl ( context: Context) : UserSettings {
//
//    override val isDarkStream: MutableStateFlow<Boolean>
//    override var isDark: Boolean by AppThemePreferenceDelegate("app_theme", false)
//
//    private val preferences: SharedPreferences =
//        context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)
//
//    init {
//        isDarkStream = MutableStateFlow(isDark)
//    }
//
//    inner class AppThemePreferenceDelegate(
//        private val name: String,
//        private val default: Boolean,
//    ) : ReadWriteProperty<Any?, Boolean> {
//
//        override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
//            preferences.getBoolean(name, default)
//
//        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
//            isDarkStream.value = value
//            preferences.edit {
//                putBoolean(name, value)
//            }
//        }
//    }
//}