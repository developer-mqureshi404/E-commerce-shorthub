package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.local.WalletDao
import com.example.tutedude.ecommerce.data.model.Wallet
import com.example.tutedude.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * WalletRepository - Manages wallet operations
 * File: WalletRepository.kt
 * Purpose: Handle wallet-related database and API operations
 */
interface IWalletRepository {
    suspend fun createWallet(wallet: Wallet): Resource<Unit>
    suspend fun getWallet(userId: String): Resource<Wallet>
    fun getWalletFlow(userId: String): Flow<Wallet?>
    suspend fun updateWallet(wallet: Wallet): Resource<Unit>
    suspend fun updateBalance(userId: String, balance: Double): Resource<Unit>
    suspend fun addFunds(userId: String, amount: Double): Resource<Unit>
    suspend fun deductFunds(userId: String, amount: Double): Resource<Unit>
}

class WalletRepository @Inject constructor(
    private val walletDao: WalletDao
) : IWalletRepository {
    
    override suspend fun createWallet(wallet: Wallet): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            walletDao.insertWallet(wallet)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to create wallet"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getWallet(userId: String): Resource<Wallet> = flow {
        try {
            emit(Resource.Loading())
            val wallet = walletDao.getWalletByUserId(userId)
            if (wallet != null) {
                emit(Resource.Success(wallet))
            } else {
                emit(Resource.Error("Wallet not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch wallet"))
        }
    }.apply { collect { } as Unit }
    
    override fun getWalletFlow(userId: String): Flow<Wallet?> {
        return walletDao.getWalletFlowByUserId(userId)
    }
    
    override suspend fun updateWallet(wallet: Wallet): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            walletDao.updateWallet(wallet)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update wallet"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun updateBalance(userId: String, balance: Double): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            walletDao.updateBalance(userId, balance)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update balance"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun addFunds(userId: String, amount: Double): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val wallet = walletDao.getWalletByUserId(userId)
            if (wallet != null) {
                val newBalance = wallet.totalBalance + amount
                walletDao.updateBalance(userId, newBalance)
                walletDao.updateAvailableBalance(userId, wallet.availableBalance + amount)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Wallet not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to add funds"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun deductFunds(userId: String, amount: Double): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val wallet = walletDao.getWalletByUserId(userId)
            if (wallet != null && wallet.availableBalance >= amount) {
                val newBalance = wallet.totalBalance - amount
                walletDao.updateBalance(userId, newBalance)
                walletDao.updateAvailableBalance(userId, wallet.availableBalance - amount)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Insufficient balance"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to deduct funds"))
        }
    }.apply { collect { } as Unit }
}
