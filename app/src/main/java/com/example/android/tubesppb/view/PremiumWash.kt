package com.example.android.tubesppb.view

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.android.tubesppb.databinding.ActivityLaundryBinding
import com.example.android.tubesppb.room.laundry.AddOrder
import com.example.android.tubesppb.util.FunctionHelper

@Suppress("DEPRECATION")
class PremiumWash : AppCompatActivity() {
    private lateinit var binding: ActivityLaundryBinding

    companion object {
        const val DATA_TITLE = "TITLE"
    }
    private var hargaKaos = 9000
    private var hargaCelana = 8000
    private var hargaJaket = 10000
    private var hargaSprei = 70000
    private var hargaKarpet = 250000
    private var itemCount1 = 0
    private var itemCount2 = 0
    private var itemCount3 = 0
    private var itemCount4 = 0
    private var itemCount5 = 0
    private var countKaos = 0
    private var countCelana = 0
    private var countJaket = 0
    private var countSprei = 0
    private var countKarpet = 0
    private var totalItems = 0
    private var totalPrice = 0
    private lateinit var strTitle: String
    private lateinit var btnCheckout: Button
    private lateinit var imageAdd1: ImageView
    private lateinit var imageAdd2: ImageView
    private lateinit var imageAdd3: ImageView
    private lateinit var imageAdd4: ImageView
    private lateinit var imageAdd5: ImageView
    private lateinit var imageMinus1: ImageView
    private lateinit var imageMinus2: ImageView
    private lateinit var imageMinus3: ImageView
    private lateinit var imageMinus4: ImageView
    private lateinit var imageMinus5: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvJumlahBarang: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvKaos: TextView
    private lateinit var tvCelana: TextView
    private lateinit var tvJaket: TextView
    private lateinit var tvSprei: TextView
    private lateinit var tvKarpet: TextView
    private lateinit var tvPriceKaos: TextView
    private lateinit var tvPriceCelana: TextView
    private lateinit var tvPriceJaket: TextView
    private lateinit var tvPriceSprei: TextView
    private lateinit var tvPriceKarpet: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaundryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setmaxLayout()
        setStatusbar()
        setInitLayout()
        setDataKaos()
        setDataCelana()
        setDataJaket()
        setDataSprei()
        setDataKarpet()
        setInputData()

    }

    private fun setStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }
    @SuppressLint("SetTextI18n")
    private fun setInitLayout() {
        tvTitle = binding.tvTitlelaundry
        tvInfo = binding.tvInfo
        tvJumlahBarang = binding.tvJumlahBarang
        tvTotalPrice = binding.tvTotalPrice
        tvKaos = binding.layoutProduk.tvKaos
        tvCelana = binding.layoutProduk.tvCelana
        tvJaket =  binding.layoutProduk.tvJaket
        tvSprei =  binding.layoutProduk.tvSprei
        tvKarpet =  binding.layoutProduk.tvKarpet
        tvPriceKaos =  binding.layoutProduk.tvPriceKaos
        tvPriceCelana =  binding.layoutProduk.tvPriceCelana
        tvPriceJaket =  binding.layoutProduk.tvPriceJaket
        tvPriceSprei =  binding.layoutProduk.tvPriceSprei
        tvPriceKarpet =  binding.layoutProduk.tvPriceKarpet
        imageAdd1 =  binding.layoutProduk.imageAdd1
        imageAdd2 =  binding.layoutProduk.imageAdd2
        imageAdd3 =  binding.layoutProduk.imageAdd3
        imageAdd4 =  binding.layoutProduk.imageAdd4
        imageAdd5 =  binding.layoutProduk.imageAdd5
        imageMinus1 =  binding.layoutProduk.imageMinus1
        imageMinus2 =  binding.layoutProduk.imageMinus2
        imageMinus3 =  binding.layoutProduk.imageMinus3
        imageMinus4 =  binding.layoutProduk.imageMinus4
        imageMinus5 =  binding.layoutProduk.imageMinus5
        btnCheckout =  binding.btnCheckout
        strTitle = intent.extras!!.getString(DATA_TITLE)!!
        tvTitle.text = strTitle
        tvJumlahBarang.text = "0 items"
        tvTotalPrice.text = "Rp 0"
        tvInfo.text = "Premium Wash menawarkan treatment eksklusif, menggunakan chemical yang environment friendly dan service sepenuh hati."

    }
    private fun setDataKaos() {
        tvKaos.text = FunctionHelper.rupiahFormat(hargaKaos)
        imageAdd1.setOnClickListener {
            itemCount1 += 1
            tvPriceKaos.text = itemCount1.toString()
            countKaos = hargaKaos * itemCount1
            setTotalPrice()
        }
        imageMinus1.setOnClickListener {
            if (itemCount1 > 0) {
                itemCount1 -= 1
                tvPriceKaos.text = itemCount1.toString()
            }
            countKaos = hargaKaos * itemCount1
            setTotalPrice()
        }
    }
    private fun setDataCelana() {
        tvCelana.text = FunctionHelper.rupiahFormat(hargaCelana)
        imageAdd2.setOnClickListener {
            itemCount2 += 1
            tvPriceCelana.text = itemCount2.toString()
            countCelana = hargaCelana * itemCount2
            setTotalPrice()
        }
        imageMinus2.setOnClickListener {
            if (itemCount2 > 0) {
                itemCount2 -= 1
                tvPriceCelana.text = itemCount2.toString()
            }
            countCelana = hargaCelana * itemCount2
            setTotalPrice()
        }
    }
    private fun setDataJaket() {
        tvJaket.text = FunctionHelper.rupiahFormat(hargaJaket)
        imageAdd3.setOnClickListener {
            itemCount3 += 1
            tvPriceJaket.text = itemCount3.toString()
            countJaket = hargaJaket * itemCount3
            setTotalPrice()
        }
        imageMinus3.setOnClickListener {
            if (itemCount3 > 0) {
                itemCount3 -= 1
                tvPriceJaket.text = itemCount3.toString()
            }
            countJaket = hargaJaket * itemCount3
            setTotalPrice()
        }
    }

    private fun setDataSprei() {
        tvSprei.text = FunctionHelper.rupiahFormat(hargaSprei)
        imageAdd4.setOnClickListener {
            itemCount4 += 1
            tvPriceSprei.text = itemCount4.toString()
            countSprei = hargaSprei * itemCount4
            setTotalPrice()
        }
        imageMinus4.setOnClickListener {
            if (itemCount4 > 0) {
                itemCount4 -= 1
                tvPriceSprei.text = itemCount4.toString()
            }
            countSprei = hargaSprei * itemCount4
            setTotalPrice()
        }
    }
    private fun setmaxLayout() {
        val nestedScrollView = binding.layoutItem
        val display: Display = windowManager.defaultDisplay
        val width = display.width

        if (width > 1000) {
            val layoutParams = nestedScrollView.layoutParams
            layoutParams.width = 1000
            nestedScrollView.layoutParams = layoutParams
        } else {
            val layoutParams = nestedScrollView.layoutParams
            layoutParams.width = width
            nestedScrollView.layoutParams = layoutParams
        }

    }
    private fun setDataKarpet() {
        tvKarpet.text = FunctionHelper.rupiahFormat(hargaKarpet)
        imageAdd5.setOnClickListener {
            itemCount5 += 1
            tvPriceKarpet.text = itemCount5.toString()
            countKarpet = hargaKarpet * itemCount5
            setTotalPrice()
        }
        imageMinus5.setOnClickListener {
            if (itemCount5 > 0) {
                itemCount5 -= 1
                tvPriceKarpet.text = itemCount5.toString()
            }
            countKarpet = hargaKarpet * itemCount5
            setTotalPrice()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTotalPrice() {
        totalItems = itemCount1 + itemCount2 + itemCount3 + itemCount4 + itemCount5
        totalPrice = countKaos + countCelana + countJaket + countSprei + countKarpet
        tvJumlahBarang.text = "$totalItems items"
        tvTotalPrice.text = FunctionHelper.rupiahFormat(totalPrice)
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

    private fun setInputData() {
        btnCheckout.setOnClickListener {
            if (totalItems == 0 || totalPrice == 0) {
                Toast.makeText(
                    this@PremiumWash,
                    "Harap pilih jenis barang!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val addOrderViewModel = ViewModelProvider(this)[AddOrder::class.java]
                addOrderViewModel.addDataLaundry(strTitle, totalItems, totalPrice.toDouble())
                Toast.makeText(
                    this@PremiumWash,
                    "Pesanan Anda sedang diproses, cek di menu riwayat",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}