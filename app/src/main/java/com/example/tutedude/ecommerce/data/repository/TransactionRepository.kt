package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.local.TransactionDao
import com.example.tutedude.ecommerce.data.model.Transaction
import com.example.tutedude.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * TransactionRepository - Manages transaction operations
 * File: TransactionRepository.kt
 * Purpose: Handle transaction history, sending/receiving money operations
 */
interface ITransactionRepository {
    suspend fun createTransaction(transaction: Transaction): Resource<Unit>
    suspend fun getTransaction(transactionId: String): Resource<Transaction>
    fun getUserTransactionsFlow(userId: String): Flow<List<Transaction>>
    fun getSentTransactionsFlow(userId: String): Flow<List<Transaction>>
    fun getReceivedTransactionsFlow(userId: String): Flow<List<Transaction>>
    suspend fun updateTransaction(transaction: Transaction): Resource<Unit>
    suspend fun updateTransactionStatus(transactionId: String, status: String): Resource<Unit>
    suspend fun getDailyTransactionCount(userId: String): Int
    suspend fun getDailyTransactionSum(userId: String): Double
}

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) : ITransactionRepository {
    
    override suspend fun createTransaction(transaction: Transaction): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            transactionDao.insertTransaction(transaction)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to create transaction"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getTransaction(transactionId: String): Resource<Transaction> = flow {
        try {
            emit(Resource.Loading())
            val transaction = transactionDao.getTransactionById(transactionId)
            if (transaction != null) {
                emit(Resource.Success(transaction))
            } else {
                emit(Resource.Error("Transaction not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch transaction"))
        }
    }.apply { collect { } as Unit }
    
    override fun getUserTransactionsFlow(userId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByUserFlow(userId)
    }
    
    override fun getSentTransactionsFlow(userId: String): Flow<List<Transaction>> {
        return transactionDao.getSentTransactionsFlow(userId)
    }
    
    override fun getReceivedTransactionsFlow(userId: String): Flow<List<Transaction>> {
        return transactionDao.getReceivedTransactionsFlow(userId)
    }
    
    override suspend fun updateTransaction(transaction: Transaction): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            transactionDao.updateTransaction(transaction)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update transaction"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun updateTransactionStatus(transactionId: String, status: String): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val transaction = transactionDao.getTransactionById(transactionId)
            if (transaction != null) {
                val updatedTransaction = transaction.copy(
                    status = status,
                    completedAt = if (status == "COMPLETED") System.currentTimeMillis() else null,
                    updatedAt = System.currentTimeMillis()
                )
                transactionDao.updateTransaction(updatedTransaction)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Transaction not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update transaction status"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getDailyTransactionCount(userId: String): Int {
        val today = System.currentTimeMillis() - (System.currentTimeMillis() % 86400000)
        return transactionDao.getDailyTransactionCount(userId, today)
    }
    
    override suspend fun getDailyTransactionSum(userId: String): Double {
        val today = System.currentTimeMillis() - (System.currentTimeMillis() % 86400000)
        return transactionDao.getDailyTransactionSum(userId, today)
    }
}
