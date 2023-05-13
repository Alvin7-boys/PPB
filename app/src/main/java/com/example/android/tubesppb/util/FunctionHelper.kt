package com.example.android.tubesppb.util

import android.text.format.DateFormat
import java.text.DecimalFormat
import java.util.*

object FunctionHelper {
    fun rupiahFormat(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return "Rp " + formatter.format(price).replace(",", ".")
    }

    val today: String
        get() {
            val date: Date = Calendar.getInstance().getTime()
            return DateFormat.format("d MMMM yyyy", date).toString()
        }
}

