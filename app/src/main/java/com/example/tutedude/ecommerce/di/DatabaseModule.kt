package com.example.tutedude.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.example.tutedude.ecommerce.data.local.CartDao
import com.example.tutedude.ecommerce.data.local.ECommerceDatabase
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
}