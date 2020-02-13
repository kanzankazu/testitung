package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.view.base.BaseDialogFragment
import kotlinx.android.synthetic.main.fragment_donate_option_dialog.*


/**
 * Created by Faisal Bahri on 2020-02-04.
 */
class DonateOptionDialogFragment : BaseDialogFragment(), DonateOptionDialogContract.View {

    private var isRewardVideoComplete: Boolean = false

    companion object {
        private const val ARG_PARAM1 = "arg_param"

        fun newInstance(hutang: Hutang): DonateOptionDialogFragment {
            val fragment = DonateOptionDialogFragment()
            val args = Bundle()
            //args.putParcelable(ARG_PARAM1, hutang)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): DonateOptionDialogFragment {
            return DonateOptionDialogFragment()
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        // request a window without the title
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.attributes.windowAnimations = R.style.AnimationRTL
        return dialog
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window.setLayout(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            //hutang = arguments!!.getParcelable(ARG_PARAM1)
        }

        // Pick a style based on the num.
        var style = STYLE_NORMAL
        var theme = 0
        val mNum = 0
        when ((mNum - 1) % 6) {
            1 -> style = STYLE_NO_TITLE
            2 -> style = STYLE_NO_FRAME
            3 -> style = STYLE_NO_INPUT
            4 -> style = STYLE_NORMAL
            5 -> style = STYLE_NORMAL
            6 -> style = STYLE_NO_TITLE
            7 -> style = STYLE_NO_FRAME
            8 -> style = STYLE_NORMAL
        }
        when ((mNum - 1) % 6) {
            4 -> theme = android.R.style.Theme_Holo
            5 -> theme = android.R.style.Theme_Holo_Light_Dialog
            6 -> theme = android.R.style.Theme_Holo_Light
            7 -> theme = android.R.style.Theme_Holo_Light_Panel
            8 -> theme = android.R.style.Theme_Holo_Light
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_donate_option_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //mPresenter = DonateOptionDialogPresenter(activity as AppCompatActivity, this)
        setupRewardVideoLegacyApi(object : RewardedVideoAdListener {
            override fun onRewardedVideoAdClosed() {
                if (isRewardVideoComplete) dialogViewAgain()
            }

            override fun onRewardedVideoAdLeftApplication() {
            }

            override fun onRewardedVideoAdLoaded() {
            }

            override fun onRewardedVideoAdOpened() {
            }

            override fun onRewardedVideoCompleted() {
                isRewardVideoComplete = true
            }

            override fun onRewarded(p0: RewardItem?) {
                Log.d("Lihat onRewarded DonateOptionDialogFragment", p0!!.type)
                Log.d("Lihat onRewarded DonateOptionDialogFragment", p0.amount.toString())
            }

            override fun onRewardedVideoStarted() {
                isRewardVideoComplete = false
            }

            override fun onRewardedVideoAdFailedToLoad(p0: Int) {
            }
        })

        initView()
        initListener()
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView() {
        showRetryDialog {

        }
    }

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    private fun initView() {

    }

    private fun initListener() {
        ll_donate_option_donate.setOnClickListener { }
        ll_donate_option_ads.setOnClickListener { }
        tv_donate_option_close.setOnClickListener { dismiss() }
    }

    private fun dialogViewAgain() {

    }
}
