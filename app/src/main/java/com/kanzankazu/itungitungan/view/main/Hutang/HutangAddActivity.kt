package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.google.GooglePhoneNumberValidation
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add.*
import kotlinx.android.synthetic.main.app_toolbar.*

class HutangAddActivity : BaseActivity() {

    private lateinit var pictureUtil: PictureUtil
    private lateinit var permissionUtil: AndroidPermissionUtil

    private lateinit var validateWatcher: TextWatcher

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
        } else if (requestCode == pictureUtil.REQUEST_IMAGE_CAMERA|| requestCode == pictureUtil.REQUEST_IMAGE_GALLERY) {
            Handler().postDelayed({
                val mCurrentPhotoPath = pictureUtil.onActivityResult(requestCode, resultCode, data)
            },500)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionUtil.RP_ACCESS) {
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, false)
        }
    }

    private fun checkData(): Boolean {
        return when {
            !InputValidUtil.isRadioGroupChecked(rg_hutang_add_user) -> {
                showSnackbar(getString(R.string.message_radio_empty))
                false
            }
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, actv_hutang_add_user, false) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_nominal, et_hutang_add_nominal, false) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_desc, et_hutang_add_desc, false) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_date, et_hutang_add_date, false) -> false
            !InputValidUtil.isEmail(actv_hutang_add_user.text.toString()) && !InputValidUtil.isPhoneNumber(actv_hutang_add_user.text.toString()) -> false
            else -> true
        }
    }

    private fun setView() {
        Utils.setupAppToolbarForActivity(this, toolbar)

        permissionUtil = AndroidPermissionUtil(this, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil = PictureUtil(this)
    }

    private fun setListener() {
        validateWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                checkData()
            }
        }

        rg_hutang_add_user.setOnCheckedChangeListener { group, checkedId ->
            val radioButtonID = group.checkedRadioButtonId
            val view = group.findViewById<View>(radioButtonID)
            val index = group.indexOfChild(view)
            val radioButton = group.getChildAt(index) as RadioButton
            val selectedtext = radioButton.text.toString()

            checkData()
        }

        actv_hutang_add_user.addTextChangedListener(validateWatcher)
        et_hutang_add_nominal.addTextChangedListener(validateWatcher)
        et_hutang_add_desc.addTextChangedListener(validateWatcher)
        et_hutang_add_note.addTextChangedListener(validateWatcher)
        et_hutang_add_date.addTextChangedListener(validateWatcher)

        et_hutang_add_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_date) }
        et_hutang_add_due_date.setOnClickListener { Utility.showCalendarDialog(this, et_hutang_add_due_date) }

        iv_hutang_add_user.setOnClickListener { GooglePhoneNumberValidation.startPhoneNumberValidation(this) }
        civ_hutang_add_bukti1.setOnClickListener { getImage(civ_hutang_add_bukti1) }
        civ_hutang_add_bukti2.setOnClickListener { getImage(civ_hutang_add_bukti2) }
        civ_hutang_add_bukti3.setOnClickListener { getImage(civ_hutang_add_bukti3) }
        tv_hutang_add_simpan.setOnClickListener { checkData() }
    }

    private fun getImage(imageView: ImageView) {
        if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
            pictureUtil.chooseGetImageDialog(imageView)
        }
    }
}
