package com.kanzankazu.itungitungan.util

import android.view.View
import android.widget.TextView

object AppUtil {
    fun checkIsStringNotEmptyVisibilityView(string: String, textView: TextView): Boolean {
        return if (string.isNotEmpty()) {
            textView.text = string
            textView.visibility = View.VISIBLE
            true
        } else {
            textView.visibility = View.GONE
            false
        }
    }
}
