package com.example.myapplication.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R

class QrCodeDialog(context: Context, val url: String) : Dialog(context) {

    val ERROR_BANNER_OPTION = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.drawable.ic_launcher_background)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_qrcode)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val v = window!!.decorView
        v.setBackgroundResource(R.color.white)

        val imgQrcode: ImageView = findViewById(R.id.img_qrcode)
        val imgClose: ImageView = findViewById(R.id.img_close)

        imgClose.setOnClickListener { dismiss() }

        Glide.with(context)
            .asBitmap()
            .load(url)
            .apply(ERROR_BANNER_OPTION)
            .into(imgQrcode)
    }
}