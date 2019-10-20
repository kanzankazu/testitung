package com.kanzankazu.itungitungan.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.kanzankazu.itungitungan.R


/**
 * Created by Faisal Bahri on 2019-10-16.
 */

class FragmentUtilKt {

    private var mActivity: AppCompatActivity
    private var targetView: Int

    private lateinit var stepToolbar: Toolbar
    private lateinit var stepfragments: Array<out Fragment>
    private lateinit var stepCurrentFragment: Fragment
    private var stepColorSelect: Int = Color.parseColor("#E0E0E0")
    private var stepColorUnSelect: Int = Color.parseColor("#5bcdcc")


    constructor(mActivity: AppCompatActivity, @IdRes targetView: Int) {
        this.mActivity = mActivity
        this.targetView = targetView
    }

    fun getFragTransaction(): FragmentTransaction {
        return mActivity.supportFragmentManager.beginTransaction()
    }

    fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            getFragTransaction()
                    .replace(targetView, fragment)
                    .commit()
            return true
        }
        return false
    }

    /**
     * STATIC */
    fun initFragment(fragment: Fragment): Fragment {
        return initFragment(fragment, null)
    }

    fun initFragment(fragment: Fragment, bundle: Bundle?): Fragment {
        val ft = getFragTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        ft.add(targetView, fragment)
        ft.addToBackStack(null)
        ft.commitAllowingStateLoss()
        ft.commit()
        return fragment

    }

    fun initFragments(vararg fragment: Fragment) {
        val ft = getFragTransaction()
        for (targetClass in fragment) {
            ft.add(targetView, targetClass)
        }
        ft.commit()
    }

    fun showHidefragment(hideFragment: Fragment, showFragment: Fragment) {
        val ft = getFragTransaction()
        ft.hide(hideFragment)
        ft.show(showFragment)
        ft.commit()

    }

    fun replaceFragment(fragment: Fragment, bundle: Bundle?) {
        fragment.arguments = bundle
        val transaction = mActivity.supportFragmentManager.beginTransaction()
        transaction.replace(targetView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /**
     * BottomNavigationView */
    fun setupBottomNavigationViewView(bottomNavigationView: BottomNavigationView, idMenus: IntArray, firstFragment: Fragment, vararg fragments: Fragment) {
        if (idMenus.size == fragments.size) {
            initFirstTab(firstFragment)
            initListener(bottomNavigationView, idMenus, *fragments)
        } else {
            Snackbar.make(bottomNavigationView, "Fragment dan id menu tidak sama", Snackbar.LENGTH_LONG)
        }
    }

    private fun initFirstTab(fragment: Fragment) {
        replaceFragment(fragment, null)
    }

    private fun initListener(bottomNavigationView: BottomNavigationView, idMenus: IntArray, vararg fragments: Fragment) {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val transaction = mActivity.supportFragmentManager.beginTransaction()

            var selectedFragment: Fragment? = null

            for (i in idMenus.indices) {
                val idMenu = idMenus[i]

                if (menuItem.itemId == idMenu) {
                    selectedFragment = fragments[i]
                }
            }

            transaction.replace(targetView, selectedFragment!!)
            transaction.addToBackStack(null)
            transaction.commit()
            true
        }
    }

    /**
     * ViewPager TabLayout */
    fun setupTabLayoutViewPager(@IdRes tabColorSelect: Int, @IdRes tabColorUnSelect: Int, titleTab: Array<String>, iconTab: IntArray, tabLayout: TabLayout, viewPager: ViewPager, vararg fragments: Fragment): SlidePagerAdapter {

        for (i in titleTab.indices) {
            tabLayout.addTab(tabLayout.newTab().setIcon(iconTab[i]))
            if (i == 0) {
                tabLayout.getTabAt(0)!!.icon!!.setColorFilter(tabColorSelect, PorterDuff.Mode.SRC_IN)
            } else {
                tabLayout.getTabAt(i)!!.icon!!.setColorFilter(tabColorUnSelect, PorterDuff.Mode.SRC_IN)
            }
        }
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.isEnabled = false

        val mPagerAdapter = SlidePagerAdapter(mActivity.supportFragmentManager, fragments)
        viewPager.adapter = mPagerAdapter
        viewPager.offscreenPageLimit = fragments.size
        viewPager.currentItem = 0

        viewPager.addOnAdapterChangeListener(object : ViewPager.OnPageChangeListener, ViewPager.OnAdapterChangeListener {
            override fun onAdapterChanged(viewPager: ViewPager, oldAdapter: PagerAdapter?, newAdapter: PagerAdapter?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("Lihat", "onPageScrollStateChanged FragmentUtilKt" + state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.d("Lihat", "onPageScrolled FragmentUtilKt" + position)
                Log.d("Lihat", "onPageScrolled FragmentUtilKt" + positionOffset)
                Log.d("Lihat", "onPageScrolled FragmentUtilKt" + positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                Log.d("Lihat", "onPageSelected FragmentUtilKt" + position)
            }
        })

        return mPagerAdapter
    }

    inner class SlidePagerAdapter : FragmentPagerAdapter {

        private val fm: FragmentManager
        private val fragments: Array<out Fragment>

        constructor(fm: FragmentManager, fragments: Array<out Fragment>) : super(fm) {
            this.fm = fm
            this.fragments = fragments
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }

    /**
     * Step Fragment*/
    fun setupStepFragment(mActivity: AppCompatActivity, @IdRes targetView: Int, stepToolbar: Toolbar, stepFragments: Array<out Fragment>) {
        this.mActivity = mActivity
        this.targetView = targetView
        this.stepToolbar = stepToolbar
        this.stepfragments = stepFragments

        val ft = getFragTransaction()
        for (fragment in stepFragments) {
            ft.add(targetView, fragment)
        }
        for (i in stepFragments.indices) {
            if (i == 0) {
                ft.show(stepFragments[i])
            } else {
                ft.hide(stepFragments[i])
            }
        }
        ft.commit()
    }

    /**
     * Setup In onBackPressed*/
    fun stepFragmentBackPressed() {
        if (stepfragments.isNotEmpty()) {
            for ((i, fragment) in stepfragments.withIndex()) {
                if (fragment.isVisible) {
                    stepCurrentFragment = fragment
                    stepToolbarUpdate(i)
                }
            }
        }
    }

    fun stepToolbarUpdate(step: Int) {
        initStepToolbarPreApproval(step)
    }

    fun initStepToolbarPreApproval(step: Int) {
        mActivity.setSupportActionBar(stepToolbar)
        mActivity.supportActionBar!!.title = ""
        mActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        stepToolbar.setNavigationOnClickListener { mActivity.onBackPressed() }

        val linearLayoutHorizontal = ViewUtil.linearLayoutView(mActivity, null, 10, LinearLayout.HORIZONTAL, Gravity.CENTER)
        val linearLayoutVertical = ViewUtil.linearLayoutView(mActivity, null, 10, LinearLayout.VERTICAL, Gravity.CENTER)

        val textViewTitle = ViewUtil.textView(mActivity)
        val textViewSubTitle = ViewUtil.textView(mActivity)
        val imageView = ViewUtil.imageView(mActivity)

        for (i in stepfragments.indices) {
            if (i == (stepfragments.size - 1)) { //Jika Sudah Data Terakhir
                if (step == i + 1) {
                    textViewTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent))
                    textViewSubTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent))
                } else {
                    textViewTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                    textViewSubTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                }
                linearLayoutVertical.addView(textViewTitle)
                linearLayoutVertical.addView(textViewSubTitle)
                linearLayoutHorizontal.addView(linearLayoutVertical)
            } else {
                if (step == i + 1) {
                    textViewTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent))
                    textViewSubTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorAccent))
                    imageView.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorAccent))
                } else {
                    textViewTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                    textViewSubTitle.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                    imageView.setColorFilter(ContextCompat.getColor(mActivity, R.color.colorPrimary))
                }
                linearLayoutVertical.addView(textViewTitle)
                linearLayoutVertical.addView(textViewSubTitle)
                linearLayoutHorizontal.addView(linearLayoutVertical)
                linearLayoutHorizontal.addView(imageView)
            }
        }
    }

}