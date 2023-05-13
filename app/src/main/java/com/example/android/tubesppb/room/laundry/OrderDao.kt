package com.example.android.tubesppb.room.laundry

import androidx.room.*

@Dao
interface OrderDao {

    @Query("SELECT * FROM `Order` WHERE uid=:uid")
    fun getOrdersByUid(uid: String): List<Order>

    @Query("SELECT * FROM `Order` WHERE id=:id")
    fun getOrderById(id: Int): Order?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(order: Order)

    @Update
    fun updateOrder(order: Order)

    @Delete
    fun deleteOrder(order: Order)

    @Query("DELETE FROM `Order` WHERE uid = :uid AND id = :id")
    fun deleteDataById(uid: String, id: Int)

}

