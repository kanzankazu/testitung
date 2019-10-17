package com.kanzankazu.itungitungan.utils

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        val fragmentManager = mActivity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (bundle != null) {
            fragment.arguments = bundle
        }
        fragmentTransaction.add(targetView, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
        fragmentTransaction.commit()
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
    fun setupBottomNavigationViewView(ft: FragmentTransaction, bottomNavigationView: BottomNavigationView, idMenus: IntArray, firstFragment: Fragment, vararg fragments: Fragment) {
        initFirstTab(ft, firstFragment)
        initListener(bottomNavigationView, idMenus, *fragments)
    }

    private fun initFirstTab(transaction: FragmentTransaction, fragment: Fragment) {
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
    fun setupTabLayoutViewPager(@IdRes tabColorSelect: Int, @IdRes tabColorUnSelect: Int, titleTab: Array<String>, iconTab: IntArray, tabLayout: TabLayout, viewPager: ViewPager, vararg fragments: Fragment, fraglist: MutableList<Fragment>): SlidePagerAdapter {

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

        return mPagerAdapter
    }

    class SlidePagerAdapter : FragmentPagerAdapter {

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

        val tvTitleStepOne = mActivity.findViewById<TextView>(R.id.tv_title_step_one)
        val tvTitleStepTwo = mActivity.findViewById<TextView>(R.id.tv_title_step_two)
        val tvTitleStepThree = mActivity.findViewById<TextView>(R.id.tv_title_step_three)

        val tvActionStepOne = mActivity.findViewById<TextView>(R.id.tv_action_step_one)
        val tvActionStepTwo = mActivity.findViewById<TextView>(R.id.tv_action_step_two)
        val tvActionStepThree = mActivity.findViewById<TextView>(R.id.tv_action_step_three)

        val ivArrowOne = mActivity.findViewById<ImageView>(R.id.iv_arrow)
        val ivArrowTwo = mActivity.findViewById<ImageView>(R.id.iv_arrow2)

        when (step) {
            1 -> {
                tvTitleStepTwo.setTextColor(stepColorUnSelect)
                tvActionStepTwo.setTextColor(stepColorUnSelect)

                tvTitleStepThree.setTextColor(stepColorUnSelect)
                tvActionStepThree.setTextColor(stepColorUnSelect)

                tvTitleStepOne.setTextColor(stepColorSelect)
                tvActionStepOne.setTextColor(stepColorSelect)
                ivArrowOne.setColorFilter(stepColorSelect, PorterDuff.Mode.SRC_ATOP)
                ivArrowTwo.setColorFilter(stepColorUnSelect, PorterDuff.Mode.SRC_ATOP)
            }
            2 -> {
                ivArrowOne.setColorFilter(stepColorUnSelect, PorterDuff.Mode.SRC_ATOP)
                tvTitleStepOne.setTextColor(stepColorUnSelect)
                tvActionStepOne.setTextColor(stepColorUnSelect)

                tvTitleStepTwo.setTextColor(stepColorSelect)
                tvActionStepTwo.setTextColor(stepColorSelect)
                ivArrowTwo.setColorFilter(stepColorSelect, PorterDuff.Mode.SRC_ATOP)

                tvTitleStepThree.setTextColor(stepColorUnSelect)
                tvActionStepThree.setTextColor(stepColorUnSelect)
            }
            3 -> {
                tvTitleStepThree.setTextColor(stepColorSelect)
                tvActionStepThree.setTextColor(stepColorSelect)

                ivArrowTwo.setColorFilter(stepColorUnSelect, PorterDuff.Mode.SRC_ATOP)
                tvTitleStepTwo.setTextColor(stepColorUnSelect)
                tvActionStepTwo.setTextColor(stepColorUnSelect)
            }
        }
    }

}