package com.white.fox.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.white.fox.session.SessionManager

class NavViewModelFactory(
    private val getter: () -> SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NavViewModel::class.java)) {
            val sessionManager = getter()
            @Suppress("UNCHECKED_CAST")
            return NavViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
