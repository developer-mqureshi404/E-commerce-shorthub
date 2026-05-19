package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * ApprovalRequest Model - Represents a pending approval request for transactions
 * File: ApprovalRequest.kt
 * Purpose: Track approval requests sent to family members for high-value transactions
 */
@Entity(tableName = "approval_requests")
data class ApprovalRequest(
    @PrimaryKey
    val requestId: String = UUID.randomUUID().toString(),
    
    // Transaction & User References
    val transactionId: String,
    val userId: String,  // Account owner requesting approval
    
    // Approval Details
    val approverMemberId: String,  // Family member who can approve
    val approverName: String,
    val approverPhone: String,
    
    // Request Information
    val amount: Double,
    val currency: String = "INR",
    val recipientUPI: String,
    val recipientName: String,
    val description: String? = null,
    
    // Status Tracking
    val status: String,  // "PENDING", "APPROVED", "REJECTED", "EXPIRED", "CANCELLED"
    val approvalResponse: String? = null,  // Comments from approver
    
    // Approval Details
    val approvalCode: String? = null,  // Unique code for approval
    val approvedAt: Long? = null,
    val rejectedAt: Long? = null,
    val expiresAt: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000),  // Expires in 24 hours
    
    // PIN Verification
    val pinRequired: Boolean = true,
    val pinVerified: Boolean = false,
    val pinVerificationAttempts: Int = 0,
    val maxPinAttempts: Int = 3,
    val pinLockedUntil: Long? = null,
    
    // Biometric Verification
    val biometricRequired: Boolean = false,
    val biometricVerified: Boolean = false,
    
    // OTP Verification
    val otpRequired: Boolean = true,
    val otpSent: Boolean = false,
    val otpVerified: Boolean = false,
    val otpAttempts: Int = 0,
    val maxOtpAttempts: Int = 3,
    val otpSentAt: Long? = null,
    val otpExpiresAt: Long? = null,
    
    // Notification
    val notificationSent: Boolean = false,
    val notificationSentAt: Long? = null,
    val notificationRead: Boolean = false,
    val notificationReadAt: Long? = null,
    
    // Security & Verification
    val deviceVerified: Boolean = true,
    val locationVerified: Boolean = false,
    val ipAddress: String? = null,
    val deviceId: String? = null,
    
    // Retry Information
    val retryCount: Int = 0,
    val lastRetryAt: Long? = null,
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Additional Info
    val notes: String? = null,
    val tags: List<String> = emptyList(),
    val priority: String = "NORMAL"  // "LOW", "NORMAL", "HIGH"
)
