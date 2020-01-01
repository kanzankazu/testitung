package com.kanzankazu.itungitungan.view.main.Hutang

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.User
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.android.AndroidUtil
import com.kanzankazu.itungitungan.view.adapter.UserSuggestAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import id.otomoto.otr.utils.Utility
import kotlinx.android.synthetic.main.activity_hutang_add_edit.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.io.File
import java.util.*

class HutangAddEditActivity : BaseActivity(), HutangAddEditContract.View, AdapterView.OnItemSelectedListener {
    private lateinit var userSuggestAdapter: UserSuggestAdapter
    private lateinit var pictureUtil: PictureUtil
    private lateinit var permissionUtil: AndroidPermissionUtil
    private lateinit var watcherValidate: TextWatcher
    private lateinit var watcherValidateCount: TextWatcher
    private var pickAccount: Int = -1
    private var positionImage: Int = -1
    private var mCurrentPhotoPath0: String? = ""
    private var mCurrentPhotoPath1: String? = ""
    private var mCurrentPhotoPath0Uri: Uri? = null
    private var mCurrentPhotoPath1Uri: Uri? = null
    private var listTypeInstallmentCount = ArrayList<String>()
    private var listTypeInstallmentCountPos: Int = -1
    private var userInvite: User? = null
    var userInviteFamily: MutableList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_add_edit)

        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pictureUtil.REQUEST_IMAGE_CAMERA || requestCode == pictureUtil.REQUEST_IMAGE_GALLERY) {
            val mCurrentPhotoPath = pictureUtil.onActivityResult(requestCode, resultCode, data)
            if (positionImage == 0) {
                mCurrentPhotoPath0 = mCurrentPhotoPath
                mCurrentPhotoPath0Uri = Uri.fromFile(File(mCurrentPhotoPath0))
            } else {
                mCurrentPhotoPath1 = mCurrentPhotoPath
                mCurrentPhotoPath1Uri = Uri.fromFile(File(mCurrentPhotoPath1))
            }
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_EMAIL_ACCOUNT) {
            val emailAccountResult = AndroidUtil.pickEmailAccountResult(requestCode, resultCode, data)
            getSuggestInviteUser(emailAccountResult, pickAccount)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickPhoneAccountResult(requestCode, resultCode, data, this, true)
            getSuggestInviteUser(phoneAccountResult, pickAccount)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionUtil.RP_ACCESS) {
            permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, false)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        listTypeInstallmentCountPos = position
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun setView() {
        Utils.setupAppToolbarForActivity(this, toolbar)

        permissionUtil = AndroidPermissionUtil(this, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil = PictureUtil(this)

        setSuggestGroupUser()
        setListTypeInstallmentCount()
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

        watcherValidateCount = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (et_hutang_add_installment_count.text.toString().trim().isNullOrEmpty()) {
                    sp_hutang_add_installment_count.visibility = View.GONE
                } else {
                    sp_hutang_add_installment_count.visibility = View.VISIBLE
                }
            }
        }

        et_hutang_add_user.addTextChangedListener(watcherValidate)
        et_hutang_add_nominal.addTextChangedListener(watcherValidate)
        et_hutang_add_desc.addTextChangedListener(watcherValidate)
        et_hutang_add_date.addTextChangedListener(watcherValidate)

        et_hutang_add_installment_count.addTextChangedListener(watcherValidateCount)

        rg_hutang_add_user.setOnCheckedChangeListener { radioGroup, _ ->
            val radioButtonID = radioGroup.checkedRadioButtonId
            val view = radioGroup.findViewById<View>(radioButtonID)
            val index = radioGroup.indexOfChild(view)
            Log.d("Lihat", "setListener HutangAddEditActivity : $index")
            val radioButton = radioGroup.getChildAt(index) as RadioButton
            val toString = radioButton.text.toString().trim()
            Log.d("Lihat", "setListener HutangAddEditActivity : $toString")

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
                listTypeInstallmentCountPos = -1
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
            userInvite = null
        }
        iv_hutang_add_user.setOnClickListener { chooseDialogPickUserData() }
        tv_hutang_add_simpan.setOnClickListener { saveHutangValidate() }

        civ_hutang_add_image_0.setOnClickListener { chooseDialogPickImage(civ_hutang_add_image_0, 0) }
        civ_hutang_remove_image_0.setOnClickListener {
            mCurrentPhotoPath0 = ""
            mCurrentPhotoPath0Uri = null
            Glide.with(this).load(File(mCurrentPhotoPath0)).placeholder(R.drawable.ic_profile_picture).into(civ_hutang_add_image_0)
        }
        civ_hutang_add_image_1.setOnClickListener { chooseDialogPickImage(civ_hutang_add_image_1, 1) }
        civ_hutang_remove_image_1.setOnClickListener {
            mCurrentPhotoPath1 = ""
            mCurrentPhotoPath1Uri = null
            Glide.with(this).load(File(mCurrentPhotoPath1)).placeholder(R.drawable.ic_profile_picture).into(civ_hutang_add_image_1)
        }
    }

    private fun setListTypeInstallmentCount() {
        listTypeInstallmentCount = arrayListOf("Tahun", "Bulan", "Hari")
        val adapter = ArrayAdapter(this, R.layout.multiline_spinner_item, listTypeInstallmentCount)
        adapter.setDropDownViewResource(R.layout.multiline_spinner_item)
        sp_hutang_add_installment_count.adapter = adapter
        sp_hutang_add_installment_count.onItemSelectedListener = this
    }

    private fun getSuggestInviteUser(resultAccount: String, pickAccount: Int) {
        showProgressDialog()
        if (pickAccount == 0) {
            User.getUserByPhone(this, databaseUtil.rootRef, resultAccount, object : FirebaseDatabaseUtil.ValueListenerObject {
                override fun onSuccess(dataSnapshot: Any?) {
                    dismissProgressDialog()
                    userInvite = dataSnapshot as User
                    iv_hutang_add_user_clear.visibility = View.VISIBLE
                    et_hutang_add_user.setText(userInvite?.name)
                }

                override fun onFailure(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                    et_hutang_add_user.setText("")
                }
            })
        } else {
            User.getUserByEmail(this, databaseUtil.rootRef, resultAccount, object : FirebaseDatabaseUtil.ValueListenerObject {
                override fun onSuccess(dataSnapshot: Any?) {
                    dismissProgressDialog()
                    userInvite = dataSnapshot as User
                    iv_hutang_add_user_clear.visibility = View.VISIBLE
                    et_hutang_add_user.setText(userInvite?.name)
                }

                override fun onFailure(message: String?) {
                    dismissProgressDialog()
                    showSnackbar(message)
                    et_hutang_add_user.setText("")
                }
            })
        }
    }

    @SuppressLint("LogNotTimber")
    private fun setSuggestGroupUser() {
        et_hutang_add_user_family.threshold = 3

        User.getUsers(databaseUtil.rootRef, true, object : FirebaseDatabaseUtil.ValueListenerData {
            override fun onSuccess(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot != null) {
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        userInviteFamily.add(user!!)
                    }
                    userSuggestAdapter = UserSuggestAdapter(this@HutangAddEditActivity, R.layout.activity_hutang_add_edit, R.id.et_hutang_add_desc, userInviteFamily)
                    et_hutang_add_user_family.setAdapter(userSuggestAdapter)
                } else {
                    Log.d("Lihat", "onSuccess HutangAddEditActivity : " + "datasnapshot kosong")
                }
            }

            override fun onFailure(message: String?) {
                Log.d("Lihat", "onFailure HutangAddEditActivity : $message")
            }
        })

        et_hutang_add_user_family.setOnItemClickListener { parent, view, position, id ->
            Log.d("Lihat", "setListener HutangAddEditActivity : $parent")
            Log.d("Lihat", "setListener HutangAddEditActivity : $view")
            Log.d("Lihat", "setListener HutangAddEditActivity : $position")
            Log.d("Lihat", "setListener HutangAddEditActivity : $id")
        }

    }

    private fun checkData(): Boolean {
        return when {
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_user, et_hutang_add_user, false) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_nominal, et_hutang_add_nominal, false) -> false
            InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_desc, et_hutang_add_desc, false) -> false
            else -> !InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_add_date, et_hutang_add_date, false)
        }
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
                AndroidUtil.pickPhoneAccount(this)
                pickAccount = 0
            } else {
                AndroidUtil.pickEmailAccount(this)
                pickAccount = 1
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

    private fun saveHutangValidate() {
        if (checkData()) {
            val hutang = Hutang()

            if (getRadioGroupIndex() == 0) {
                hutang.hutangRadioIndex = 0
                hutang.penghutangId = UserPreference.getInstance().uid
                hutang.penghutangNama = UserPreference.getInstance().name
                hutang.penghutangEmail = UserPreference.getInstance().email
                hutang.piutangId = userInvite?.getuId() ?: ""
                hutang.piutangNama = userInvite?.name ?: et_hutang_add_user.text.toString().trim()
                hutang.piutangEmail = userInvite?.email ?: ""
                hutang.penghutangPersetujuan = true
                hutang.piutangPersetujuan = false
            } else {
                hutang.hutangRadioIndex = 1
                hutang.piutangId = UserPreference.getInstance().uid
                hutang.piutangNama = UserPreference.getInstance().name
                hutang.piutangEmail = UserPreference.getInstance().email
                hutang.penghutangId = userInvite?.getuId() ?: ""
                hutang.penghutangNama = userInvite?.name
                        ?: et_hutang_add_user.text.toString().trim()
                hutang.penghutangEmail = userInvite?.email ?: ""
                hutang.piutangPersetujuan = true
                hutang.penghutangPersetujuan = false
            }

            hutang.hutangNominal = Utils.getRupiahToString(et_hutang_add_nominal.text.toString().trim())
            hutang.hutangKeterangan = et_hutang_add_desc.text.toString().trim()
            hutang.hutangCatatan = et_hutang_add_note.text.toString().trim()

            hutang.cicilan = sw_hutang_add_installment.isChecked
            if (hutang.cicilan) {
                hutang.hutangCicilanBerapaKali = et_hutang_add_installment_count.text.toString().trim()
                if (listTypeInstallmentCountPos > -1) {
                    hutang.hutangCicilanBerapaKaliTipe = listTypeInstallmentCount[listTypeInstallmentCountPos]
                }
                hutang.hutangCicilanNominal = Utils.getRupiahToString(et_hutang_add_installment_nominal.text.toString().trim())
                hutang.freeTimeToPay = cb_hutang_add_installment_free_to_pay.isChecked
                if (hutang.freeTimeToPay) {
                    hutang.hutangCicilanTanggalAkhir = et_hutang_add_installment_due_date.text.toString().trim()
                }
            }

            if (mCurrentPhotoPath0Uri != null || mCurrentPhotoPath1Uri != null) {
                val listUri = PictureUtil.convertArrayUriToArrayListUri(mCurrentPhotoPath0Uri, mCurrentPhotoPath1Uri)
                storageUtil.uploadImages("Hutang", listUri, object : FirebaseStorageUtil.DoneListener {
                    override fun isFinised(imageDonwloadUrls: ArrayList<String>?) {
                        hutang.hutangBuktiGambar = imageDonwloadUrls
                        saveHutang(hutang)
                    }

                    override fun isFailed() {
                        showSnackbar(getString(R.string.message_database_save_failed))
                    }
                })
            } else {
                saveHutang(hutang)
            }
        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }

    private fun saveHutang(hutang: Hutang) {
        showProgressDialog()

        Hutang.setHutang(databaseUtil.rootRef, this, hutang, object : FirebaseDatabaseUtil.ValueListenerString {
            override fun onSuccess(message: String?) {
                dismissProgressDialog()
                showSnackbar(message)

                finish()
            }

            override fun onFailure(message: String?) {
                dismissProgressDialog()
                showSnackbar(message)
            }
        })
    }
}
