package com.example.android.tubesppb.room.laundry

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddOrder(application: Application) : AndroidViewModel(application) {
    private val orderDao: OrderDao = OrderDB(application).OrderDao()

    fun addDataLaundry(strTitle: String, totalItems: Int, totalPrice: Double) {
        val order = Order(
            id = 0, // Auto-generated primary key
            uid = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            strTitle = strTitle,
            totalItems = totalItems,
            totalPrice = totalPrice,
            date = System.currentTimeMillis(),
            status = "On Process"
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                orderDao.insertOrder(order)
            }
        }
    }
}