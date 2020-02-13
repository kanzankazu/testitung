package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.view.base.BaseActivity

class DonateActivity : BaseActivity() {

    private lateinit var rewardedVideoAd: RewardedVideoAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

    }
}
