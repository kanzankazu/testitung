package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.view.base.BaseDialogFragment
import kotlinx.android.synthetic.main.frgament_give_star_dialog.*


/**
 * Created by Faisal Bahri on 2020-02-04.
 */
class GiveStarDialogFragment : BaseDialogFragment(), GiveStarDialogContract.View {

    companion object {
        private const val ARG_PARAM1 = "arg_param"

        fun newInstance(hutang: Hutang): GiveStarDialogFragment {
            val fragment = GiveStarDialogFragment()
            val args = Bundle()
            //args.putParcelable(ARG_PARAM1, hutang)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): GiveStarDialogFragment {
            return GiveStarDialogFragment()
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
        val view = inflater.inflate(R.layout.frgament_give_star_dialog, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //mPresenter = GiveStarDialogPresenter(activity as AppCompatActivity, this)

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
        showRetryDialog {}
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
        rb_give_star_donate.setOnRatingBarChangeListener { ratingBar, _, _ ->
            when (ratingBar.rating.toInt()) {
                1 -> tv_give_star_result.text = "Very bad"
                2 -> tv_give_star_result.text = "Need some improvement"
                3 -> tv_give_star_result.text = "Good"
                4 -> tv_give_star_result.text = "Great"
                5 -> tv_give_star_result.text = "Awesome. I love it"
                else -> tv_give_star_result.text = ""
            }
        }
        tv_give_star_send.setOnClickListener {
            DialogUtil.showIntroductionDialog(mActivity, "", "Informasi", "Untuk saat ini aplikasi belum terupload ke playstore, support kami terus agar bisa menjadikan aplikasi ini go internasional.", "Donasi", "Tutup", false, -1, object : DialogUtil.IntroductionButtonListener {
                override fun onFirstButtonClick() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onSecondButtonClick() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }
        tv_give_star_close.setOnClickListener { dismiss() }
    }
}
