package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.util.google.GooglePhoneNumberValidation
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add.*
import kotlinx.android.synthetic.main.app_toolbar.*

class HutangAddActivity : BaseActivity() {

    private lateinit var pictureUtil: PictureUtil
    private lateinit var permissionUtil: AndroidPermissionUtil
    private lateinit var watcherValidate: TextWatcher
    private lateinit var watcherInstallment: TextWatcher

    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_add)

        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GooglePhoneNumberValidation.REQ_CODE_G_PHONE_VALIDATION && resultCode == Activity.RESULT_OK) {
            val s = GooglePhoneNumberValidation.onActivityResults(requestCode, resultCode, data)
            actv_hutang_add_user.setText(s)
        } else if (requestCode == pictureUtil.REQUEST_IMAGE_CAMERA || requestCode == pictureUtil.REQUEST_IMAGE_GALLERY) {
            Handler().postDelayed({
                val mCurrentPhotoPath = pictureUtil.onActivityResult(requestCode, resultCode, data)
                this.mCurrentPhotoPath = mCurrentPhotoPath
            }, 500)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val emailAccountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            actv_hutang_add_user.setText(emailAccountResult)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickContactResult(requestCode, resultCode, data, this, true)
            actv_hutang_add_user.setText(phoneAccountResult)
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

        actv_hutang_add_user.addTextChangedListener(watcherValidate)
        et_hutang_add_nominal.addTextChangedListener(watcherValidate)
        et_hutang_add_desc.addTextChangedListener(watcherValidate)
        et_hutang_add_date.addTextChangedListener(watcherValidate)

        et_hutang_add_due_date.addTextChangedListener(watcherInstallment)

        et_hutang_add_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_date) }
        et_hutang_add_due_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_due_date) }

        iv_hutang_add_user.setOnClickListener { chooseDialogPickUserData() }
        civ_hutang_add_bukti1.setOnClickListener { chooseDialogPickImage(civ_hutang_add_bukti1) }
        civ_hutang_add_bukti2.setOnClickListener { chooseDialogPickImage(civ_hutang_add_bukti2) }
        civ_hutang_add_bukti3.setOnClickListener { chooseDialogPickImage(civ_hutang_add_bukti3) }
        tv_hutang_add_simpan.setOnClickListener { saveHutang() }
    }

    private fun checkData(): Boolean {
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, actv_hutang_add_user, false)) return false
        if (!InputValidUtil.isEmailOrPhone(actv_hutang_add_user.text.toString(), til_hutang_add_user, actv_hutang_add_user)) return false
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_nominal, et_hutang_add_nominal, false)) return false
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_desc, et_hutang_add_desc, false)) return false
        if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_date, et_hutang_add_date, false)) return false
        return true
    }

    private fun chooseDialogPickImage(imageView: ImageView) {
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
        val toString = radioButton.text.toString()
        return index
    }

    private fun saveHutang() {
        if (checkData()) {
            val hutang = Hutang()
            if (getRadioGroupIndex() == 0) {
                hutang.receiverId = UserPreference.getInstance().uid
                hutang.receiverNm = UserPreference.getInstance().name
                hutang.approvalReceiver = true
                hutang.approvalGiver = false
            } else {
                hutang.giverId = UserPreference.getInstance().uid
                hutang.giverNm = UserPreference.getInstance().name
                hutang.approvalReceiver = false
                hutang.approvalGiver = true
            }

            hutang.desc = et_hutang_add_desc.text.toString().trim()
            hutang.notes = et_hutang_add_note.text.toString().trim()

            hutang.dueDt = et_hutang_add_due_date.text.toString().trim()
            hutang.installmentPrice = et_hutang_add_price_installment.text.toString().trim()
            hutang.installmentTime = et_hutang_add_installment.text.toString().trim()

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
