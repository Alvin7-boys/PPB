package com.example.android.tubesppb.room.laundry

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Order::class],
    version = 1
)
abstract class OrderDB : RoomDatabase(){

    abstract fun OrderDao() : OrderDao

    companion object {

        @Volatile private var instance : OrderDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            OrderDB::class.java,
            "Order.db"
        ).build()

    }
}