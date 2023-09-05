package com.example.perfumeshop.ui_layer.theme_handler

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


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