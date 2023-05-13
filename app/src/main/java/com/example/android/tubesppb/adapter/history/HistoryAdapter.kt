package com.example.android.tubesppb.adapter.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tubesppb.R
import com.example.android.tubesppb.room.laundry.Order
import com.example.android.tubesppb.util.FunctionHelper
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NAME_SHADOWING")
class HistoryAdapter(private val orders: List<Order>, private val adapterCallback: HistoryAdapterCallback) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvItems: TextView = itemView.findViewById(R.id.tvItems)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val imageDelete: ImageView = itemView.findViewById(R.id.imageDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.tvTitle.text = order.strTitle
        holder.tvDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(order.date))
        holder.tvItems.text = "${order.totalItems}"
        holder.tvPrice.text =  FunctionHelper.rupiahFormat(order.totalPrice.toInt())
        holder.tvStatus.text = order.status

        holder.imageDelete.setOnClickListener {
            val order = orders[position]
            adapterCallback.onDelete(order)
        }
    }
    override fun getItemCount(): Int {
        return orders.size
    }
    interface HistoryAdapterCallback {
        fun onDelete(order: Order)
    }
}