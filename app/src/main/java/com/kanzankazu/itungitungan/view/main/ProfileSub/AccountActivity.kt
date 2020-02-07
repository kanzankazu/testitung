package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_account.*
import kotlin.math.abs

class AccountActivity : BaseActivity() {

    private lateinit var iv_account_user_image: ImageView
    private lateinit var toolbar_account: Toolbar
    private lateinit var appbarlayout_account: AppBarLayout
    private lateinit var tv_account_user_name_long: AppCompatTextView
    private lateinit var tv_account_user_name_short: AppCompatTextView
    private lateinit var fl_account_stuff_container: FrameLayout
    private lateinit var fl_account_background: FrameLayout

    private var EXPAND_AVATAR_SIZE: Float = 135f
    private var COLLAPSE_IMAGE_SIZE: Float = 45f
    private var margin: Float = 8f
    private var cashCollapseState: Pair<Int, Int>? = null
    private var startAvatarAnimatePointY: Float = 0F
    private var animateWeigt: Float = 0F
    private var isCalculated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        setView()
        setListener()
    }

    private fun setView() {
        setCollapseView()


    }

    private fun setCollapseView() {
        appbarlayout_account = findViewById(R.id.appbarlayout_account)
        toolbar_account = findViewById(R.id.toolbar_account)
        toolbar_account.visibility = View.INVISIBLE
        fl_account_stuff_container = findViewById(R.id.fl_account_stuff_container)
        iv_account_user_image = findViewById(R.id.iv_account_user_image)
        tv_account_user_name_long = findViewById(R.id.tv_account_user_name_long)
        tv_account_user_name_short = findViewById(R.id.tv_account_user_name_short)
        fl_account_background = findViewById(R.id.fl_account_background)
    }

    private fun setListener() {
        appbarlayout_account.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            if (isCalculated.not()) {
                startAvatarAnimatePointY = abs((appBarLayout.height - EXPAND_AVATAR_SIZE - toolbar_account.height / 2) / appBarLayout.totalScrollRange)
                animateWeigt = 1 / (1 - startAvatarAnimatePointY)
                isCalculated = true
            }

            val offset = abs(i / appBarLayout.totalScrollRange.toFloat())
            updateToolbarViewImage(offset)
        })

        b_account_save.setOnClickListener { finish() }
    }

    private fun updateToolbarViewImage(percentOffset: Float) {
        /* Collapsing avatar transparent*/
        when {
            percentOffset > mUpperLimitTransparently -> {
                //avatarContainerView.alpha = 0.0f
                tv_account_user_name_long.alpha = 0.0F
            }

            percentOffset < mUpperLimitTransparently -> {
                //  avatarContainerView.alpha = 1 - percentOffset
                tv_account_user_name_long.alpha = 1f
            }
        }

        /*Collapsed/expended sizes for views*/
        val result: Pair<Int, Int> = when {
            percentOffset < ABROAD -> {
                Pair(
                    TO_EXPANDED_STATE, cashCollapseState?.second
                        ?: WAIT_FOR_SWITCH
                )
            }
            else -> {
                Pair(TO_COLLAPSED_STATE, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            }
        }
        result.apply {
            var translationY = 0f
            var headContainerHeight = 0f
            val translationX: Float
            var currentImageSize = 0
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED_STATE -> {
                            translationY = toolbar_account.height.toFloat()
                            headContainerHeight = appbarlayout_account.totalScrollRange.toFloat()
                            currentImageSize = EXPAND_AVATAR_SIZE.toInt()
                            /**/
                            tv_account_user_name_long.visibility = View.VISIBLE
                            tv_account_user_name_short.visibility = View.INVISIBLE
                            fl_account_background.setBackgroundColor(ContextCompat.getColor(this@AccountActivity, R.color.color_transparent))
                            /**/
                            iv_account_user_image.translationX = 0f
                        }

                        TO_COLLAPSED_STATE -> {
                            fl_account_background.setBackgroundColor(ContextCompat.getColor(this@AccountActivity, R.color.colorPrimary))
                            currentImageSize = COLLAPSE_IMAGE_SIZE.toInt()
                            translationY = appbarlayout_account.totalScrollRange.toFloat() - (toolbar_account.height - COLLAPSE_IMAGE_SIZE) / 2
                            headContainerHeight = toolbar_account.height.toFloat()
                            translationX = appbarlayout_account.width / 2f - COLLAPSE_IMAGE_SIZE / 2 - margin * 2
                            /**/
                            ValueAnimator.ofFloat(iv_account_user_image.translationX, translationX).apply {
                                addUpdateListener {
                                    if (cashCollapseState!!.first == TO_COLLAPSED_STATE) {
                                        iv_account_user_image.translationX = it.animatedValue as Float
                                    }
                                }
                                interpolator = AnticipateOvershootInterpolator()
                                startDelay = 69
                                duration = 350
                                start()
                            }
                            /**/
                            tv_account_user_name_long.visibility = View.INVISIBLE
                            tv_account_user_name_short.apply {
                                visibility = View.VISIBLE
                                alpha = 0.2f
                                this.translationX = width.toFloat() / 2
                                animate().translationX(0f)
                                    .setInterpolator(AnticipateOvershootInterpolator())
                                    .alpha(1.0f)
                                    .setStartDelay(69)
                                    .setDuration(450)
                                    .setListener(null)
                            }
                        }
                    }

                    iv_account_user_image.apply {
                        layoutParams.height = currentImageSize
                        layoutParams.width = currentImageSize
                    }
                    fl_account_stuff_container.apply {
                        layoutParams.height = headContainerHeight.toInt()
                        this.translationY = translationY
                        requestLayout()
                    }
                    /**/
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> {
                    /* Collapse avatar img*/
                    iv_account_user_image.apply {
                        if (percentOffset > startAvatarAnimatePointY) {

                            val animateOffset = (percentOffset - startAvatarAnimatePointY) * animateWeigt
                            Log.d("Lihat", "updateToolbarViewImage AccountActivity : " + animateOffset)
                            val avatarSize = EXPAND_AVATAR_SIZE - (EXPAND_AVATAR_SIZE - COLLAPSE_IMAGE_SIZE) * animateOffset

                            this.layoutParams.also {
                                if (it.height != Math.round(avatarSize)) {
                                    it.height = Math.round(avatarSize)
                                    it.width = Math.round(avatarSize)
                                    this.requestLayout()
                                }
                            }

                            this.translationY = (COLLAPSE_IMAGE_SIZE - avatarSize) * animateOffset

                        } else {
                            this.layoutParams.also {
                                if (it.height != EXPAND_AVATAR_SIZE.toInt()) {
                                    it.height = EXPAND_AVATAR_SIZE.toInt()
                                    it.width = EXPAND_AVATAR_SIZE.toInt()
                                    this.layoutParams = it
                                }
                            }
                        }
                    }
                    cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
                }
            }


        }
    }

    companion object {
        const val ABROAD = 0.99f
        const val TO_EXPANDED_STATE = 0
        const val TO_COLLAPSED_STATE = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }

    private val mLowerLimitTransparently = ABROAD * 0.45
    private val mUpperLimitTransparently = ABROAD * 0.65

}
