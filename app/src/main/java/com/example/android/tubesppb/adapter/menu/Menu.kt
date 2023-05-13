package com.example.android.tubesppb.adapter.menu

class Menu(imageDrawable: Int, tvTitle: String) {

    private var iconRes: Int = imageDrawable
    var title: String = tvTitle

    fun getTvTitle(): String {
        return title
    }

    fun setTvTitle(tvTitle: String) {
        title = tvTitle
    }

    fun getImageDrawable(): Int {
        return iconRes
    }

    fun setImageDrawable(imageDrawable: Int) {
        iconRes = imageDrawable
    }


}
