package com.kanzankazu.itungitungan.view.logreg

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.main.MainActivity

/**
 * Created by Faisal Bahri on 2019-11-05.
 */
class SignInUpActivity : BaseActivity(), SignInUpContract.View {

    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    private var slidePagerAdapter: FragmentUtil.SlidePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initContent()
    }

    private fun initContent() {
        if (FirebaseLoginUtil.isSignIn()) {
            moveToNext(MainActivity::class.java)
        }

        fragmentUtil = FragmentUtil(this)

        tabLayout = findViewById<TabLayout>(R.id.tablayout)
        viewPager = findViewById<ViewPager>(R.id.vp_signInUp)

        slidePagerAdapter = fragmentUtil.setupTabLayoutViewPager(
                arrayOf("Masuk", "Daftar"),
                null,
                tabLayout,
                viewPager,
                SignInFragment.newInstance(),
                SignUpFragment.newInstance()
        )
    }

    private fun moveToNext(targetClass: Class<*>) {
        startActivity(Intent(this, targetClass))
    }
}
