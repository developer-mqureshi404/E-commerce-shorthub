package com.example.tutedude.ecommerce.di

import com.example.tutedude.ecommerce.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
    
    // Payment App Repositories
    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        impl: WalletRepository
    ): IWalletRepository
    
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepository
    ): ITransactionRepository
    
    @Binds
    @Singleton
    abstract fun bindApprovedMemberRepository(
        impl: ApprovedMemberRepository
    ): IApprovedMemberRepository
    
    @Binds
    @Singleton
    abstract fun bindApprovalRepository(
        impl: ApprovalRepository
    ): IApprovalRepository
    
    @Binds
    @Singleton
    abstract fun bindFraudDetectionRepository(
        impl: FraudDetectionRepository
    ): IFraudDetectionRepository
}