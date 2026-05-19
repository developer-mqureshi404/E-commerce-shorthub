package com.example.tutedude.ecommerce.presentation.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.Wallet
import com.example.tutedude.ecommerce.data.repository.IWalletRepository
import com.example.tutedude.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * WalletViewModel - Manages wallet state and operations
 * File: WalletViewModel.kt
 * Purpose: Handle wallet balance, transactions, and UI state
 */
@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: IWalletRepository
) : ViewModel() {
    
    private val _wallet = MutableStateFlow<Wallet?>(null)
    val wallet: StateFlow<Wallet?> = _wallet.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()
    
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    fun initWallet(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Get wallet from repository
                val walletResource = walletRepository.getWallet(userId)
                // Handle resource based on actual implementation
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
    
    fun updateBalance(userId: String, newBalance: Double) {
        viewModelScope.launch {
            _loading.value = true
            try {
                walletRepository.updateBalance(userId, newBalance)
                _balance.value = newBalance
                _successMessage.value = "Balance updated successfully"
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
    
    fun addFunds(userId: String, amount: Double) {
        viewModelScope.launch {
            _loading.value = true
            try {
                walletRepository.addFunds(userId, amount)
                _successMessage.value = "Funds added successfully"
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
    
    fun deductFunds(userId: String, amount: Double) {
        viewModelScope.launch {
            _loading.value = true
            try {
                walletRepository.deductFunds(userId, amount)
                _successMessage.value = "Funds deducted successfully"
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearSuccess() {
        _successMessage.value = null
    }
}
