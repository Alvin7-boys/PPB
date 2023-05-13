package com.example.android.tubesppb.view

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tubesppb.adapter.history.HistoryAdapter
import com.example.android.tubesppb.databinding.ActivityHistoryBinding
import com.example.android.tubesppb.room.laundry.Order
import com.example.android.tubesppb.room.laundry.OrderDB
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("DEPRECATION")
class History : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tvNotFound: TextView

    private val db by lazy { OrderDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        setStatusbar()
        historyRecyclerView = binding.rvHistory
        tvNotFound = binding.tvNotFound
        setInitLayout(historyRecyclerView)
        getListOrder { orders ->
            if (orders.isNotEmpty()) {
                // Update the UI or perform other operations with the orders
                val adapter = HistoryAdapter(orders, adapterCallback)
                // Set the adapter to the RecyclerView or update the existing adapter
                historyRecyclerView.adapter = adapter
            } else {
                tvNotFound.visibility = View.VISIBLE
                historyRecyclerView.visibility = View.GONE
            }
        }

    }

    private fun setInitLayout(historyRecyclerView: RecyclerView) {
        tvNotFound.visibility = View.GONE
        historyRecyclerView.layoutManager = LinearLayoutManager(this@History)
        historyRecyclerView.setHasFixedSize(true)
    }

    private fun setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val window: Window = activity.window
        val layoutParams: WindowManager.LayoutParams = window.attributes
        if (on) {
            layoutParams.flags = layoutParams.flags or bits
        } else {
            layoutParams.flags = layoutParams.flags and bits.inv()
        }
        window.attributes = layoutParams
    }

    private val adapterCallback = object : HistoryAdapter.HistoryAdapterCallback {
        override fun onDelete(order: Order) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@History)
            alertDialogBuilder.setMessage("Hapus riwayat ini?")
            alertDialogBuilder.setPositiveButton("Ya, Hapus") { _, _ ->
                val uid: String = order.uid
                val id: Int = order.id
                CoroutineScope(Dispatchers.IO).launch {
                    db.OrderDao().deleteDataById(uid, id)
                }
                Toast.makeText(
                    this@History, "Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT
                ).show()
                recreate()
            }
            alertDialogBuilder.setNegativeButton("Batal") { dialogInterface, _ -> dialogInterface.cancel() }
            val alertDialog: AlertDialog = alertDialogBuilder.create()
            alertDialog.show()

        }
    }

    private fun getListOrder(callback: (List<Order>) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid.toString()
        CoroutineScope(Dispatchers.IO).launch {
            val orderDao = OrderDB.invoke(applicationContext).OrderDao()
            val orders = orderDao.getOrdersByUid(uid)
            withContext(Dispatchers.Main) {
                callback(orders)
            }
        }
    }
}