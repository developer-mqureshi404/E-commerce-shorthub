package com.example.tutedude.ecommerce.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BiometricAuthManager - Manages fingerprint and face recognition authentication
 * File: BiometricAuthManager.kt
 * Purpose: Handle biometric authentication for secure transactions
 */
@Singleton
class BiometricAuthManager @Inject constructor(
    private val context: Context
) {
    private val biometricManager = BiometricManager.from(context)
    
    private val _authenticationState = MutableStateFlow<AuthenticationState>(AuthenticationState.Idle)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState.asStateFlow()
    
    private val _isBiometricAvailable = MutableStateFlow(checkBiometricAvailability())
    val isBiometricAvailable: StateFlow<Boolean> = _isBiometricAvailable.asStateFlow()
    
    fun checkBiometricAvailability(): Boolean {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }
    
    /**
     * Authenticate user with biometric (fingerprint or face)
     */
    fun authenticateWithBiometric(
        activity: FragmentActivity,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        if (!_isBiometricAvailable.value) {
            onError("Biometric authentication not available")
            return
        }
        
        _authenticationState.value = AuthenticationState.Authenticating
        
        val executor = androidx.core.content.ContextCompat.getMainExecutor(context)
        
        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    _authenticationState.value = AuthenticationState.Error(errString.toString())
                    onError(errString.toString())
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _authenticationState.value = AuthenticationState.Success
                    onSuccess(result)
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    _authenticationState.value = AuthenticationState.Failed
                    onFailed()
                }
            }
        )
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    fun resetAuthenticationState() {
        _authenticationState.value = AuthenticationState.Idle
    }
}

sealed class AuthenticationState {
    data object Idle : AuthenticationState()
    data object Authenticating : AuthenticationState()
    data object Success : AuthenticationState()
    data object Failed : AuthenticationState()
    data class Error(val message: String) : AuthenticationState()
}
