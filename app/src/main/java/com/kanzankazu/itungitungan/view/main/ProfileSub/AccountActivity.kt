package com.kanzankazu.itungitungan.view.main.ProfileSub

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.google.GooglePhoneNumberValidation
import com.kanzankazu.itungitungan.view.base.BaseActivity
import com.kanzankazu.itungitungan.view.main.ProfileAccountModel
import com.kanzankazu.itungitungan.view.main.ProfileAccountOptionAdapter
import kotlinx.android.synthetic.main.activity_account.*
import kotlin.math.abs

class AccountActivity : BaseActivity(), AccountContract.View {

    private var EXPAND_AVATAR_SIZE: Float = 135f
    private var COLLAPSE_IMAGE_SIZE: Float = 45f
    private var margin: Float = 8f
    private var cashCollapseState: Pair<Int, Int>? = null
    private var startAvatarAnimatePointY: Float = 0F
    private var animateWeigt: Float = 0F
    private var isCalculated = false
    private val mLowerLimitTransparently = ABROAD * 0.45
    private val mUpperLimitTransparently = ABROAD * 0.65
    private var user: User = User()
    private lateinit var permissionUtil: AndroidPermissionUtil
    private lateinit var mPresenter: AccountPresenter
    private lateinit var pictureUtil: PictureUtil2
    private lateinit var optionAdapter: ProfileAccountOptionAdapter

    companion object {
        const val ABROAD = 0.99f
        const val TO_EXPANDED_STATE = 0
        const val TO_COLLAPSED_STATE = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        pictureUtil = PictureUtil2(this)
        permissionUtil = AndroidPermissionUtil(this, true, *AndroidPermissionUtil.permCameraGallery)
        mPresenter = AccountPresenter(this, this)
        Utils.setupAppToolbarForActivity(this, toolbar_account)

        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PictureUtil2.REQUEST_CODE_IMAGE_CAMERA || requestCode == PictureUtil2.REQUEST_CODE_IMAGE_GALLERY) {
            val photoPath = pictureUtil.onActivityResult(requestCode, resultCode, data)
            user.photoUrl = photoPath
            user.photoChangeAt = DateTimeUtil.currentDateTimeString!!
            setUserImage(photoPath)
        } else if (requestCode == GooglePhoneNumberValidation.REQUEST_CODE_GOOGLE_PHONE_VALIDATION) {
            val phoneNumber = GooglePhoneNumberValidation.onActivityResults(requestCode, resultCode, data)
            mPresenter.isPhoneNumberExist(phoneNumber)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(
            false,
            object : AndroidPermissionUtil.AndroidPermissionUtilListener {
                override fun onPermissionGranted() {
                    pictureUtil.chooseGetImageDialog()
                }

                override fun onPermissionDenied(message: String) {
                    showSnackbar(message)
                }
            },
            requestCode, permissions, grantResults
        )
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView() {
        showRetryDialog { }
    }

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun setDataUser(user: User) {
        this.user = user
        et_account_user_name.setText(user.name)
        et_account_user_email.setText(user.email)
        et_account_user_phone.setText(user.phone)
        setUserImage(user.photoUrl)
    }

    override fun setPhoneNumber(phoneNumber: String) {
        et_account_user_phone.setText(phoneNumber)
    }

    private fun setView() {
        setCollapseView()
        setOptionRecyclerView()

        mPresenter.getUserData()
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
        iv_account_user_image.setOnClickListener {
            if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
                pictureUtil.chooseGetImageDialog()
            }
        }
        et_account_user_phone.setOnClickListener { GooglePhoneNumberValidation.startPhoneNumberValidation(this) }

        b_account_save.setOnClickListener {
            if (checkUserData(true)) {
                saveUserData()
            }
        }
    }

    private fun saveUserData() {
        user.name = et_account_user_name.text.toString().trim()
        user.email = et_account_user_email.text.toString().trim()
        user.phone = et_account_user_phone.text.toString().trim()
    }

    private fun checkUserData(isFocus: Boolean): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_account_user_name, et_account_user_name, isFocus)) {
            false
        } else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_account_user_email, et_account_user_name, isFocus)) {
            false
        } else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_account_user_phone, et_account_user_phone, isFocus)) {
            false
        } else if (!InputValidUtil.isEmail(getString(R.string.message_field_email_wrong_format), til_account_user_email, et_account_user_name, isFocus)) {
            false
        } else InputValidUtil.isPhoneNumber(getString(R.string.message_field_empty), til_account_user_phone, et_account_user_phone, isFocus)
    }

    private fun setCollapseView() {
        toolbar_account.visibility = View.INVISIBLE
    }

    private fun setOptionRecyclerView() {
        val listOfProfileAccount = mutableListOf<ProfileAccountModel>()
        listOfProfileAccount.add(ProfileAccountModel(0, getString(R.string.account_activity_fast_notes), "Opsi ini untuk mempermudah anda membalas cepat saat pembayaran hutang", "", true))
        listOfProfileAccount.add(ProfileAccountModel(0, getString(R.string.account_activity_payment_account), "Opsi ini untuk mempermudah peminjam melakukan pembayaran dengan via trasfer", "", true))
        listOfProfileAccount.add(ProfileAccountModel(0, getString(R.string.account_activity_category_cash_inout), "Opsi ini mengkategorikan pemasukan dan pengeluaran anda", "", true))

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_account_option.layoutManager = linearLayoutManager
        optionAdapter = ProfileAccountOptionAdapter(this, object : ProfileAccountOptionAdapter.Listener {
            override fun onItemAdapterClick(position: Int, data: ProfileAccountModel) {
                itemAdapterClick(data)
            }
        })
        rv_account_option.adapter = optionAdapter
        optionAdapter.addDatas(listOfProfileAccount)
    }

    private fun setUserImage(photoPath: String) {
        Glide.with(this)
            .load(photoPath)
            .placeholder(R.drawable.ic_profile)
            .into(iv_account_user_image)
    }

    private fun itemAdapterClick(data: ProfileAccountModel) {
        when (data.title) {
            getString(R.string.account_activity_fast_notes) -> {
                showSnackbar(getString(R.string.message_info_under_development))
            }
            getString(R.string.account_activity_payment_account) -> {
                showSnackbar(getString(R.string.message_info_under_development))
            }
            getString(R.string.account_activity_category_cash_inout) -> {
                showSnackbar(getString(R.string.message_info_under_development))
            }
        }
    }

    private fun updateToolbarViewImage(percentOffset: Float) {
        /* Collapsing avatar transparent*/
        when {
            percentOffset > mUpperLimitTransparently -> {
                //avatarContainerView.alpha = 0.0f
                tv_account_user_name_big.alpha = 0.0F
            }

            percentOffset < mUpperLimitTransparently -> {
                //  avatarContainerView.alpha = 1 - percentOffset
                tv_account_user_name_big.alpha = 1f
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
                            tv_account_user_name_big.visibility = View.VISIBLE
                            tv_account_user_name_small.visibility = View.INVISIBLE
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
                            tv_account_user_name_big.visibility = View.INVISIBLE
                            tv_account_user_name_small.apply {
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
                            Log.d("Lihat", "updateToolbarViewImage AccountActivity : $animateOffset")
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
}
