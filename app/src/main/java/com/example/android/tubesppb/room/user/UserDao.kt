package com.example.android.tubesppb.room.user


import androidx.room.*


@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Query("UPDATE User SET username = :username, phoneNumber = :phoneNumber, email = :email WHERE uid = :uid")
    fun updateUser(uid: String, username: String, phoneNumber: String, email: String)

    @Query("SELECT * FROM User")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM User WHERE uid = :uid")
    fun getUser(uid: String):User?
}


