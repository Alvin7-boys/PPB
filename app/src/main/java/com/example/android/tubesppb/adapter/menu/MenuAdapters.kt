package com.example.android.tubesppb.adapter.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tubesppb.R


class MenuAdapters(private val context: Context, items: List<Menu>) :
    RecyclerView.Adapter<MenuAdapters.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var menuList: List<Menu> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Menu = menuList[position]
        holder.imageMenu.setImageResource(data.getImageDrawable())
        holder.tvTitle.text = data.getTvTitle()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cvMenu: CardView = itemView.findViewById(R.id.cvMenu)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitlemenu)
        val imageMenu: ImageView = itemView.findViewById(R.id.imageMenu)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(menuList[position])
                }
            }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(modelMenu: Menu)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}


