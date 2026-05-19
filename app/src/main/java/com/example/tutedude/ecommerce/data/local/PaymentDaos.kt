package com.example.tutedude.ecommerce.data.local

import androidx.room.*
import com.example.tutedude.ecommerce.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * WalletDao - Database operations for Wallet entity
 * File: WalletDao.kt
 * Purpose: Provide CRUD operations for wallet data
 */
@Dao
interface WalletDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)
    
    @Query("SELECT * FROM wallets WHERE userId = :userId")
    suspend fun getWalletByUserId(userId: String): Wallet?
    
    @Query("SELECT * FROM wallets WHERE userId = :userId")
    fun getWalletFlowByUserId(userId: String): Flow<Wallet?>
    
    @Update
    suspend fun updateWallet(wallet: Wallet)
    
    @Delete
    suspend fun deleteWallet(wallet: Wallet)
    
    @Query("UPDATE wallets SET totalBalance = :balance WHERE userId = :userId")
    suspend fun updateBalance(userId: String, balance: Double)
    
    @Query("UPDATE wallets SET availableBalance = :balance WHERE userId = :userId")
    suspend fun updateAvailableBalance(userId: String, balance: Double)
    
    @Query("UPDATE wallets SET onHoldBalance = :balance WHERE userId = :userId")
    suspend fun updateOnHoldBalance(userId: String, balance: Double)
}

/**
 * TransactionDao - Database operations for Transaction entity
 * File: TransactionDao.kt
 * Purpose: Provide CRUD operations for transaction history
 */
@Dao
interface TransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)
    
    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    suspend fun getTransactionById(transactionId: String): Transaction?
    
    @Query("SELECT * FROM transactions WHERE senderId = :userId OR recipientId = :userId ORDER BY initiatedAt DESC")
    fun getTransactionsByUserFlow(userId: String): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE senderId = :userId ORDER BY initiatedAt DESC")
    fun getSentTransactionsFlow(userId: String): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE recipientId = :userId ORDER BY initiatedAt DESC")
    fun getReceivedTransactionsFlow(userId: String): Flow<List<Transaction>>
    
    @Update
    suspend fun updateTransaction(transaction: Transaction)
    
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)
    
    @Query("DELETE FROM transactions WHERE transactionId = :transactionId")
    suspend fun deleteTransactionById(transactionId: String)
    
    @Query("SELECT COUNT(*) FROM transactions WHERE senderId = :userId AND initiatedAt >= :dateInMillis")
    suspend fun getDailyTransactionCount(userId: String, dateInMillis: Long): Int
    
    @Query("SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE senderId = :userId AND status = 'COMPLETED' AND initiatedAt >= :dateInMillis")
    suspend fun getDailyTransactionSum(userId: String, dateInMillis: Long): Double
}

/**
 * ApprovedMemberDao - Database operations for ApprovedMember entity
 * File: ApprovedMemberDao.kt
 * Purpose: Provide CRUD operations for approved family members
 */
@Dao
interface ApprovedMemberDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: ApprovedMember)
    
    @Query("SELECT * FROM approved_members WHERE memberId = :memberId")
    suspend fun getMemberById(memberId: String): ApprovedMember?
    
    @Query("SELECT * FROM approved_members WHERE userId = :userId ORDER BY memberPosition ASC")
    fun getMembersByUserFlow(userId: String): Flow<List<ApprovedMember>>
    
    @Query("SELECT * FROM approved_members WHERE userId = :userId AND memberPosition = :position")
    suspend fun getMemberByPosition(userId: String, position: Int): ApprovedMember?
    
    @Query("SELECT COUNT(*) FROM approved_members WHERE userId = :userId AND status = 'APPROVED'")
    suspend fun getApprovedMemberCount(userId: String): Int
    
    @Update
    suspend fun updateMember(member: ApprovedMember)
    
    @Delete
    suspend fun deleteMember(member: ApprovedMember)
    
    @Query("UPDATE approved_members SET status = :status, approvalDate = :date WHERE memberId = :memberId")
    suspend fun updateMemberStatus(memberId: String, status: String, date: Long? = null)
    
    @Query("SELECT * FROM approved_members WHERE userId = :userId AND status = 'APPROVED' AND canApproveTransactions = 1")
    suspend fun getActiveApprovers(userId: String): List<ApprovedMember>
}

/**
 * ApprovalRequestDao - Database operations for ApprovalRequest entity
 * File: ApprovalRequestDao.kt
 * Purpose: Provide CRUD operations for approval requests
 */
