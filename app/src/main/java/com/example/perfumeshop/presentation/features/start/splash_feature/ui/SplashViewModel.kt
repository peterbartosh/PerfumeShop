package com.example.perfumeshop.presentation.features.start.splash_feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.user.UserData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userData: UserData,
    private val auth: FirebaseAuth
) : ViewModel() {

    fun start(navigateHome: () -> Unit, navigateAsk: () -> Unit) = viewModelScope.launch {

        val currentUser = auth.currentUser
        if (currentUser?.isAnonymous == true || !currentUser?.email.isNullOrEmpty()) {
            if (!currentUser?.email.isNullOrEmpty())
                userData.loadUserDataFromDatabase().join()
            navigateHome()
        } else
            navigateAsk()
    }

}