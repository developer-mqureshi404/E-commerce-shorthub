package com.example.tutedude.ecommerce.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.repository.AuthRepository
import com.example.tutedude.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = authRepository.signIn(email, password)) {
                is Resource.Success -> {
                    _authState.value = AuthState.Success
                }
                is Resource.Error -> {
                    _authState.value = AuthState.Error(result.message ?: "Login failed")
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = authRepository.signUp(email, password, displayName)) {
                is Resource.Success -> {
                    _authState.value = AuthState.Success
                }
                is Resource.Error -> {
                    _authState.value = AuthState.Error(result.message ?: "Registration failed")
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}