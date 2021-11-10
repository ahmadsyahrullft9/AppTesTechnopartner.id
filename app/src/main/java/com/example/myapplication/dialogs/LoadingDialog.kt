package com.example.myapplication.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.myapplication.R

class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
    }
}