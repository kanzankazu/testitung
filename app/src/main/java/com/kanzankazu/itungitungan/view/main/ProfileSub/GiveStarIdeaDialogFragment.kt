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
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.view.base.BaseDialogFragment
import kotlinx.android.synthetic.main.frgament_give_star_dialog.*


/**
 * Created by Faisal Bahri on 2020-02-04.
 */
class GiveStarIdeaDialogFragment : BaseDialogFragment(), GiveStarDialogContract.View {

    private var isGiveStar: Boolean = false

    companion object {
        private const val ARG_PARAM1 = "arg_param"

        fun newInstance(isGiveStar: Boolean): GiveStarIdeaDialogFragment {
            val fragment = GiveStarIdeaDialogFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM1, isGiveStar)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): GiveStarIdeaDialogFragment {
            return GiveStarIdeaDialogFragment()
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
            if (arguments!!.containsKey(ARG_PARAM1)) {
                isGiveStar = arguments!!.getBoolean(ARG_PARAM1)
            }
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
        if (isGiveStar) {
            iv_give_star_idea_idea.visibility = View.GONE
            ll_give_star_idea_star.visibility = View.VISIBLE
            til_give_star_idea_note.visibility = View.GONE
        } else {
            iv_give_star_idea_idea.visibility = View.VISIBLE
            ll_give_star_idea_star.visibility = View.GONE
            til_give_star_idea_note.visibility = View.VISIBLE
        }
    }

    private fun initListener() {
        rb_give_star_idea_star.setOnRatingBarChangeListener { ratingBar, _, _ ->
            Log.d("Lihat initListener GiveStarIdeaDialogFragment", ratingBar.rating.toInt().toString())
            when {
                ratingBar.rating.toInt() < 3 -> til_give_star_idea_note.visibility = View.VISIBLE
                else -> {
                    til_give_star_idea_note.visibility = View.GONE
                    hideKeyboard()
                }
            }

            when (ratingBar.rating.toInt()) {
                1 -> tv_give_star_idea_star_result.text = "Tidak Bagus, Masih banyak kekurangan"
                2 -> tv_give_star_idea_star_result.text = "Lumayan, Butuh pengembangan"
                3 -> tv_give_star_idea_star_result.text = "Bagus"
                4 -> tv_give_star_idea_star_result.text = "Sangat Bagus"
                5 -> tv_give_star_idea_star_result.text = "Keren, Saya suka aplikasi ini"
                else -> tv_give_star_idea_star_result.text = "Gimana nih bagus nggak aplikasinya?, kasih masukan aja deh kalo masih bingung"
            }
        }
        tv_give_star_idea_send.setOnClickListener {
            if (isGiveStar) {
                DialogUtil.showIntroductionDialog(
                    mActivity,
                    "",
                    "Informasi",
                    "Untuk saat ini aplikasi belum terupload ke playstore, support kami terus agar bisa menjadikan aplikasi ini go internasional.",
                    "Donasi",
                    "Tutup",
                    false,
                    -1,
                    object : DialogUtil.IntroductionButtonListener {
                        override fun onFirstButtonClick() {}

                        override fun onSecondButtonClick() {}

                    }
                )
            } else {
                showToast(mActivity.getString(R.string.message_info_under_development))
            }
        }
        tv_give_star_idea_close.setOnClickListener { dismiss() }
    }
}
