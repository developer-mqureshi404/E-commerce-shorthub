package com.example.tutedude.ecommerce.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.Transaction
import com.example.tutedude.ecommerce.data.repository.ITransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * TransactionViewModel - Manages transaction state and history
 * File: TransactionViewModel.kt
 * Purpose: Handle transaction history display and operations
 */
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: ITransactionRepository
) : ViewModel() {
    
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()
    
    private val _sentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val sentTransactions: StateFlow<List<Transaction>> = _sentTransactions.asStateFlow()
    
    private val _receivedTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val receivedTransactions: StateFlow<List<Transaction>> = _receivedTransactions.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _selectedTransaction = MutableStateFlow<Transaction?>(null)
    val selectedTransaction: StateFlow<Transaction?> = _selectedTransaction.asStateFlow()
    
    private val _dailyTransactionCount = MutableStateFlow(0)
    val dailyTransactionCount: StateFlow<Int> = _dailyTransactionCount.asStateFlow()
    
    private val _dailyTransactionSum = MutableStateFlow(0.0)
    val dailyTransactionSum: StateFlow<Double> = _dailyTransactionSum.asStateFlow()
    
    fun loadUserTransactions(userId: String) {
        viewModelScope.launch {
            try {
                transactionRepository.getUserTransactionsFlow(userId).collect { txns ->
                    _transactions.value = txns
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun loadSentTransactions(userId: String) {
        viewModelScope.launch {
            try {
                transactionRepository.getSentTransactionsFlow(userId).collect { txns ->
                    _sentTransactions.value = txns
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun loadReceivedTransactions(userId: String) {
        viewModelScope.launch {
            try {
                transactionRepository.getReceivedTransactionsFlow(userId).collect { txns ->
                    _receivedTransactions.value = txns
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun selectTransaction(transaction: Transaction) {
        _selectedTransaction.value = transaction
    }
    
    fun clearSelection() {
        _selectedTransaction.value = null
    }
    
    fun loadDailyStats(userId: String) {
        viewModelScope.launch {
            try {
                _dailyTransactionCount.value = transactionRepository.getDailyTransactionCount(userId)
                _dailyTransactionSum.value = transactionRepository.getDailyTransactionSum(userId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
