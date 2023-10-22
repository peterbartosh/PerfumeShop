package com.example.perfumeshop.data.user_preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Singleton

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("DarkThemeOn", Context.MODE_PRIVATE)

    fun saveThemeData(value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("themeState", value)
        editor.apply()
    }

    fun saveFontSizeData(value : Int){
        val editor = sharedPreferences.edit()
        editor.putInt("fontSizeState", value)
        editor.apply()
    }

    fun getThemeData(defaultValue: Int): Int {
        return sharedPreferences.getInt("themeState", defaultValue)
    }

    fun getFontSizeData(defaultValue: Int): Int {
        return sharedPreferences.getInt("fontSizeState", defaultValue)
    }
}