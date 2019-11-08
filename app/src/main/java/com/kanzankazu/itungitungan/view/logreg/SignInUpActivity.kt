package com.kanzankazu.itungitungan.view.logreg

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.view.util.FragmentUtil

/**
 * Created by Faisal Bahri on 2019-11-05.
 */
class SignInUpActivity : AppCompatActivity() {

    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var slidePagerAdapterInifinite: FragmentUtil.SlidePagerAdapterInfinite? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initContent()
    }

    private fun initContent() {
        fragmentUtil = FragmentUtil(this)

        tabLayout = findViewById<TabLayout>(R.id.tablayout)
        viewPager = findViewById<ViewPager>(R.id.vp_signInUp)

        slidePagerAdapterInifinite = fragmentUtil.setupTabLayoutViewPagerInfinite(
            arrayOf("Masuk", "Daftar"),
            null,
            tabLayout,
            viewPager,
            SignInFragment.newInstance(),
            SignUpFragment.newInstance()
        )
    }
}
