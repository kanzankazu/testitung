package com.kanzankazu.itungitungan.view.main.Hutang

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add.*
import kotlinx.android.synthetic.main.app_toolbar.*

class HutangAddEditActivity : BaseActivity(), HutangAddEditContract.View {

    private lateinit var pictureUtil: PictureUtil
    private lateinit var permissionUtil: AndroidPermissionUtil
    private lateinit var watcherValidate: TextWatcher
    private lateinit var watcherInstallment: TextWatcher
    private var positionImage: Int = -1
    private var mCurrentPhotoPath0: String? = ""
    private var mCurrentPhotoPath1: String? = ""
    private var mCurrentPhotoPath0Uri: Uri? = null
    private var mCurrentPhotoPath1Uri: Uri? = null
    private var mCurrentPhotoPath1Uris: MutableList<Uri> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_add)

        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pictureUtil.REQUEST_IMAGE_CAMERA || requestCode == pictureUtil.REQUEST_IMAGE_GALLERY) {
            val mCurrentPhotoPath = pictureUtil.onActivityResult(requestCode, resultCode, data)
            if (positionImage == 0) {
                mCurrentPhotoPath0Uri = data!!.data
                mCurrentPhotoPath0 = mCurrentPhotoPath
            } else {
                mCurrentPhotoPath1Uri = data!!.data
                mCurrentPhotoPath1 = mCurrentPhotoPath
            }
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val emailAccountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            et_hutang_add_user.setText(emailAccountResult)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickContactResult(requestCode, resultCode, data, this, true)
            et_hutang_add_user.setText(phoneAccountResult)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionUtil.RP_ACCESS) {
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, false)
        }
    }

    private fun setView() {
        Utils.setupAppToolbarForActivity(this, toolbar)

        permissionUtil = AndroidPermissionUtil(this, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil = PictureUtil(this)
    }

    @SuppressLint("LogNotTimber")
    private fun setListener() {
        watcherValidate = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkData()
            }
        }

        watcherInstallment = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    ll_hutang_add_installment.visibility = View.GONE
                } else {
                    ll_hutang_add_installment.visibility = View.VISIBLE
                }
            }
        }

        et_hutang_add_user.addTextChangedListener(watcherValidate)
        et_hutang_add_nominal.addTextChangedListener(watcherValidate)
        et_hutang_add_desc.addTextChangedListener(watcherValidate)
        et_hutang_add_date.addTextChangedListener(watcherValidate)

        et_hutang_add_due_date.addTextChangedListener(watcherInstallment)

        rg_hutang_add_user.setOnCheckedChangeListener { radioGroup, _ ->
            val radioButtonID = radioGroup.checkedRadioButtonId
            val view = radioGroup.findViewById<View>(radioButtonID)
            val index = radioGroup.indexOfChild(view)
            Log.d("Lihat", "setListener HutangAddEditActivity : $index")
            val radioButton = radioGroup.getChildAt(index) as RadioButton
            val toString = radioButton.text.toString()
            Log.d("Lihat", "setListener HutangAddEditActivity : $toString")

            if (index == 0) {
                til_hutang_add_user.hint = getString(R.string.invite_piutang)
            } else {
                til_hutang_add_user.hint = getString(R.string.invite_penghutang)
            }
        }

        et_hutang_add_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_date) }
        et_hutang_add_due_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_due_date) }

        iv_hutang_add_user.setOnClickListener { chooseDialogPickUserData() }
        tv_hutang_add_simpan.setOnClickListener { saveHutang() }

        civ_hutang_add_image_0.setOnClickListener { chooseDialogPickImage(civ_hutang_add_image_0, 0) }
        civ_hutang_add_image_1.setOnClickListener { chooseDialogPickImage(civ_hutang_add_image_1, 1) }
    }

    private fun checkData(): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, et_hutang_add_user, false)) {
            false
        } else if (InputValidUtil.isEmailOrPhone(et_hutang_add_user.text.toString(), til_hutang_add_user, et_hutang_add_user)) {
            false
        } else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_nominal, et_hutang_add_nominal, false)) {
            false
        } else if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_desc, et_hutang_add_desc, false)) {
            false
        } else !InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_date, et_hutang_add_date, false)
    }

    private fun chooseDialogPickImage(imageView: ImageView?, positionImage: Int) {
        this.positionImage = positionImage
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            pictureUtil.chooseGetImageDialog(imageView)
        }
    }

    private fun chooseDialogPickUserData() {
        val items = arrayOf("Pilih Kontak", "Pilih Email")

        val chooseImageDialog = AlertDialog.Builder(this)
        chooseImageDialog.setItems(items) { _, i ->
            if (items[i] == "Pilih Kontak") {
                AndroidUtil.pickContact(this)
            } else {
                AndroidUtil.pickEmailAccount(this)
            }
        }

        chooseImageDialog.show()
    }

    private fun getRadioGroupIndex(): Int {
        val radioButtonID = rg_hutang_add_user.checkedRadioButtonId
        val view = rg_hutang_add_user.findViewById<View>(radioButtonID)
        val index = rg_hutang_add_user.indexOfChild(view)
        val radioButton = rg_hutang_add_user.getChildAt(index) as RadioButton
        radioButton.text.toString()
        return index
    }

    private fun saveHutang() {
        if (checkData()) {
            val hutang = Hutang()
            if (getRadioGroupIndex() == 0) {
                hutang.hutangRadioIndex = 0
                hutang.penghutangId = UserPreference.getInstance().uid
                hutang.penghutangNama = UserPreference.getInstance().name
                hutang.penghutangEmail = UserPreference.getInstance().email
                hutang.piutangId = ""
                hutang.piutangNama = ""
                hutang.piutangEmail = ""
                hutang.penghutangPersetujuan = true
                hutang.piutangPersetujuan = false
            } else {
                hutang.hutangRadioIndex = 1
                hutang.penghutangId = ""
                hutang.penghutangNama = ""
                hutang.penghutangEmail = ""
                hutang.piutangId = UserPreference.getInstance().uid
                hutang.piutangNama = UserPreference.getInstance().name
                hutang.piutangEmail = UserPreference.getInstance().email
                hutang.penghutangPersetujuan = false
                hutang.piutangPersetujuan = true
            }

            hutang.hutangKeterangan = et_hutang_add_desc.text.toString().trim()
            hutang.hutangCatatan = et_hutang_add_note.text.toString().trim()

            hutang.hutangCicilanTanggalAkhir = et_hutang_add_due_date.text.toString().trim()
            hutang.hutangCicilanHarga = et_hutang_add_price_installment.text.toString().trim()
            hutang.hutangCicilanWaktu = et_hutang_add_installment.text.toString().trim()

            hutang.hutangBuktiGambar0 = mCurrentPhotoPath0
            hutang.hutangBuktiGambar1 = mCurrentPhotoPath1

            showProgressDialog()

            Hutang.setHutang(databaseUtil.getRootRef(false, false), this, hutang, object : FirebaseDatabaseUtil.ValueListenerString {
                override fun onSuccess(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                }

                override fun onFailure(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                }
            })
        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }
}
