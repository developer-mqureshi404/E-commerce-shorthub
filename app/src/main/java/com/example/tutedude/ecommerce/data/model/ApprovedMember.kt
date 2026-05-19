package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * ApprovedMember Model - Represents a family member/parent with approval authority
 * File: ApprovedMember.kt
 * Purpose: Store family members who can approve high-value transactions (2-member system)
 */
@Entity(tableName = "approved_members")
data class ApprovedMember(
    @PrimaryKey
    val memberId: String = UUID.randomUUID().toString(),
    
    // Relationship Info
    val userId: String,  // Primary account owner
    val memberPosition: Int,  // 1 or 2 (for 2-member approval system)
    
    // Member Details
    val memberName: String,
    val memberPhone: String,
    val memberEmail: String,
    val relationship: String,  // "PARENT", "GUARDIAN", "SPOUSE", "TRUSTED_MEMBER"
    
    // Profile Info
    val profileImageUrl: String? = null,
    val dateOfBirth: Long? = null,
    
    // Approval Status
    val status: String,  // "PENDING", "APPROVED", "REJECTED", "REVOKED"
    val approvalDate: Long? = null,
    val rejectionDate: Long? = null,
    val revokeDate: Long? = null,
    
    // Verification
    val isVerified: Boolean = false,
    val verificationMethod: String? = null,  // "OTP", "BIOMETRIC", "MANUAL"
    val verificationDate: Long? = null,
    
    // PIN & Security
    val pinHash: String? = null,  // Hashed PIN for approvals
    val isPinSet: Boolean = false,
    val biometricRegistered: Boolean = false,
    
    // Approval Rights
    val canApproveTransactions: Boolean = false,
    val approvalLimit: Double = 100000.0,  // Max transaction amount they can approve
    val maxDailyApprovals: Int = 10,
    val dailyApprovalsUsed: Int = 0,
    val approvalResetDate: Long = 0L,
    
    // Activity Tracking
    val totalApprovalsGiven: Int = 0,
    val totalApprovalsRejected: Int = 0,
    val lastApprovalTime: Long? = null,
    val lastActivityTime: Long? = null,
    
    // Device Management
    val deviceId: String? = null,
    val deviceName: String? = null,
    val isDeviceVerified: Boolean = false,
    val deviceVerificationDate: Long? = null,
    
    // Notification Preferences
    val notificationsEnabled: Boolean = true,
    val emailNotifications: Boolean = true,
    val smsNotifications: Boolean = true,
    val pushNotifications: Boolean = true,
    
    // Flags
    val isBlocked: Boolean = false,
    val blockReason: String? = null,
    val blockDate: Long? = null,
    
    // Timestamps
    val addedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Additional Notes
    val notes: String? = null
)
