package com.example.perfumeshop.presentation.features.app_blocked

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.user.UserData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppBlockedViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userData: UserData
) : ViewModel() {

    fun refresh(
        navigateToHome: () -> Unit,
        navigateToAsk: () -> Unit
    ) = viewModelScope.launch {
        FireRepository.isAppBlocked()?.let { isBlocked ->
            if (!isBlocked) {

                val currentUser = auth.currentUser

                if (currentUser?.isAnonymous == true || !currentUser?.email.isNullOrEmpty()) {
                    if (!currentUser?.email.isNullOrEmpty())
                        userData.loadUserDataFromDatabase().join()
                    navigateToHome()
                } else
                    navigateToAsk()
            }
        }
    }

}