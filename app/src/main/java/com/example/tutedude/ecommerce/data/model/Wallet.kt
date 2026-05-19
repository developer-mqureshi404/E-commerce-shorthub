package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

/**
 * Wallet Model - Represents a user's wallet account
 * File: Wallet.kt
 * Purpose: Store wallet balance, UPI ID, and account details for payment operations
 */
@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey
    val userId: String,
    
    // Balance Information
    val totalBalance: Double = 0.0,
    val availableBalance: Double = 0.0,
    val onHoldBalance: Double = 0.0,  // For pending approvals
    
    // UPI & Account Details
    val upiId: String = "",  // user@bank
    val accountNumber: String? = null,
    val ifscCode: String? = null,
    val bankName: String? = null,
    val accountHolderName: String? = null,
    
    // Phone & Email
    val phoneNumber: String = "",
    val email: String = "",
    
    // Transaction Limits
    val dailyTransactionLimit: Double = 100000.0,
    val monthlyTransactionLimit: Double = 1000000.0,
    val singleTransactionLimit: Double = 50000.0,
    
    // Approval System
    val requiresApprovalForTransactions: Boolean = true,
    val approvedMembersCount: Int = 0,
    val maxApprovedMembers: Int = 2,
    
    // Security
    val isPinSet: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isKycVerified: Boolean = false,
    val deviceFingerprint: String? = null,
    
    // Account Status
    val isActive: Boolean = true,
    val isFrozen: Boolean = false,
    val lastLogin: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Additional Info
    val currency: String = "INR",
    val country: String = "IN",
    val profileImageUrl: String? = null
)
