package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * QRTransaction Model - Represents a transaction initiated via QR code scanning
 * File: QRTransaction.kt
 * Purpose: Store QR-code-based payment transactions with special metadata
 */
@Entity(tableName = "qr_transactions")
data class QRTransaction(
    @PrimaryKey
    val qrTransactionId: String = UUID.randomUUID().toString(),
    
    // Link to Main Transaction
    val transactionId: String,
    
    // QR Code Information
    val qrCodeData: String,  // Raw QR code content
    val qrCodeFormat: String = "UPI",  // "UPI", "CUSTOM", "DYNAMIC"
    val qrCodeType: String = "STATIC",  // "STATIC", "DYNAMIC", "TIME_LIMITED"
    
    // Merchant Information (for merchant QR codes)
    val merchantUPI: String? = null,
    val merchantName: String? = null,
    val merchantId: String? = null,
    val merchantCategory: String? = null,
    val merchantLocation: String? = null,
    
    // Transaction Details
    val amount: Double? = null,  // Null for open amount QRs
    val description: String? = null,
    val reference: String? = null,
    
    // QR Code Validity
    val isExpired: Boolean = false,
    val expiryTime: Long? = null,
    val scanCount: Int = 0,
    val maxScans: Int? = null,  // Null for unlimited
    
    // Verification
    val isMerchantVerified: Boolean = false,
    val verificationMethod: String? = null,
    val verificationDate: Long? = null,
    
    // Additional Metadata
    val originalQRPath: String? = null,
    val capturedImage: String? = null,  // Base64 or image URL
    val scannedAt: Long = System.currentTimeMillis(),
    
    // Timestamps
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * PaymentNotification Model - Represents notifications for payment events
 * File: PaymentNotification.kt
 * Purpose: Store payment and approval notifications for users
 */
@Entity(tableName = "payment_notifications")
data class PaymentNotification(
    @PrimaryKey
    val notificationId: String = UUID.randomUUID().toString(),
    
    // User Reference
    val userId: String,
    
    // Notification Type
    val type: String,  // "TRANSACTION_SENT", "TRANSACTION_RECEIVED", "APPROVAL_REQUEST", "APPROVAL_DECISION"
    val title: String,
    val message: String,
    
    // Related Entity
    val relatedId: String? = null,  // Transaction ID or Approval Request ID
    val relatedType: String? = null,  // "TRANSACTION", "APPROVAL_REQUEST", "MEMBER"
    
    // Status
    val isRead: Boolean = false,
    val readAt: Long? = null,
    val isArchived: Boolean = false,
    val archivedAt: Long? = null,
    
    // Priority & Urgency
    val priority: String = "NORMAL",  // "LOW", "NORMAL", "HIGH", "URGENT"
    val isUrgent: Boolean = false,
    
    // Action
    val actionRequired: Boolean = false,
    val actionType: String? = null,  // "APPROVE", "REJECT", "REVIEW"
    val actionData: String? = null,
    
    // Delivery Status
    val deliveryStatus: String = "DELIVERED",  // "PENDING", "DELIVERED", "FAILED"
    val deliveryMethod: String = "PUSH",  // "PUSH", "EMAIL", "SMS", "BOTH"
    val deliveryAttempts: Int = 0,
    
    // Timestamps
    val sentAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Additional Info
    val metadata: String? = null  // JSON string for additional data
)

/**
 * FraudAlert Model - Represents fraud detection alerts for transactions
 * File: FraudAlert.kt
 * Purpose: Store fraud detection alerts and suspicious activity logs
 */
@Entity(tableName = "fraud_alerts")
data class FraudAlert(
    @PrimaryKey
    val alertId: String = UUID.randomUUID().toString(),
    
    // User & Transaction Reference
    val userId: String,
    val transactionId: String? = null,
    
    // Alert Details
    val alertType: String,  // "UNUSUAL_AMOUNT", "UNUSUAL_TIME", "UNUSUAL_LOCATION", "UNUSUAL_RECIPIENT"
    val severity: String,  // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val fraudScore: Float,  // 0-100
    val reason: String,
    val description: String,
    
    // Detection Method
    val detectionMethod: String,  // "AMOUNT_THRESHOLD", "TIME_PATTERN", "LOCATION_ANOMALY", "BEHAVIOR_ANALYSIS", "ML_MODEL"
    
    // Transaction Context
    val transactionAmount: Double? = null,
    val averageTransactionAmount: Double? = null,
    val location: String? = null,
    val standardLocation: String? = null,
    val timestamp: Long? = null,
    val standardTime: String? = null,
    
    // Status
    val status: String,  // "PENDING", "REVIEWED", "APPROVED", "BLOCKED", "ESCALATED"
    val reviewedAt: Long? = null,
    val reviewedBy: String? = null,
    
    // Action Taken
    val actionTaken: String? = null,  // "BLOCK_TRANSACTION", "REQUEST_OTP", "REQUEST_BIOMETRIC", "MANUAL_REVIEW"
    val actionTakenAt: Long? = null,
    
    // Additional Info
    val ipAddress: String? = null,
    val deviceId: String? = null,
    val deviceModel: String? = null,
    val userAgent: String? = null,
    
    // Timestamps
    val detectedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Resolution
    val resolved: Boolean = false,
    val resolvedAt: Long? = null,
    val resolutionNotes: String? = null
)

/**
 * DeviceInfo Model - Represents device information for verification
 * File: DeviceInfo.kt
 * Purpose: Store device details for device verification and fraud detection
 */
@Entity(tableName = "device_info")
data class DeviceInfo(
    @PrimaryKey
    val deviceId: String,
    
    // User Reference
    val userId: String? = null,
    
    // Device Details
    val deviceName: String,
    val deviceModel: String,
    val deviceBrand: String,
    val osVersion: String,
    val androidVersion: Int,
    
    // Device Identifiers
    val imei: String? = null,
    val deviceSerial: String? = null,
    val androidId: String? = null,
    val macAddress: String? = null,
    
    // Device Fingerprint
    val deviceFingerprint: String,
    val fingerprintHash: String,
    
    // Security
    val isRooted: Boolean = false,
    val isEmulator: Boolean = false,
    val securityPatchLevel: String? = null,
    val bootloaderUnlocked: Boolean = false,
    
    // Verification Status
    val isVerified: Boolean = false,
    val verificationDate: Long? = null,
    val verificationMethod: String? = null,  // "OTP", "BIOMETRIC", "DEVICE_INTEGRITY"
    
    // Biometric
    val hasBiometric: Boolean = false,
    val biometricType: String? = null,  // "FINGERPRINT", "FACE", "IRIS"
    val biometricRegistered: Boolean = false,
    
    // Network
    val defaultNetworkType: String? = null,  // "WIFI", "MOBILE"
    val vpnDetected: Boolean = false,
    val proxyDetected: Boolean = false,
    
    // App Installation
    val appInstallTime: Long = 0L,
    val appUpdateTime: Long = 0L,
    
    // Trust Score
    val trustScore: Float = 0f,  // 0-100
    val lastTrustedAt: Long? = null,
    
    // Timestamps
    val registeredAt: Long = System.currentTimeMillis(),
    val lastSeenAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    
    // Additional Info
    val notes: String? = null
)
