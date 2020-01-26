package com.kanzankazu.itungitungan.util

import android.view.View
import android.widget.TextView

object AppUtil {
    fun checkStringNVisibilityView(string: String, textView: TextView): Boolean {
        return if (string.isNotEmpty()) {
            textView.text = string
            textView.visibility = View.VISIBLE
            false
        } else {
            textView.visibility = View.GONE
            true
        }
    }
}
