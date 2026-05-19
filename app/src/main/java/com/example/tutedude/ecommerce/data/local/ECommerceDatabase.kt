package com.example.tutedude.ecommerce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tutedude.ecommerce.data.model.*

@Database(
    entities = [
        CartItem::class,
        Wallet::class,
        Transaction::class,
        ApprovedMember::class,
        ApprovalRequest::class,
        QRTransaction::class,
        PaymentNotification::class,
        FraudAlert::class,
        DeviceInfo::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ECommerceDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun walletDao(): WalletDao
    abstract fun transactionDao(): TransactionDao
    abstract fun approvedMemberDao(): ApprovedMemberDao
    abstract fun approvalRequestDao(): ApprovalRequestDao
    abstract fun paymentNotificationDao(): PaymentNotificationDao
    abstract fun fraudAlertDao(): FraudAlertDao
    abstract fun deviceInfoDao(): DeviceInfoDao
}