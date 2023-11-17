package com.example.perfumeshop.presentation.features.main.home_feature.home.ui

import androidx.lifecycle.ViewModel
import com.example.perfumeshop.data.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) : ViewModel()
