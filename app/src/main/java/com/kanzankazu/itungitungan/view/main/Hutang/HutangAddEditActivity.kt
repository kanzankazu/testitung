package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.Firebase.MyFirebaseMessagingUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.util.widget.view.CurrencyEditText
import com.kanzankazu.itungitungan.view.adapter.UserSuggestAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add_edit.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HutangAddEditActivity : BaseActivity(), HutangAddEditContract.View {
    private var hutang: Hutang = Hutang()
    private var isEdit: Boolean = false
    private var positionImage: Int = -1
    private var mCurrentPhotoPath0: String? = ""
    private var mCurrentPhotoPath1: String? = ""
    private var mCurrentPhotoPath0Uri: Uri? = null
    private var mCurrentPhotoPath1Uri: Uri? = null
    private val userSuggest = ArrayList<User>()
    private var listTypeInstallmentCount = ArrayList<String>()
    private var listTypeInstallmentCountPos: Int = 0
    private var userInvite: User = User()
    private var isIInclude: Boolean = false
    private var isIPenghutang: Boolean = false
    private var isIPiutang: Boolean = false
    private var isIFamily: Boolean = false
    private var isDataPenghutang: Boolean = false
    private lateinit var mPresenter: HutangAddEditPresenter
    private lateinit var userSuggestAdapter: UserSuggestAdapter
    private lateinit var pictureUtil2: PictureUtil2
    private lateinit var permissionUtil: AndroidPermissionUtil
    private lateinit var watcherValidate: TextWatcher
    private lateinit var watcherValidateInstallmentCount: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_add_edit)

        mPresenter = HutangAddEditPresenter(this, this)
        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pictureUtil2.REQUEST_IMAGE_CAMERA || requestCode == pictureUtil2.REQUEST_IMAGE_GALLERY) {
            val mCurrentPhotoPath = pictureUtil2.onActivityResult(requestCode, resultCode, data)
            if (positionImage == 0) {
                mCurrentPhotoPath0 = mCurrentPhotoPath
                mCurrentPhotoPath0Uri = Uri.fromFile(File(mCurrentPhotoPath0))
                civ_hutang_remove_image_0.visibility = View.VISIBLE
            } else {
                mCurrentPhotoPath1 = mCurrentPhotoPath
                mCurrentPhotoPath1Uri = Uri.fromFile(File(mCurrentPhotoPath1))
                civ_hutang_remove_image_1.visibility = View.VISIBLE
            }
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val emailAccountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            setSuggestUser(emailAccountResult)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickPhoneAccountResult(requestCode, resultCode, data, this, true)
            setSuggestUser(phoneAccountResult)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionUtil.RP_ACCESS) {
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, false)
        }
    }

    override fun onBackPressed() {
        DialogUtil.showYesNoDialog(this, "Konfirmasi", "Apakah anda tidak ingin melanjutkan ini?", object : DialogUtil.IntroductionButtonListener {
            override fun onFirstButtonClick() {
                finish()
            }

            override fun onSecondButtonClick() {
            }
        })
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

    override fun checkData(isFocus: Boolean): Boolean {
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, et_hutang_add_user, isFocus)) return false
        else if (!InputValidUtil.isLenghtCharOver(getString(R.string.message_field_less_nominal, "3"), til_hutang_add_nominal, et_hutang_add_nominal, CurrencyEditText.validationLimit)) return false
        else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_desc, et_hutang_add_desc, isFocus)) return false
        else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_date, et_hutang_add_date, isFocus)) return false
        else if (sw_hutang_add_installment.isChecked) {
            if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_installment_count, et_hutang_add_installment_count, isFocus)) return false
            else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_installment_nominal, et_hutang_add_installment_nominal, isFocus)) return false
            else if (cb_hutang_add_installment_free_to_pay.isChecked) {
                return true
            } else {
                if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_installment_due_date, et_hutang_add_installment_due_date, isFocus)) return false
            }
        } else {
            return true
        }
        return true
    }

    private fun setView() {
        permissionUtil = AndroidPermissionUtil(this, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil2 = PictureUtil2(this)

        setSuggestUserFamily(et_hutang_add_user_family)
        setListTypeInstallmentCount(sp_hutang_add_installment_count)

        getBundle()
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            isEdit = true
            if (bundle.containsKey(Constants.Bundle.HUTANG)) {
                hutang = bundle.getParcelable(Constants.Bundle.HUTANG) as Hutang
                hutang.backupBeforeChange = hutang
                mPresenter.saveEditHutang(hutang, isEdit, false)
                isIInclude = if (hutang.debtorCreditorId.isNotEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
                isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
                isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
                isIFamily = if (hutang.creditorFamilyId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorFamilyId, true) else false
                isIFamily = if (hutang.debtorFamilyId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorFamilyId, true) else false
                isDataPenghutang = hutang.hutangRadioIndex == 0
            }
            //isFromValuationActivity = bundle.getBoolean(Constants.ScreenFlag.IS_FROM_VALUATION_ACTIVITY)
        }

        if (isEdit) {
            setBundleData()
            setFamilyData(!isIFamily)

            Utils.setupAppToolbarForActivity(this, toolbar, "Ubah HUTANG Piutang")
        } else {
            Utils.setupAppToolbarForActivity(this, toolbar, "Tambah HUTANG Piutang")
        }

    }

    private fun setBundleData() {
        if (isIPenghutang) {
            if (hutang.creditorId.isEmpty() || hutang.creditorId.equals(UserPreference.getInstance().uid, true)) {
                et_hutang_add_user.setText(hutang.creditorName)
                userInvite.name = hutang.creditorName
                userInvite.uId = ""
                userInvite.email = ""
            } else {
                val user = User().apply {
                    uId = hutang.creditorId
                    name = hutang.creditorName
                    email = hutang.creditorEmail
                }
                setCheckSuggestUsers(user)
            }

            if (hutang.debtorFamilyId.isNotEmpty()) {
                iv_hutang_add_family_clear.visibility = View.VISIBLE
                et_hutang_add_user_family.setText(hutang.debtorFamilyName)
            } else {
                iv_hutang_add_family_clear.visibility = View.GONE
            }
        } else {
            if (hutang.debtorId.isEmpty() || hutang.debtorId.equals(UserPreference.getInstance().uid, true)) {
                et_hutang_add_user.setText(hutang.debtorName)
                userInvite.name = hutang.debtorName
                userInvite.uId = ""
                userInvite.email = ""
            } else {
                val user = User()
                user.uId = hutang.debtorId
                user.name = hutang.debtorName
                user.email = hutang.debtorEmail
                setCheckSuggestUsers(user)
            }

            if (hutang.creditorFamilyId.isNotEmpty()) {
                iv_hutang_add_family_clear.visibility = View.VISIBLE
                et_hutang_add_user_family.setText(hutang.creditorFamilyName)
            } else {
                iv_hutang_add_family_clear.visibility = View.GONE
            }

        }

        et_hutang_add_nominal.setText(Utils.setRupiah(hutang.hutangNominal))
        et_hutang_add_desc.setText(hutang.hutangKeperluan)
        et_hutang_add_note.setText(hutang.hutangCatatan)
        et_hutang_add_date.setText(hutang.hutangPinjam)

        sw_hutang_add_editable.isChecked = hutang.hutangEditableis

        Handler().postDelayed({
            mPresenter.setRadioGroupIndex(rg_hutang_add_user, hutang.hutangRadioIndex).isChecked = true
            sw_hutang_add_installment.isChecked = hutang.hutangCicilanIs
            if (hutang.hutangCicilanIs) {
                Handler().postDelayed({
                    et_hutang_add_installment_count.setText(hutang.hutangCicilanBerapaKali)
                }, 500)
                sp_hutang_add_installment_count.setSelection(hutang.hutangCicilanBerapaKaliPosisi)
                et_hutang_add_installment_nominal.setText(Utils.setRupiah(hutang.hutangCicilanNominal))
                cb_hutang_add_installment_free_to_pay.isChecked = hutang.hutangCicilanIsBayarKapanSaja
                if (!hutang.hutangCicilanIsBayarKapanSaja) {
                    et_hutang_add_installment_due_date.setText(hutang.hutangCicilanTanggalAkhir)
                }
            }
        }, 500)

        if (hutang.hutangBuktiGambar.isNotEmpty()) {
            if (hutang.hutangBuktiGambar.size == 1) {
                Glide.with(this).load(hutang.hutangBuktiGambar[0]).into(civ_hutang_add_image_0)
                civ_hutang_remove_image_0.visibility = View.VISIBLE
            } else if (hutang.hutangBuktiGambar.size == 2) {
                Glide.with(this).load(hutang.hutangBuktiGambar[0]).into(civ_hutang_add_image_0)
                civ_hutang_remove_image_0.visibility = View.VISIBLE
                Glide.with(this).load(hutang.hutangBuktiGambar[1]).into(civ_hutang_add_image_1)
                civ_hutang_remove_image_1.visibility = View.VISIBLE
            }
        }
    }

    private fun setFamilyData(iFamily: Boolean) {
        rg_hutang_add_user.isClickable = iFamily
        rb_hutang_add_hutang.isClickable = iFamily
        rb_hutang_add_piutang.isClickable = iFamily
        et_hutang_add_user.isClickable = iFamily
        iv_hutang_add_user_clear.isClickable = iFamily
        iv_hutang_add_user.isClickable = iFamily
        et_hutang_add_user_family.isClickable = iFamily
        iv_hutang_add_family_clear.isClickable = iFamily
        et_hutang_add_nominal.isClickable = iFamily
        et_hutang_add_desc.isClickable = iFamily
        et_hutang_add_note.isClickable = iFamily
        et_hutang_add_date.isClickable = iFamily
        sw_hutang_add_editable.isClickable = iFamily
        sw_hutang_add_installment.isClickable = iFamily
        ll_hutang_add_installment.isClickable = iFamily
        et_hutang_add_installment_nominal.isClickable = iFamily
        et_hutang_add_installment_count.isClickable = iFamily
        sp_hutang_add_installment_count.isClickable = iFamily
        cb_hutang_add_installment_free_to_pay.isClickable = iFamily
        et_hutang_add_installment_due_date.isClickable = iFamily
        civ_hutang_add_image_0.isClickable = iFamily
        civ_hutang_remove_image_0.isClickable = iFamily
        civ_hutang_add_image_1.isClickable = iFamily
        civ_hutang_remove_image_1.isClickable = iFamily
        tv_hutang_add_simpan.isClickable = iFamily

        rg_hutang_add_user.isEnabled = iFamily
        rb_hutang_add_hutang.isEnabled = iFamily
        rb_hutang_add_piutang.isEnabled = iFamily
        et_hutang_add_user.isEnabled = iFamily
        iv_hutang_add_user_clear.isEnabled = iFamily
        iv_hutang_add_user.isEnabled = iFamily
        et_hutang_add_user_family.isEnabled = iFamily
        iv_hutang_add_family_clear.isEnabled = iFamily
        et_hutang_add_nominal.isEnabled = iFamily
        et_hutang_add_desc.isEnabled = iFamily
        et_hutang_add_note.isEnabled = iFamily
        et_hutang_add_date.isEnabled = iFamily
        sw_hutang_add_editable.isEnabled = iFamily
        sw_hutang_add_installment.isEnabled = iFamily
        ll_hutang_add_installment.isEnabled = iFamily
        et_hutang_add_installment_nominal.isEnabled = iFamily
        et_hutang_add_installment_count.isEnabled = iFamily
        sp_hutang_add_installment_count.isEnabled = iFamily
        cb_hutang_add_installment_free_to_pay.isEnabled = iFamily
        et_hutang_add_installment_due_date.isEnabled = iFamily
        civ_hutang_add_image_0.isEnabled = iFamily
        civ_hutang_remove_image_0.isEnabled = iFamily
        civ_hutang_add_image_1.isEnabled = iFamily
        civ_hutang_remove_image_1.isEnabled = iFamily
        tv_hutang_add_simpan.isEnabled = iFamily
    }

    private fun setListener() {
        watcherValidate = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkData(false)
            }
        }

        watcherValidateInstallmentCount = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                calculateNominalCount()
            }
        }

        et_hutang_add_user.addTextChangedListener(watcherValidate)
        et_hutang_add_desc.addTextChangedListener(watcherValidate)
        et_hutang_add_date.addTextChangedListener(watcherValidate)
        et_hutang_add_installment_due_date.addTextChangedListener(watcherValidate)

        et_hutang_add_user.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                val s = et_hutang_add_user.text.toString().trim()
                if (InputValidUtil.isEmailOrPhone(getString(R.string.message_field_email_phone_wrong_format), til_hutang_add_user, et_hutang_add_user)) {
                    setSuggestUser(s)
                }
            }
            false
        }
        et_hutang_add_nominal.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_NEXT) {
                checkData(false)
                calculateNominalCount()
            }
            false
        }
        et_hutang_add_nominal.setOnFocusChangeListener { _, b ->
            if (!b) {
                checkData(false)
                calculateNominalCount()
            }
        }
        et_hutang_add_installment_count.addTextChangedListener(watcherValidateInstallmentCount)

        rg_hutang_add_user.setOnCheckedChangeListener { radioGroup, _ ->
            val radioButtonID = radioGroup.checkedRadioButtonId
            val view = radioGroup.findViewById<View>(radioButtonID)
            val index = radioGroup.indexOfChild(view)
            Log.d("Lihat", "setListener HutangAddEditActivity : $index")

            if (index == 0) {
                til_hutang_add_user.hint = getString(R.string.hutang_invite_piutang)
            } else {
                til_hutang_add_user.hint = getString(R.string.hutang_invite_penghutang)
            }
        }
        sw_hutang_add_installment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (InputValidUtil.isLenghtCharOver(et_hutang_add_nominal, CurrencyEditText.validationLimit)) {
                    ll_hutang_add_installment.visibility = View.VISIBLE
                } else {
                    sw_hutang_add_installment.isChecked = false
                    checkData(true)
                    showSnackbar(getString(R.string.message_field_empty_nominal))
                }
            } else {
                et_hutang_add_installment_nominal.setText("")
                et_hutang_add_installment_count.setText("")
                ll_hutang_add_installment.visibility = View.GONE
                cb_hutang_add_installment_free_to_pay.isChecked = true
                listTypeInstallmentCountPos = 0
            }
        }
        cb_hutang_add_installment_free_to_pay.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                til_hutang_add_installment_due_date.visibility = View.GONE
            } else {
                til_hutang_add_installment_due_date.visibility = View.VISIBLE
            }
        }

        et_hutang_add_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_date) }
        et_hutang_add_installment_due_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_installment_due_date) }

        iv_hutang_add_user_clear.setOnClickListener {
            et_hutang_add_user.setText("")
            iv_hutang_add_user_clear.visibility = View.GONE
            userInvite = User()

            if (isEdit && mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 0) {
                hutang.creditorName = ""
                hutang.creditorId = ""
                hutang.creditorEmail = ""
            } else {
                hutang.debtorName = ""
                hutang.debtorId = ""
                hutang.debtorEmail = ""
            }
        }
        iv_hutang_add_family_clear.setOnClickListener {
            et_hutang_add_user_family.setText("")
            iv_hutang_add_family_clear.visibility = View.GONE

            if (isEdit && hutang.debtorFamilyId.isNotEmpty() && mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 0) {
                hutang.debtorFamilyId = ""
                hutang.debtorFamilyName = ""
            } else if (isEdit && hutang.creditorFamilyId.isNotEmpty() && mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 1) {
                hutang.creditorFamilyId = ""
                hutang.creditorFamilyName = ""
            }
        }
        iv_hutang_add_user.setOnClickListener { chooseDialogPickUserData() }
        tv_hutang_add_simpan.setOnClickListener {
            calculateNominalCount()
            dialogConfirmSave()
        }

        civ_hutang_add_image_0.setOnClickListener { chooseDialogPickImage(this, civ_hutang_add_image_0, 0) }
        civ_hutang_remove_image_0.setOnClickListener {
            removeImage(0, civ_hutang_add_image_0)
        }
        civ_hutang_add_image_1.setOnClickListener { chooseDialogPickImage(this, civ_hutang_add_image_1, 1) }
        civ_hutang_remove_image_1.setOnClickListener {
            removeImage(1, civ_hutang_add_image_1)
        }

        sw_hutang_add_editable.setOnCheckedChangeListener { _, b ->
            if (!b) {
                DialogUtil.showConfirmationDialog(
                    this,
                    "Konfirmasi",
                    "Dengan mengubah opsi ini, penghutang dan piutang tidak bisa mengubah data hutang ini."
                ) {}
            }
        }
    }

    private fun calculateNominalCount() {
        if (InputValidUtil.isLenghtCharOver(et_hutang_add_nominal, CurrencyEditText.validationLimit) && sw_hutang_add_installment.isChecked) {
            val nominalPembayaran: Int = if (!et_hutang_add_nominal.text.toString().trim().equals("Rp ", true)) Utils.getRupiahToString(et_hutang_add_nominal).toInt() else "1".toInt()
            val installmentCount: Int = if (et_hutang_add_installment_count.text.toString().isNotEmpty()) et_hutang_add_installment_count.text.toString().trim().toInt() else "1".toInt()
            if (installmentCount < 1) {
                til_hutang_add_installment_nominal.visibility = View.GONE
            } else {
                if (nominalPembayaran <= 1) {
                    et_hutang_add_nominal.requestFocus()
                } else {
                    val i = nominalPembayaran / installmentCount
                    et_hutang_add_installment_nominal.setText(Utils.setRupiah(i.toString()))
                }
                til_hutang_add_installment_nominal.visibility = View.VISIBLE
            }
        } else {
            sw_hutang_add_installment.isChecked = false
            showSnackbar(getString(R.string.message_field_empty_nominal))
        }
    }

    private fun removeImage(pos: Int, imageView: ImageView) {
        if (isEdit) {
            if (hutang.hutangBuktiGambar.isNotEmpty()) {
                /*if (hutang.hutangBuktiGambar[pos].isNotEmpty()) {
                    com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil.deleteImage(hutang.hutangBuktiGambar[pos]
                            , {
                        removeImagePath(pos)
                        hutang.hutangBuktiGambar.removeAt(pos)
                    }
                            , {
                        showSnackbar(it.message)
                        it.stackTrace
                    })
                } else {
                    showSnackbar("gagal menghapus")
                }*/
                dialogClearImage(this, imageView, false, "dengan anda menghapus 1 gambar anda akan menghapus semua gambar yang tersimpan, apakah anda setuju?")
            } else {
                removeImagePath(pos)
            }
        } else {
            removeImagePath(pos)
        }
    }

    private fun removeImagePath(pos: Int) {
        if (pos == 0) {
            mCurrentPhotoPath0 = ""
            mCurrentPhotoPath0Uri = null
            Glide.with(this).load(File(mCurrentPhotoPath0)).placeholder(R.drawable.ic_profile).into(civ_hutang_add_image_0)
            civ_hutang_remove_image_0.visibility = View.GONE
        } else {
            mCurrentPhotoPath1 = ""
            mCurrentPhotoPath1Uri = null
            Glide.with(this).load(File(mCurrentPhotoPath1)).placeholder(R.drawable.ic_profile).into(civ_hutang_add_image_1)
            civ_hutang_remove_image_1.visibility = View.GONE
        }
    }

    private fun setListTypeInstallmentCount(spinner: Spinner) {
        listTypeInstallmentCount = arrayListOf(Constants.Hutang.Installment.Month, Constants.Hutang.Installment.Year, Constants.Hutang.Installment.Day)
        val adapter = ArrayAdapter(this, R.layout.multiline_spinner_item, listTypeInstallmentCount)
        adapter.setDropDownViewResource(R.layout.multiline_spinner_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listTypeInstallmentCountPos = position
                when (listTypeInstallmentCount[position]) {
                    Constants.Hutang.Installment.Day -> InputValidUtil.setEditTextMaxLenght(et_hutang_add_installment_count, 3)
                    Constants.Hutang.Installment.Month -> InputValidUtil.setEditTextMaxLenght(et_hutang_add_installment_count, 2)
                    Constants.Hutang.Installment.Year -> InputValidUtil.setEditTextMaxLenght(et_hutang_add_installment_count, 4)
                }

                et_hutang_add_installment_count.setText("1")
                et_hutang_add_installment_count.clearFocus()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setSuggestUser(resultAccount: String) {
        showProgressDialog()
        if (!InputValidUtil.isEmail(resultAccount)) {
            FirebaseDatabaseHandler.getUserByPhone(this, InputValidUtil.getPhoneNumber62(resultAccount), object : FirebaseDatabaseUtil.ValueListenerObject {
                override fun onSuccessData(dataSnapshot: Any?) {
                    dismissProgressDialog()
                    setCheckSuggestUsers(dataSnapshot)
                }

                override fun onFailureData(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                    et_hutang_add_user.setText("")
                }
            })
        } else {
            FirebaseDatabaseHandler.getUserByEmail(this, resultAccount, object : FirebaseDatabaseUtil.ValueListenerObject {
                override fun onSuccessData(dataSnapshot: Any?) {
                    dismissProgressDialog()
                    setCheckSuggestUsers(dataSnapshot)
                }

                override fun onFailureData(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                    et_hutang_add_user.setText("")
                }
            })
        }
    }

    private fun setCheckSuggestUsers(dataSnapshot: Any?) {
        userInvite = dataSnapshot as User
        if (userInvite.uId.equals(UserPreference.getInstance().uid, true)) {
            showSnackbar(getString(R.string.message_its_you))
            userInvite = User()
        } else if (mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 0) {
            if (hutang.creditorFamilyId.isNotEmpty() && userInvite.uId.equals(hutang.creditorFamilyId, true)) {
                showSnackbar(getString(R.string.message_its_you_family))
                userInvite = User()
            } else if (hutang.debtorFamilyId.isNotEmpty() && userInvite.uId.equals(hutang.debtorFamilyId, true)) {
                showSnackbar(getString(R.string.message_its_you_family))
                userInvite = User()
            } else {
                iv_hutang_add_user_clear.visibility = View.VISIBLE
                et_hutang_add_user.setText(userInvite.name)
            }
        } else {
            iv_hutang_add_user_clear.visibility = View.VISIBLE
            et_hutang_add_user.setText(userInvite.name)
        }
    }

    private fun setSuggestUserFamily(completeTextView: AutoCompleteTextView) {
        FirebaseDatabaseHandler.getUsers(true, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccessData(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {

                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        userSuggest.add(user!!)
                    }

                    userSuggestAdapter = UserSuggestAdapter(this@HutangAddEditActivity, R.layout.activity_hutang_add_edit, R.id.et_hutang_add_desc, userSuggest)
                    completeTextView.setAdapter(userSuggestAdapter)
                } else {
                    Log.d("Lihat", "onSuccessString HutangAddEditActivity : " + "datasnapshot kosong")
                }
            }

            override fun onFailureData(message: String?) {
                Log.d("Lihat", "onFailureString HutangAddEditActivity : $message")
            }
        })
        completeTextView.threshold = 3
        completeTextView.setOnItemClickListener { parent, view, position, id ->
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $parent")
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $view")
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $position")
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $id")

            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : " + (parent.adapter.getItem(position) as User).name)
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : " + (parent.adapter.getItem(position) as User).uId)
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : " + userSuggest[position])

            Utils.closeSoftKeyboard(this)

            if (mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 0) {
                hutang.debtorFamilyName = (parent.adapter.getItem(position) as User).name
                hutang.debtorFamilyId = (parent.adapter.getItem(position) as User).uId

                if (hutang.debtorFamilyId.equals(UserPreference.getInstance().uid, true)) {
                    showSnackbar(getString(R.string.message_its_you))
                    hutang.debtorFamilyId = ""
                    hutang.debtorFamilyName = ""
                    et_hutang_add_user_family.setText("")
                } else if (userInvite.uId.isNotEmpty()) {
                    if (hutang.debtorFamilyId.equals(userInvite.uId, true)) {
                        showSnackbar(getString(R.string.message_its_you_invite))
                        hutang.debtorFamilyId = ""
                        hutang.debtorFamilyName = ""
                        et_hutang_add_user_family.setText("")
                    }
                } else {
                    iv_hutang_add_family_clear.visibility = View.VISIBLE
                }
            } else {
                hutang.creditorFamilyName = (parent.adapter.getItem(position) as User).name
                hutang.creditorFamilyId = (parent.adapter.getItem(position) as User).uId

                if (hutang.creditorFamilyId.equals(UserPreference.getInstance().uid, true)) {
                    showSnackbar(getString(R.string.message_its_you))
                    hutang.creditorFamilyId = ""
                    hutang.creditorFamilyName = ""
                    et_hutang_add_user_family.setText("")
                } else if (userInvite.uId.isNotEmpty()) {
                    if (hutang.creditorFamilyId.equals(userInvite.uId, true)) {
                        showSnackbar(getString(R.string.message_its_you_invite))
                        hutang.creditorFamilyId = ""
                        hutang.creditorFamilyName = ""
                        et_hutang_add_user_family.setText("")
                    }
                } else {
                    iv_hutang_add_family_clear.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun chooseDialogPickImage(activity: Activity, imageView: ImageView?, positionImage: Int) {
        this.positionImage = positionImage
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            if (isEdit && hutang.hutangBuktiGambar.isNotEmpty()) {
                dialogClearImage(activity, imageView, true, "dengan anda mengambil gambar baru anda akan menghapus semua gambar yang tersimpan, apakah anda setuju?")
            } else {
                pictureUtil2.chooseGetImageDialog(activity, imageView)
            }
        }
    }

    private fun dialogClearImage(activity: Activity, imageView: ImageView?, isChooseImage: Boolean, message: String?) {
        DialogUtil.showIntroductionDialog(this, "", "Konfirmasi", message, "Iya", "Tidak", false, -1, object : DialogUtil.IntroductionButtonListener {
            override fun onFirstButtonClick() {
                FirebaseStorageUtil.deleteImages(this@HutangAddEditActivity, hutang.hutangBuktiGambar,
                    object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
                        override fun isFinised() {
                            if (isChooseImage)
                                pictureUtil2.chooseGetImageDialog(activity, imageView)

                            removeImagePath(0)
                            removeImagePath(1)
                            hutang.hutangBuktiGambar.clear()
                        }

                        override fun isFailed(message: String) {
                            showSnackbar(message)
                        }
                    })
            }

            override fun onSecondButtonClick() {}
        })
    }

    private fun dialogConfirmSave() {
        DialogUtil.showYesNoDialog(this, "Konfirmasi", "Apakah anda yakin ingin lanjut disimpan?", object : DialogUtil.IntroductionButtonListener {
            override fun onFirstButtonClick() {
                saveHutangValidate()
            }

            override fun onSecondButtonClick() {}
        })
    }

    override fun onSuccessSaveUpdate(hutangRadioIndex: Int, inviteUid: String) {
        val title: String
        val message: String
        val type: String = Constants.FirebasePushNotif.TypeNotif.HUTANG

        if (hutangRadioIndex == 0) {
            title = "Piutang Baru"
            message = "Anda menjadi piutang baru, silahkan di buka aplikasinya"
        } else {
            title = "Piutang Baru"
            message = "Anda menjadi penghutang baru, silahkan di buka aplikasinya"
        }

        FirebaseDatabaseHandler.getUserByUid(inviteUid, object : FirebaseDatabaseUtil.ValueListenerDataTrueFalse {
            override fun onSuccessDataExist(dataSnapshot: DataSnapshot, isExsist: Boolean?) {
                val user: User? = dataSnapshot.getValue(User::class.java)
                MyFirebaseMessagingUtil.makeNotificationToken(this@HutangAddEditActivity, user!!.tokenFcm, title, message, type)
            }

            override fun onFailureDataExist(message: String?) {
                showSnackbar(message)
            }
        })
    }

    private fun chooseDialogPickUserData() {
        val items = arrayOf("Pilih Kontak", "Pilih Email")

        val chooseImageDialog = AlertDialog.Builder(this)
        chooseImageDialog.setItems(items) { _: DialogInterface?, position: Int ->
            if (items[position] == "Pilih Kontak") {
                AndroidUtil.pickPhoneAccount(this)
            } else {
                AndroidUtil.pickEmailAccount(this)
            }
        }
        chooseImageDialog.show()
    }

    private fun saveHutangValidate() {
        if (checkData(true)) {
            if (mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 0) {
                hutang.hutangRadioIndex = 0
                hutang.debtorId = UserPreference.getInstance().uid
                hutang.debtorName = UserPreference.getInstance().name
                hutang.debtorEmail = UserPreference.getInstance().email
                hutang.debtorApprovalNew = true
                hutang.debtorApprovalEdit = true
                hutang.debtorApprovalDelete = true

                hutang.creditorId = if (userInvite.uId.isNotEmpty()) userInvite.uId else ""
                hutang.creditorEmail = if (userInvite.email.isNotEmpty()) userInvite.email else ""
                hutang.creditorName = if (userInvite.name.equals(et_hutang_add_user.text.toString().trim(), true)) userInvite.name else et_hutang_add_user.text.toString().trim()
                hutang.creditorApprovalNew = if (isEdit && hutang.creditorId.isNotEmpty()) hutang.creditorApprovalNew else hutang.creditorId.isEmpty()
                hutang.creditorApprovalEdit = if (isEdit) hutang.creditorId.isEmpty() else !isEdit
                hutang.creditorApprovalDelete = true
            } else {
                hutang.hutangRadioIndex = 1
                hutang.creditorId = UserPreference.getInstance().uid
                hutang.creditorName = UserPreference.getInstance().name
                hutang.creditorEmail = UserPreference.getInstance().email
                hutang.creditorApprovalNew = true
                hutang.creditorApprovalEdit = true
                hutang.creditorApprovalDelete = true

                hutang.debtorId = if (userInvite.uId.isNotEmpty()) userInvite.uId else ""
                hutang.debtorEmail = if (userInvite.email.isNotEmpty()) userInvite.email else ""
                hutang.debtorName = if (userInvite.name.equals(et_hutang_add_user.text.toString().trim(), true)) userInvite.name else et_hutang_add_user.text.toString().trim()
                hutang.debtorApprovalNew = if (isEdit && hutang.debtorId.isNotEmpty()) hutang.debtorApprovalNew else hutang.debtorId.isEmpty()
                hutang.debtorApprovalEdit = if (isEdit) hutang.debtorId.isEmpty() else !isEdit
                hutang.debtorApprovalDelete = true
            }

            hutang.creditorFamilyId = hutang.creditorFamilyId
            hutang.creditorFamilyName = hutang.creditorFamilyName
            hutang.debtorFamilyId = hutang.debtorFamilyId
            hutang.debtorFamilyName = hutang.debtorFamilyName

            hutang.debtorCreditorId = hutang.debtorId + "_" + hutang.creditorId

            val rupiahToString = Utils.getRupiahToString(et_hutang_add_nominal.text.toString().trim())
            hutang.hutangNominal = if (rupiahToString.equals("Rp", true)) "1" else rupiahToString
            hutang.hutangKeperluan = et_hutang_add_desc.text.toString().trim()
            hutang.hutangCatatan = et_hutang_add_note.text.toString().trim()
            hutang.hutangPinjam = et_hutang_add_date.text.toString().trim()

            hutang.hutangEditableis = sw_hutang_add_editable.isChecked
            hutang.hutangCicilanIs = sw_hutang_add_installment.isChecked
            if (hutang.hutangCicilanIs) {
                hutang.hutangCicilanBerapaKali = et_hutang_add_installment_count.text.toString().trim()
                hutang.hutangCicilanBerapaKaliType = listTypeInstallmentCount[listTypeInstallmentCountPos]
                hutang.hutangCicilanBerapaKaliPosisi = listTypeInstallmentCountPos
                hutang.hutangCicilanNominal = Utils.getRupiahToString(et_hutang_add_installment_nominal.text.toString().trim())
                hutang.hutangCicilanIsBayarKapanSaja = cb_hutang_add_installment_free_to_pay.isChecked
                if (!hutang.hutangCicilanIsBayarKapanSaja) {
                    hutang.hutangCicilanTanggalAkhir = et_hutang_add_installment_due_date.text.toString().trim()
                }
            } else {
                hutang.hutangCicilanBerapaKali = ""
                hutang.hutangCicilanBerapaKaliType = ""
                hutang.hutangCicilanNominal = ""
                hutang.hutangCicilanIsBayarKapanSaja = false
                hutang.hutangCicilanTanggalAkhir = ""
            }

            if (mCurrentPhotoPath0Uri != null || mCurrentPhotoPath1Uri != null) {
                val listUri = PictureUtil2.convertArrayUriToArrayListUri(mCurrentPhotoPath0Uri, mCurrentPhotoPath1Uri)
                FirebaseStorageUtil.uploadImages(this@HutangAddEditActivity, "HUTANG", listUri, object : FirebaseStorageUtil.DoneListenerMultiple {
                    override fun isFinised(imageDownloadUrls: ArrayList<String>) {
                        if (isEdit && hutang.hutangBuktiGambar.isNotEmpty()) {
                            if (hutang.hutangBuktiGambar.size > 0) {
                                FirebaseStorageUtil.deleteImages(this@HutangAddEditActivity, hutang.hutangBuktiGambar,
                                    object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
                                        override fun isFinised() {
                                            hutang.hutangBuktiGambar = imageDownloadUrls
                                            mPresenter.saveEditHutang(hutang, isEdit, true)
                                        }

                                        override fun isFailed(message: String) {
                                            showSnackbar(message)
                                        }
                                    })
                            } else {
                                hutang.hutangBuktiGambar = imageDownloadUrls
                                mPresenter.saveEditHutang(hutang, isEdit, true)
                            }
                        } else {
                            hutang.hutangBuktiGambar = imageDownloadUrls
                            mPresenter.saveEditHutang(hutang, isEdit, true)
                        }
                    }

                    override fun isFailed(message: String) {
                        showSnackbar(message)
                    }
                })
            } else {
                mPresenter.saveEditHutang(hutang, isEdit, true)
            }
        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }

}

