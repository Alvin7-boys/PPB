package com.example.android.tubesppb.room.user

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.tubesppb.room.laundry.Order
import com.example.android.tubesppb.room.laundry.OrderDao

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDB : RoomDatabase(){

    abstract fun UserDao() : UserDao

    companion object {

        @Volatile private var instance : UserDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDB::class.java,
            "User.db"
        ).build()

    }
}