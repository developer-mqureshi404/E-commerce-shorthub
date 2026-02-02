package com.example.tutedude.ecommerce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tutedude.ecommerce.data.model.CartItem

@Database(
    entities = [CartItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ECommerceDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}