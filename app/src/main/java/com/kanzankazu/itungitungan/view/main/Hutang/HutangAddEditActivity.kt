package com.kanzankazu.itungitungan.view.main.Hutang

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.DialogUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseConnectionUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import com.kanzankazu.itungitungan.util.widget.view.CurrencyEditText
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import com.kanzankazu.itungitungan.view.adapter.UserSuggestAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add_edit.*
import kotlinx.android.synthetic.main.app_toolbar.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class HutangAddEditActivity : BaseActivity(), HutangAddEditContract.View {
    private var hutang: Hutang = Hutang()
    private var isEdit: Boolean = false
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
    private lateinit var imageListAdapter: ImageListAdapter
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
            imageListAdapter.addDataFirst(ImageModel(mCurrentPhotoPath, ""))
            checkImageData()
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val emailAccountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            mPresenter.getSuggestUserValidate(emailAccountResult, et_hutang_add_user)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickPhoneAccountResult(requestCode, resultCode, data, this, true)
            mPresenter.getSuggestUserValidate(phoneAccountResult, et_hutang_add_user)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtil.onRequestPermissionsResult(
                false,
                object : AndroidPermissionUtil.AndroidPermissionUtilListener {
                    override fun onPermissionGranted() {
                        if (permissions.contentEquals(AndroidPermissionUtil.permCameraGallery)) {
                            pictureUtil2.chooseGetImageDialog()
                        }
                    }

                    override fun onPermissionDenied(message: String?) {
                        showSnackbar(message)
                    }
                },
                requestCode, permissions, grantResults
        )
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

    override fun validateFieldData(isFocus: Boolean): Boolean {
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, et_hutang_add_user, isFocus)) return false
        else if (!InputValidUtil.isLenghtCharOver(getString(R.string.message_field_less_nominal, CurrencyEditText.lenghtNominalDigits), til_hutang_add_nominal, et_hutang_add_nominal, CurrencyEditText.validationLimit)) return false
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

    override fun setCheckSuggestUsers(dataSnapshot: Any?) {
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

    override fun setSuggestUserFamily(dataSnapshot: DataSnapshot?) {
        if (dataSnapshot != null) {
            val userSuggest = mutableListOf<User>()

            for (snapshot in dataSnapshot.children) {
                val user = snapshot.getValue(User::class.java)
                userSuggest.add(user!!)
            }

            if (userSuggest.isNotEmpty()) {
                setListSuggestFamilyAdapter(userSuggest)
            } else {
                til_hutang_add_user_family.visibility = View.GONE
            }
        } else {
            Log.d("Lihat", "onSuccessString HutangAddEditActivity : " + "datasnapshot kosong")
        }
    }

    override fun setHutangData(dataSnapshot: DataSnapshot?) {
        hutang = dataSnapshot?.getValue(Hutang::class.java)!!
        setPreBundleData()
    }

    private fun setView() {
        pictureUtil2 = PictureUtil2(this)
        permissionUtil = AndroidPermissionUtil(this)

        mPresenter.getSuggestUserFamily()
        setListTypeInstallmentCount(sp_hutang_add_installment_count)
        setListImage(rv_hutang_add_image)

        getBundle()
    }

    private fun setListener() {
        watcherValidate = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateFieldData(false)
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
                    mPresenter.getSuggestUserValidate(s, et_hutang_add_user)
                }
            }
            false
        }
        et_hutang_add_nominal.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_NEXT) {
                validateFieldData(false)
                calculateNominalCount()
            }
            false
        }
        et_hutang_add_nominal.setOnFocusChangeListener { _, b ->
            if (!b) {
                validateFieldData(false)
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
                    validateFieldData(true)
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
        iv_hutang_add_user.setOnClickListener { dialogPickUserData() }
        tv_hutang_add_simpan.setOnClickListener {
            calculateNominalCount()
            dialogSaveConfirm()
        }

        iv_hutang_add_image_add.setOnClickListener { dialogPickImage() }

        sw_hutang_add_editable.setOnCheckedChangeListener { _, b ->
            if (!b) {
                DialogUtil.showConfirmationDialog(
                        this,
                        "Konfirmasi",
                        "Dengan mengubah opsi ini, penghutang dan piutang tidak bisa mengubah data hutangList ini."
                ) {}
            }
        }
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            isEdit = true
            if (bundle.containsKey(Constants.Bundle.HUTANG_LIST)) {
                hutang = bundle.getParcelable(Constants.Bundle.HUTANG_LIST) as Hutang
                setPreBundleData()
            } else if (bundle.containsKey(Constants.Bundle.HUTANG_ID)) {
                val hutangId = bundle.getString(Constants.Bundle.HUTANG_ID, "")

                if (hutangId.isNotEmpty()) {
                    mPresenter.getHutangByHid(hutangId)
                } else {
                    showToast(getString(R.string.message_field_empty))
                    finish()
                }
            }
            //isFromValuationActivity = bundle.getBoolean(Constants.ScreenFlag.IS_FROM_VALUATION_ACTIVITY)
        }

        if (isEdit) {
            setBundleData()
            setFamilyViewOnly(!isIFamily)

            Utils.setupAppToolbarForActivity(this, toolbar, "Ubah hutangList Piutang")
        } else {
            Utils.setupAppToolbarForActivity(this, toolbar, "Tambah hutangList Piutang")
        }
    }

    private fun setPreBundleData() {
        hutang.backupBeforeChange = hutang
        isIInclude = if (hutang.debtorCreditorId.isNotEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
        isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
        isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
        isIFamily = if (hutang.creditorFamilyId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorFamilyId, true) else false
        isIFamily = if (hutang.debtorFamilyId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorFamilyId, true) else false
        isDataPenghutang = hutang.hutangRadioIndex == 0
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

        if (hutang.hutangProofImage.isNotEmpty()) {
            imageListAdapter.addDatasString(hutang.hutangProofImage, "")
            checkImageData()
        }
    }

    private fun setFamilyViewOnly(iFamily: Boolean) {
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
        rv_hutang_add_image.isClickable = iFamily
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
        rv_hutang_add_image.isEnabled = iFamily
        tv_hutang_add_simpan.isEnabled = iFamily
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

    private fun setListImage(recyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        imageListAdapter = ImageListAdapter(this, object : ImageListAdapter.ImageListContract {
            override fun onImageListView(data: ImageModel, position: Int) {}

            override fun onImageListRemove(data: ImageModel, position: Int) {
                dialogClearImage(data, position)
            }

            override fun onImageListAdd(data: ImageModel, position: Int) {}
        })
        recyclerView.adapter = imageListAdapter
    }

    private fun checkImageData() {
        if (imageListAdapter.getDatas().isNotEmpty()) {
            rv_hutang_add_image.visibility = View.VISIBLE
        } else {
            rv_hutang_add_image.visibility = View.GONE
        }
    }

    private fun setListSuggestFamilyAdapter(userSuggest: MutableList<User>) {
        userSuggestAdapter = UserSuggestAdapter(this@HutangAddEditActivity, R.layout.activity_hutang_add_edit, R.id.et_hutang_add_desc, userSuggest)
        et_hutang_add_user_family.setAdapter(userSuggestAdapter)
        et_hutang_add_user_family.threshold = 3
        et_hutang_add_user_family.setOnItemClickListener { parent, view, position, id ->
            hideKeyboard()

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

    private fun dialogPickImage() {
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            pictureUtil2.chooseGetImageDialog()
        }
    }

    private fun dialogClearImage(data: ImageModel, position: Int) {
        DialogUtil.showIntroductionDialog(
                this,
                "",
                "Konfirmasi",
                "Apa anda yakin gambar ini akan di hapus? ",
                "Iya",
                "Tidak",
                false,
                -1,
                object : DialogUtil.IntroductionButtonListener {
                    override fun onFirstButtonClick() {
                        if (InputValidUtil.isLinkUrl(data.path)) {
                            FirebaseConnectionUtil.isConnect(this@HutangAddEditActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
                                override fun hasInternet() {
                                    FirebaseStorageUtil.deleteImages(this@HutangAddEditActivity, hutang.hutangProofImage,
                                            object : FirebaseStorageUtil.DoneRemoveListenerMultiple {
                                                override fun isFinised() {
                                                    imageListAdapter.removeAt(position)
                                                    checkImageData()
                                                }

                                                override fun isFailed(message: String) {
                                                    showSnackbar(message)
                                                }
                                            })
                                }

                                override fun noInternet(message: String?) {
                                    showRetryDialog { }
                                }
                            })
                        } else {
                            imageListAdapter.removeAt(position)
                            checkImageData()
                        }
                    }

                    override fun onSecondButtonClick() {}
                }
        )
    }

    private fun dialogSaveConfirm() {
        DialogUtil.showYesNoDialog(this, "Konfirmasi", "Apakah anda yakin ingin lanjut disimpan?", object : DialogUtil.IntroductionButtonListener {
            override fun onFirstButtonClick() {
                saveHutangValidate()
            }

            override fun onSecondButtonClick() {}
        })
    }

    private fun dialogPickUserData() {
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
        if (validateFieldData(true)) {
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

            if (imageListAdapter.getDatasUri().isNotEmpty()) {
                mPresenter.saveImageHutang(hutang, isEdit, imageListAdapter.getDatasUri())
            } else {
                mPresenter.saveEditHutang(hutang, isEdit, true)
            }
        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }

}

