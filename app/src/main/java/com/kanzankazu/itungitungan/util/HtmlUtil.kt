package com.kanzankazu.itungitungan.util

import android.text.TextUtils

object HtmlUtil {
    fun checkBr(sHtml: String): String {
        val split = sHtml.split("<br>".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (split.size > 1) {
            if (TextUtils.isEmpty(split[1])) {
                sHtml.replace("<br>", "\n")
            } else {
                sHtml.replace("<br>", "")
            }
        } else sHtml
    }
}
