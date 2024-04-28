package com.kotlin.weather.util

import android.app.Dialog
import android.content.Context
import com.kotlin.weather.R

class CustomProgressDialog(val context: Context) {

    private var progressDialog =  Dialog(context)

    //Progress dialogu full screen layout ile birleştirdik ve göstermesini sağladık
    fun show() {
        progressDialog.setContentView(R.layout.full_screen_progress_bar)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }

    //Progress barı kapatır
    fun dismiss() {
        progressDialog.dismiss()
    }

}