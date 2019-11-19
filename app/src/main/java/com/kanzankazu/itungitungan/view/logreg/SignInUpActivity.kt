package com.kanzankazu.itungitungan.view.logreg

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Firebase.FirebaseLoginUtil
import com.kanzankazu.itungitungan.util.FragmentUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.sample.SampleCrudMainActivity


/**
 * Created by Faisal Bahri on 2019-11-05.
 */
class SignInUpActivity : BaseActivity(), SignInUpContract.View {

    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentUtil: FragmentUtil
    private lateinit var viewPager: ViewPager

    private var slidePagerAdapter: FragmentUtil.SlidePagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initContent()

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.d("Lihat", "onStart SignInUpActivity $currentUser")
        if (currentUser != null) {
            moveToNext()
        }
    }

    private fun initContent() {
        if (FirebaseLoginUtil.isSignIn()) {
            moveToNext()
        }

        fragmentUtil = FragmentUtil(this, -1)

        viewPager = findViewById(R.id.vp_signInUp)

        slidePagerAdapter = fragmentUtil.setupTabLayoutViewPager(
                null,
                null,
                null,
                viewPager,
                SignInFragment.newInstance(),
                SignUpFragment.newInstance()
        )
    }

    fun moveToNext() {
        startActivity(Intent(this, SampleCrudMainActivity::class.java))
        finish()
    }

}
