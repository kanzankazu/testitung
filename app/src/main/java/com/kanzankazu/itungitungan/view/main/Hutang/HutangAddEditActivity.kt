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
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.view.adapter.UserSuggestAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add_edit.*
import kotlinx.android.synthetic.main.app_toolbar.*
import retrofit2.Call
import java.io.File

class HutangAddEditActivity : BaseActivity(), HutangAddEditContract.View {
    private var hutang: Hutang = Hutang()
    private var isBundle: Boolean = false
    private var pickAccount: Int = -1
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
            setSuggestUser(emailAccountResult, pickAccount)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickPhoneAccountResult(requestCode, resultCode, data, this, true)
            setSuggestUser(phoneAccountResult, pickAccount)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionUtil.RP_ACCESS) {
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, false)
        }
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView(call: Call<*>?) {
        showRetryDialog {}
    }

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    private fun setView() {
        permissionUtil = AndroidPermissionUtil(this, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil2 = PictureUtil2(this)

        setSuggestUserFamily(et_hutang_add_user_family)
        setListTypeInstallmentCount(sp_hutang_add_installment_count)

        setBundle()
    }

    private fun setBundle() {
        val extras = intent.extras
        if (extras != null) {
            isBundle = true
            if (extras.containsKey(Constants.BUNDLE.Hutang)) {
                hutang = extras.getParcelable(Constants.BUNDLE.Hutang)
                isIInclude = if (!hutang.piutang_penghutang_id.isNullOrEmpty()) UserPreference.getInstance().uid.contains(hutang.piutang_penghutang_id, true) else false
                isIPenghutang = if (!hutang.penghutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.penghutangId, true) else false
                isIPiutang = if (!hutang.piutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.piutangId, true) else false
                isIFamily = if (!hutang.hutangKeluargaId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
                isDataPenghutang = hutang.hutangRadioIndex == 0
            }
            //isFromValuationActivity = extras.getBoolean(Constants.ScreenFlag.IS_FROM_VALUATION_ACTIVITY)
        }

        if (isBundle) {
            setBundleData()
            setFamilyData(!isIFamily)

            Utils.setupAppToolbarForActivity(this, toolbar, "Ubah Hutang Piutang")
        } else {
            Utils.setupAppToolbarForActivity(this, toolbar, "Tambah Hutang Piutang")
        }

    }

    private fun setBundleData() {
        if (hutang.hutangRadioIndex == 0) {
            if (!hutang.piutangId.isNullOrEmpty() && !hutang.piutangId.equals(UserPreference.getInstance().uid, true)) {
                setCheckSuggestUsers(User(hutang.piutangId, hutang.piutangNama, hutang.piutangEmail))
            } else {
                et_hutang_add_user.setText(hutang.piutangNama)
                userInvite.name = hutang.piutangNama
                userInvite.setuId("")
                userInvite.email = ""
            }
        } else {
            if (!hutang.penghutangId.isNullOrEmpty() && !hutang.penghutangId!!.equals(UserPreference.getInstance().uid, true)) {
                setCheckSuggestUsers(User(hutang.penghutangId, hutang.penghutangNama, hutang.penghutangEmail))
            } else {
                et_hutang_add_user.setText(hutang.penghutangNama)
                userInvite.name = hutang.penghutangNama
                userInvite.setuId("")
                userInvite.email = ""
            }
        }

        et_hutang_add_nominal.setText(Utils.setRupiah(hutang.hutangNominal))
        et_hutang_add_desc.setText(hutang.hutangKeperluan)
        et_hutang_add_note.setText(hutang.hutangCatatan)
        et_hutang_add_date.setText(hutang.hutangPinjam)

        if (!hutang.hutangKeluargaId.isNullOrEmpty()) {
            iv_hutang_add_family_clear.visibility = View.VISIBLE
            et_hutang_add_user_family.setText(hutang.hutangKeluargaNama)
        }

        Handler().postDelayed({
            mPresenter.setRadioGroupIndex(rg_hutang_add_user, hutang.hutangRadioIndex).isChecked = true
            if (hutang.hutangIsCicilan != null) {
                sw_hutang_add_installment.isChecked = hutang.hutangIsCicilan
                if (hutang.hutangIsCicilan) {
                    Handler().postDelayed({
                        et_hutang_add_installment_count.setText(hutang.hutangCicilanBerapaKali)
                    }, 500)
                    sp_hutang_add_installment_count.setSelection(hutang.hutangCicilanBerapaKaliPosisi)
                    et_hutang_add_installment_nominal.setText(Utils.setRupiah(hutang.hutangCicilanNominal))
                    cb_hutang_add_installment_free_to_pay.isChecked = hutang.hutangisBayarKapanSaja
                    if (!hutang.hutangisBayarKapanSaja) {
                        et_hutang_add_installment_due_date.setText(hutang.hutangCicilanTanggalAkhir)
                    }
                }
            }
        }, 500)

        if (hutang.hutangBuktiGambar != null) {
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
        et_hutang_add_nominal.addTextChangedListener(watcherValidate)
        et_hutang_add_desc.addTextChangedListener(watcherValidate)
        et_hutang_add_date.addTextChangedListener(watcherValidate)
        et_hutang_add_installment_due_date.addTextChangedListener(watcherValidate)

        et_hutang_add_installment_count.addTextChangedListener(watcherValidateInstallmentCount)
        et_hutang_add_nominal.setOnEditorActionListener { p0, p1, p2 ->
            if (p1 == EditorInfo.IME_ACTION_NEXT) {
                calculateNominalCount()
            }
            false
        }
        et_hutang_add_nominal.setOnFocusChangeListener { view, b ->
            Log.d("Lihat", "setListener HutangAddEditActivity : $b")
            Log.d("Lihat", "setListener HutangAddEditActivity : ${view.parent}")
            if (!b) {
                calculateNominalCount()
            }
        }

        rg_hutang_add_user.setOnCheckedChangeListener { radioGroup, _ ->
            val radioButtonID = radioGroup.checkedRadioButtonId
            val view = radioGroup.findViewById<View>(radioButtonID)
            val index = radioGroup.indexOfChild(view)
            Log.d("Lihat", "setListener HutangAddEditActivity : $index")
            //val radioButton = radioGroup.getChildAt(index) as RadioButton
            //val toString = radioButton.text.toString().trim()
            //Log.d("Lihat", "setListener HutangAddEditActivity : $toString")

            if (index == 0) {
                til_hutang_add_user.hint = getString(R.string.invite_piutang)
            } else {
                til_hutang_add_user.hint = getString(R.string.invite_penghutang)
            }
        }
        sw_hutang_add_installment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                ll_hutang_add_installment.visibility = View.VISIBLE
            } else {
                ll_hutang_add_installment.visibility = View.GONE
                cb_hutang_add_installment_free_to_pay.isChecked = false
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

            if (isBundle && hutang.hutangRadioIndex == 0) {
                hutang.piutangNama = null
                hutang.piutangId = null
                hutang.piutangEmail = null
            } else {
                hutang.penghutangNama = null
                hutang.penghutangId = null
                hutang.penghutangEmail = null
            }
        }
        iv_hutang_add_family_clear.setOnClickListener {
            et_hutang_add_user_family.setText("")
            iv_hutang_add_family_clear.visibility = View.GONE

            if (isBundle && !hutang.hutangKeluargaId.isNullOrEmpty()) {
                hutang.hutangKeluargaId = ""
                hutang.hutangKeluargaNama = ""
            }
        }
        iv_hutang_add_user.setOnClickListener { chooseDialogPickUserData() }
        tv_hutang_add_simpan.setOnClickListener { saveHutangValidate() }

        civ_hutang_add_image_0.setOnClickListener { chooseDialogPickImage(this, civ_hutang_add_image_0, 0) }
        civ_hutang_remove_image_0.setOnClickListener {
            removeImage(0)
        }
        civ_hutang_add_image_1.setOnClickListener { chooseDialogPickImage(this, civ_hutang_add_image_1, 1) }
        civ_hutang_remove_image_1.setOnClickListener {
            removeImage(1)
        }
    }

    private fun calculateNominalCount() {
        val nominal: Int = if (et_hutang_add_nominal.text.toString().isNotEmpty() && !et_hutang_add_nominal.text.toString().trim().equals("Rp", true)) Utils.getRupiahToString(et_hutang_add_nominal).toInt() else "1".toInt()
        val installmentCount: Int = if (et_hutang_add_installment_count.text.toString().isNotEmpty()) et_hutang_add_installment_count.text.toString().trim().toInt() else "1".toInt()
        if (installmentCount < 1) {
            til_hutang_add_installment_nominal.visibility = View.GONE
        } else {
            if (nominal <= 1) {
                et_hutang_add_nominal.requestFocus()
            } else {
                val i = nominal / installmentCount
                et_hutang_add_installment_nominal.setText(Utils.setRupiah(i.toString()))
            }
            til_hutang_add_installment_nominal.visibility = View.VISIBLE
        }
    }

    private fun removeImage(pos: Int) {
        if (isBundle) {
            if (hutang.hutangBuktiGambar != null) {
                if (hutang.hutangBuktiGambar[pos].isNotEmpty()) {
                    storageUtil.deleteImage(hutang.hutangBuktiGambar[pos]
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
                }
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
        listTypeInstallmentCount = arrayListOf(Constants.Installment.Month, Constants.Installment.Year, Constants.Installment.Day)
        val adapter = ArrayAdapter(this, R.layout.multiline_spinner_item, listTypeInstallmentCount)
        adapter.setDropDownViewResource(R.layout.multiline_spinner_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listTypeInstallmentCountPos = position
                when (listTypeInstallmentCount[position]) {
                    Constants.Installment.Day -> InputValidUtil.setEditTextMaxLenght(et_hutang_add_installment_count, 3)
                    Constants.Installment.Month -> InputValidUtil.setEditTextMaxLenght(et_hutang_add_installment_count, 2)
                    Constants.Installment.Year -> InputValidUtil.setEditTextMaxLenght(et_hutang_add_installment_count, 4)
                }

                et_hutang_add_installment_count.setText("1")
                et_hutang_add_installment_count.clearFocus()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setSuggestUser(resultAccount: String, pickAccount: Int) {
        showProgressDialog()
        if (pickAccount == 0) {
            FirebaseDatabaseHandler.getUserByPhone(this, resultAccount, object : FirebaseDatabaseUtil.ValueListenerObject {
                override fun onSuccess(dataSnapshot: Any?) {
                    dismissProgressDialog()
                    setCheckSuggestUsers(dataSnapshot)
                }

                override fun onFailure(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                    et_hutang_add_user.setText("")
                }
            })
        } else {
            FirebaseDatabaseHandler.getUserByEmail(this, resultAccount, object : FirebaseDatabaseUtil.ValueListenerObject {
                override fun onSuccess(dataSnapshot: Any?) {
                    dismissProgressDialog()
                    setCheckSuggestUsers(dataSnapshot)
                }

                override fun onFailure(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                    et_hutang_add_user.setText("")
                }
            })
        }
    }

    private fun setCheckSuggestUsers(dataSnapshot: Any?) {
        userInvite = dataSnapshot as User
        if (userInvite.getuId()!!.equals(UserPreference.getInstance().uid, true)) {
            showSnackbar(getString(R.string.message_its_you))
            userInvite = User()
        } else if (!hutang.hutangKeluargaId.isNullOrEmpty() && userInvite.getuId()!!.equals(hutang.hutangKeluargaId, true)) {
            showSnackbar(getString(R.string.message_its_you_family))
            userInvite = User()
        } else {
            iv_hutang_add_user_clear.visibility = View.VISIBLE
            et_hutang_add_user.setText(userInvite.name)
        }
    }

    private fun setSuggestUserFamily(completeTextView: AutoCompleteTextView) {
        FirebaseDatabaseHandler.getUsers(true, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {

                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        userSuggest.add(user!!)
                    }

                    userSuggestAdapter = UserSuggestAdapter(this@HutangAddEditActivity, R.layout.activity_hutang_add_edit, R.id.et_hutang_add_desc, userSuggest)
                    completeTextView.setAdapter(userSuggestAdapter)
                } else {
                    Log.d("Lihat", "onSuccess HutangAddEditActivity : " + "datasnapshot kosong")
                }
            }

            override fun onFailure(message: String?) {
                Log.d("Lihat", "onFailure HutangAddEditActivity : $message")
            }
        })
        completeTextView.threshold = 3
        completeTextView.setOnItemClickListener { parent, view, position, id ->
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $parent")
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $view")
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $position")
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : $id")

            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : " + (parent.adapter.getItem(position) as User).name)
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : " + (parent.adapter.getItem(position) as User).getuId())
            Log.d("Lihat", "setSuggestUserFamily HutangAddEditActivity : " + userSuggest[position])

            hutang.hutangKeluargaNama = (parent.adapter.getItem(position) as User).name ?: ""
            hutang.hutangKeluargaId = (parent.adapter.getItem(position) as User).getuId() ?: ""

            if (hutang.hutangKeluargaId!!.equals(UserPreference.getInstance().uid, true)) {
                showSnackbar(getString(R.string.message_its_you))
                hutang.hutangKeluargaId = ""
                hutang.hutangKeluargaNama = ""
                et_hutang_add_user_family.setText("")
            } else if (!hutang.piutangId.isNullOrEmpty() || !hutang.penghutangId.isNullOrEmpty()) {
                if (hutang.hutangKeluargaId!!.equals(hutang.piutangId, true) || hutang.hutangKeluargaId!!.equals(hutang.penghutangId, true)) {
                    showSnackbar(getString(R.string.message_its_you_invite))
                    hutang.hutangKeluargaId = ""
                    hutang.hutangKeluargaNama = ""
                    et_hutang_add_user_family.setText("")
                }
            } else {
                iv_hutang_add_family_clear.visibility = View.VISIBLE
            }
        }
    }

    private fun checkData(isFocus: Boolean): Boolean {
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, et_hutang_add_user, isFocus)) return false
        else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_nominal, et_hutang_add_nominal, isFocus)) return false
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

    private fun chooseDialogPickImage(activity: Activity, imageView: ImageView?, positionImage: Int) {
        this.positionImage = positionImage
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            if (isBundle && hutang.hutangBuktiGambar != null) {
                Utils.showIntroductionDialog(
                        this,
                        "",
                        "Konfirmasi",
                        "Anda Akan menghapus semua gambar lama menjadi baru, apakah anda setuju?",
                        "Iya",
                        "Tidak",
                        false,
                        -1, object : Utils.IntroductionButtonListener {
                    override fun onFirstButtonClick() {
                        pictureUtil2.chooseGetImageDialog(activity, imageView)

                        removeImagePath(0)
                        removeImagePath(1)
                        hutang.hutangBuktiGambar.clear()
                        hutang.hutangBuktiGambar = null
                    }

                    override fun onSecondButtonClick() {}
                })
            } else {
                pictureUtil2.chooseGetImageDialog(activity, imageView)
            }
        }
    }

    private fun chooseDialogPickUserData() {
        val items = arrayOf("Pilih Kontak", "Pilih Email")

        val chooseImageDialog = AlertDialog.Builder(this)
        chooseImageDialog.setItems(items) { _: DialogInterface?, position: Int ->
            Log.d("Lihat", "chooseDialogPickUserData HutangAddEditActivity : $position")
            pickAccount = if (items[position] == "Pilih Kontak") {
                AndroidUtil.pickPhoneAccount(this)
                0
            } else {
                AndroidUtil.pickEmailAccount(this)
                1
            }
        }
        chooseImageDialog.show()
    }

    private fun saveHutangValidate() {
        if (checkData(true)) {
            if (mPresenter.getRadioGroupIndex(rg_hutang_add_user) == 0) {
                hutang.hutangRadioIndex = 0
                hutang.penghutangId = UserPreference.getInstance().uid
                hutang.penghutangNama = UserPreference.getInstance().name
                hutang.penghutangEmail = UserPreference.getInstance().email
                hutang.penghutangPersetujuanBaru = true
                hutang.penghutangPersetujuanUbah = true
                hutang.penghutangPersetujuanHapus = true

                hutang.piutangId = userInvite.getuId() ?: ""
                hutang.piutangNama = userInvite.name ?: et_hutang_add_user.text.toString().trim()
                hutang.piutangEmail = userInvite.email ?: ""
                hutang.piutangPersetujuanBaru = hutang.piutangPersetujuanBaru
                        ?: hutang.piutangId.isNullOrEmpty()
                hutang.piutangPersetujuanUbah = !isBundle
                hutang.piutangPersetujuanHapus = true
            } else {
                hutang.hutangRadioIndex = 1
                hutang.piutangId = UserPreference.getInstance().uid
                hutang.piutangNama = UserPreference.getInstance().name
                hutang.piutangEmail = UserPreference.getInstance().email
                hutang.piutangPersetujuanBaru = true
                hutang.piutangPersetujuanUbah = true
                hutang.piutangPersetujuanHapus = true

                hutang.penghutangId = userInvite.getuId() ?: ""
                hutang.penghutangNama = userInvite.name ?: et_hutang_add_user.text.toString().trim()
                hutang.penghutangEmail = userInvite.email ?: ""
                hutang.penghutangPersetujuanBaru = hutang.penghutangPersetujuanBaru
                        ?: hutang.penghutangId.isNullOrEmpty()
                hutang.penghutangPersetujuanUbah = !isBundle
                hutang.penghutangPersetujuanHapus = true
            }

            calculateNominalCount()

            hutang.hutangKeluargaId = hutang.hutangKeluargaId ?: ""
            hutang.hutangKeluargaNama = hutang.hutangKeluargaNama ?: ""

            hutang.piutang_penghutang_id = hutang.penghutangId + "_" + hutang.piutangId
            hutang.hutangNominal = Utils.getRupiahToString(et_hutang_add_nominal.text.toString().trim())
            hutang.hutangKeperluan = et_hutang_add_desc.text.toString().trim()
            hutang.hutangCatatan = et_hutang_add_note.text.toString().trim()
            hutang.hutangPinjam = et_hutang_add_date.text.toString().trim()

            hutang.hutangIsCicilan = sw_hutang_add_installment.isChecked
            if (hutang.hutangIsCicilan) {
                hutang.hutangCicilanBerapaKali = et_hutang_add_installment_count.text.toString().trim()
                hutang.hutangCicilanBerapaKaliType = listTypeInstallmentCount[listTypeInstallmentCountPos]
                hutang.hutangCicilanBerapaKaliPosisi = listTypeInstallmentCountPos
                hutang.hutangCicilanNominal = Utils.getRupiahToString(et_hutang_add_installment_nominal.text.toString().trim())
                hutang.hutangisBayarKapanSaja = cb_hutang_add_installment_free_to_pay.isChecked
                if (!hutang.hutangisBayarKapanSaja) {
                    hutang.hutangCicilanTanggalAkhir = et_hutang_add_installment_due_date.text.toString().trim()
                }
            } else {
                hutang.hutangCicilanBerapaKali = ""
                hutang.hutangCicilanBerapaKaliType = ""
                hutang.hutangCicilanNominal = ""
                hutang.hutangisBayarKapanSaja = false
                hutang.hutangCicilanTanggalAkhir = ""
            }

            if (mCurrentPhotoPath0Uri != null || mCurrentPhotoPath1Uri != null) {
                val listUri = PictureUtil2.convertArrayUriToArrayListUri(mCurrentPhotoPath0Uri, mCurrentPhotoPath1Uri)
                storageUtil.uploadImages("Hutang", listUri, object : FirebaseStorageUtil.DoneListener {
                    override fun isFinised(imageDonwloadUrls: ArrayList<String>?) {
                        if (isBundle && hutang.hutangBuktiGambar != null) {
                            if (hutang.hutangBuktiGambar.size > 0) {
                                storageUtil.deleteImages1(hutang.hutangBuktiGambar,
                                        object : FirebaseStorageUtil.DoneRemoveListener {
                                            override fun isFinised() {
                                                hutang.hutangBuktiGambar = imageDonwloadUrls
                                                mPresenter.saveEditHutang(hutang, databaseUtil, isBundle)
                                            }

                                            override fun isFailed(message: String?) {
                                                showSnackbar(message)
                                            }
                                        })
                            } else {
                                hutang.hutangBuktiGambar = imageDonwloadUrls
                                mPresenter.saveEditHutang(hutang, databaseUtil, isBundle)
                            }
                        } else {
                            hutang.hutangBuktiGambar = imageDonwloadUrls
                            mPresenter.saveEditHutang(hutang, databaseUtil, isBundle)
                        }
                    }

                    override fun isFailed(message: String?) {
                        showSnackbar(message)
                    }
                })
            } else {
                mPresenter.saveEditHutang(hutang, databaseUtil, isBundle)
            }
        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }

}