@Dao
interface ApprovalRequestDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApprovalRequest(request: ApprovalRequest)
    
    @Query("SELECT * FROM approval_requests WHERE requestId = :requestId")
    suspend fun getApprovalRequestById(requestId: String): ApprovalRequest?
    
    @Query("SELECT * FROM approval_requests WHERE transactionId = :transactionId")
    fun getApprovalsByTransactionFlow(transactionId: String): Flow<List<ApprovalRequest>>
    
    @Query("SELECT * FROM approval_requests WHERE userId = :userId ORDER BY createdAt DESC")
    fun getApprovalsByUserFlow(userId: String): Flow<List<ApprovalRequest>>
    
    @Query("SELECT * FROM approval_requests WHERE approverMemberId = :memberId AND status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingApprovalsForMemberFlow(memberId: String): Flow<List<ApprovalRequest>>
    
    @Update
    suspend fun updateApprovalRequest(request: ApprovalRequest)
    
    @Delete
    suspend fun deleteApprovalRequest(request: ApprovalRequest)
    
    @Query("SELECT COUNT(*) FROM approval_requests WHERE userId = :userId AND status = 'PENDING'")
    suspend fun getPendingApprovalCount(userId: String): Int
    
    @Query("UPDATE approval_requests SET status = :status, approvedAt = :date WHERE requestId = :requestId")
    suspend fun approveRequest(requestId: String, status: String, date: Long)
    
    @Query("SELECT * FROM approval_requests WHERE transactionId = :transactionId AND status = 'PENDING'")
    suspend fun getPendingApprovalsForTransaction(transactionId: String): List<ApprovalRequest>
}

/**
 * PaymentNotificationDao - Database operations for PaymentNotification entity
 * File: PaymentNotificationDao.kt
 * Purpose: Provide CRUD operations for payment notifications
 */
@Dao
interface PaymentNotificationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: PaymentNotification)
    
    @Query("SELECT * FROM payment_notifications WHERE notificationId = :notificationId")
    suspend fun getNotificationById(notificationId: String): PaymentNotification?
    
    @Query("SELECT * FROM payment_notifications WHERE userId = :userId AND isArchived = 0 ORDER BY sentAt DESC")
    fun getNotificationsFlow(userId: String): Flow<List<PaymentNotification>>
    
    @Query("SELECT * FROM payment_notifications WHERE userId = :userId AND isRead = 0 ORDER BY sentAt DESC")
    fun getUnreadNotificationsFlow(userId: String): Flow<List<PaymentNotification>>
    
    @Update
    suspend fun updateNotification(notification: PaymentNotification)
    
    @Delete
    suspend fun deleteNotification(notification: PaymentNotification)
    
    @Query("UPDATE payment_notifications SET isRead = 1, readAt = :timestamp WHERE notificationId = :notificationId")
    suspend fun markAsRead(notificationId: String, timestamp: Long)
    
    @Query("UPDATE payment_notifications SET isArchived = 1, archivedAt = :timestamp WHERE notificationId = :notificationId")
    suspend fun archiveNotification(notificationId: String, timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM payment_notifications WHERE userId = :userId AND isRead = 0")
    suspend fun getUnreadCount(userId: String): Int
}

/**
 * FraudAlertDao - Database operations for FraudAlert entity
 * File: FraudAlertDao.kt
 * Purpose: Provide CRUD operations for fraud alerts
 */
@Dao
interface FraudAlertDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: FraudAlert)
    
    @Query("SELECT * FROM fraud_alerts WHERE alertId = :alertId")
    suspend fun getAlertById(alertId: String): FraudAlert?
    
    @Query("SELECT * FROM fraud_alerts WHERE userId = :userId ORDER BY detectedAt DESC")
    fun getAlertsByUserFlow(userId: String): Flow<List<FraudAlert>>
    
    @Query("SELECT * FROM fraud_alerts WHERE userId = :userId AND status = 'PENDING' ORDER BY severity DESC, detectedAt DESC")
    fun getPendingAlertsFlow(userId: String): Flow<List<FraudAlert>>
    
    @Update
    suspend fun updateAlert(alert: FraudAlert)
    
    @Delete
    suspend fun deleteAlert(alert: FraudAlert)
    
    @Query("SELECT COUNT(*) FROM fraud_alerts WHERE userId = :userId AND status = 'PENDING'")
    suspend fun getPendingAlertCount(userId: String): Int
}

/**
 * DeviceInfoDao - Database operations for DeviceInfo entity
 * File: DeviceInfoDao.kt
 * Purpose: Provide CRUD operations for device information
 */
@Dao
interface DeviceInfoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeviceInfo(device: DeviceInfo)
    
    @Query("SELECT * FROM device_info WHERE deviceId = :deviceId")
    suspend fun getDeviceInfoById(deviceId: String): DeviceInfo?
    
    @Query("SELECT * FROM device_info WHERE userId = :userId")
    fun getDevicesByUserFlow(userId: String): Flow<List<DeviceInfo>>
    
    @Update
    suspend fun updateDeviceInfo(device: DeviceInfo)
    
    @Delete
    suspend fun deleteDeviceInfo(device: DeviceInfo)
    
    @Query("UPDATE device_info SET isVerified = 1, verificationDate = :date WHERE deviceId = :deviceId")
    suspend fun verifyDevice(deviceId: String, date: Long)
    
    @Query("SELECT COUNT(*) FROM device_info WHERE userId = :userId AND isVerified = 1")
    suspend fun getVerifiedDeviceCount(userId: String): Int
}
