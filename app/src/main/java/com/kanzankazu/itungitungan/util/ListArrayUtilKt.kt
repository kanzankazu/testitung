package com.kanzankazu.itungitungan.util

import java.util.*

object ListArrayUtilKt {
    fun getPosListStringIn2List(sourceList: MutableList<String>, s: MutableList<String>): MutableList<Int> {
        val integers = ArrayList<Int>()
        for (i in sourceList.indices) {
            for (ss in s) {
                if (sourceList[i].equals(ss, ignoreCase = true)) {
                    integers.add(i)
                }
            }
        }
        return integers
    }
}