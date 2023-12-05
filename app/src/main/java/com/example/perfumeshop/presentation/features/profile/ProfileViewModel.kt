package com.example.perfumeshop.presentation.features.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.skeleton.DataManager
import com.example.perfumeshop.data.skeleton.FavouriteFunctionality
import com.example.perfumeshop.data.user.UserData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val userData: UserData,
    val cartFunctionality: CartFunctionality,
    val favouriteFunctionality: FavouriteFunctionality,
    val dataManager: DataManager
): ViewModel(){
    override fun onCleared() {
        Log.d("VM_LFC_TAG", "profile vm cleared")
        super.onCleared()
    }
    init {
        Log.d("VM_LFC_TAG", "profile vm started")
    }
}