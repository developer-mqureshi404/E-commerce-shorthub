package com.example.tutedude.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.example.tutedude.ecommerce.data.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideECommerceDatabase(
        @ApplicationContext context: Context
    ): ECommerceDatabase {
        return Room.databaseBuilder(
            context,
            ECommerceDatabase::class.java,
            "ecommerce_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCartDao(database: ECommerceDatabase): CartDao {
        return database.cartDao()
    }
    
    // Payment App DAOs
    @Provides
    @Singleton
    fun provideWalletDao(database: ECommerceDatabase): WalletDao {
        return database.walletDao()
    }
    
    @Provides
    @Singleton
    fun provideTransactionDao(database: ECommerceDatabase): TransactionDao {
        return database.transactionDao()
    }
    
    @Provides
    @Singleton
    fun provideApprovedMemberDao(database: ECommerceDatabase): ApprovedMemberDao {
        return database.approvedMemberDao()
    }
    
    @Provides
    @Singleton
    fun provideApprovalRequestDao(database: ECommerceDatabase): ApprovalRequestDao {
        return database.approvalRequestDao()
    }
    
    @Provides
    @Singleton
    fun providePaymentNotificationDao(database: ECommerceDatabase): PaymentNotificationDao {
        return database.paymentNotificationDao()
    }
    
    @Provides
    @Singleton
    fun provideFraudAlertDao(database: ECommerceDatabase): FraudAlertDao {
        return database.fraudAlertDao()
    }
    
    @Provides
    @Singleton
    fun provideDeviceInfoDao(database: ECommerceDatabase): DeviceInfoDao {
        return database.deviceInfoDao()
    }
}