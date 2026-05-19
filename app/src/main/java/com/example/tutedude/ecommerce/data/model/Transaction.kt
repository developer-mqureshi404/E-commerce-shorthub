package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Transaction Model - Represents a money transfer/transaction
 * File: Transaction.kt
 * Purpose: Store transaction history with sender, receiver, amount, and status details
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey
    val transactionId: String = UUID.randomUUID().toString(),
    
    // Transaction Type
    val type: String,  // "SENT", "RECEIVED", "WITHDRAWAL", "DEPOSIT"
    val status: String,  // "PENDING", "COMPLETED", "FAILED", "CANCELLED", "AWAITING_APPROVAL"
    
    // Parties Involved
    val senderId: String,
    val senderUPI: String,
    val senderName: String,
    
    val recipientId: String?,  // null for external transfers
    val recipientUPI: String,
    val recipientName: String,
    val recipientPhone: String? = null,
    
    // Amount Details
    val amount: Double,
    val currency: String = "INR",
    val transactionFee: Double = 0.0,
    val totalAmount: Double = amount + transactionFee,
    
    // Description
    val description: String? = null,
    val reference: String? = null,  // For QR code transfers
    
    // Approval System
    val requiresApproval: Boolean = false,
    val approvalsNeeded: Int = 0,
    val approvalsReceived: Int = 0,
    val approvalRequestIds: List<String> = emptyList(),
    val approvalStatus: String = "NONE",  // "NONE", "PENDING", "APPROVED", "REJECTED"
    
    // Security & Verification
    val isQRTransaction: Boolean = false,
    val pinVerified: Boolean = false,
    val biometricVerified: Boolean = false,
    val deviceVerified: Boolean = true,
    val fraudScore: Float = 0f,  // 0-100, higher = more suspicious
    val fraudStatus: String = "NONE",  // "NONE", "LOW", "MEDIUM", "HIGH", "BLOCKED"
    
    // Timestamps
    val initiatedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Additional Info
    val paymentMethod: String = "UPI",  // "UPI", "CARD", "BANK_TRANSFER"
    val notes: String? = null,
    val tags: List<String> = emptyList(),
    
    // Metadata
    val ipAddress: String? = null,
    val deviceId: String? = null,
    val location: String? = null
)
