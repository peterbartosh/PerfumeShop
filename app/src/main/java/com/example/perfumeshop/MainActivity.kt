package com.example.perfumeshop

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.perfumeshop.data_layer.utils.createProducts
import com.example.perfumeshop.ui_layer.app.App
import com.example.perfumeshop.ui_layer.app.saveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            saveData = false
            //createProducts()
            App()
        }
    }

    override fun onDestroy() {
        saveData = true
        super.onDestroy()
    }


}

