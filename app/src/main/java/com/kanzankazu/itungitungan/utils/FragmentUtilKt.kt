package com.kanzankazu.itungitungan.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Created by Faisal Bahri on 2019-10-16.
 */

class FragmentUtilKt {
    private val activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    fun setBottomNavView(bottomNavigationView: BottomNavigationView, fragment: Fragment, vararg fragments: Array<Fragment>) {
    }

}