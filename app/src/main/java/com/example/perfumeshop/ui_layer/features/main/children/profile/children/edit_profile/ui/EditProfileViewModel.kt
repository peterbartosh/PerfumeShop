package com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.ui

import androidx.lifecycle.ViewModel
import com.example.perfumeshop.data_layer.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val fireRepository: FireRepository) : ViewModel() {

}