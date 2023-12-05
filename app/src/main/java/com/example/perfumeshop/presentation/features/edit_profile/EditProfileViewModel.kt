package com.example.perfumeshop.presentation.features.edit_profile

import androidx.lifecycle.ViewModel
import com.example.perfumeshop.data.user.UserData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(val auth: FirebaseAuth, val userData: UserData) : ViewModel()