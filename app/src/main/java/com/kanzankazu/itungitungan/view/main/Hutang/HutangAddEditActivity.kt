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
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.User
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

class HutangAddEditActivity : BaseActivity(), HutangAddEditContract.View, AdapterView.OnItemSelectedListener {
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
    private var userInvite: User? = null
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
            setSuggestInviteUser(emailAccountResult, pickAccount)
        } else if (requestCode == AndroidUtil.REQ_CODE_PICK_CONTACT) {
            val phoneAccountResult = AndroidUtil.pickPhoneAccountResult(requestCode, resultCode, data, this, true)
            setSuggestInviteUser(phoneAccountResult, pickAccount)
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

    override fun showprogressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

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

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    private fun setView() {
        Utils.setupAppToolbarForActivity(this, toolbar)

        permissionUtil = AndroidPermissionUtil(this, *AndroidPermissionUtil.permCameraGallery)
        pictureUtil2 = PictureUtil2(this)

        setSuggestGroupUser(et_hutang_add_user_family)
        setListTypeInstallmentCount(sp_hutang_add_installment_count)

        setBundle()
    }

    private fun setBundle() {
        val extras = intent.extras
        if (extras != null) {
            isBundle = true
            if (extras.containsKey(Constants.BUNDLE.Hutang)) {
                hutang = extras.getParcelable(Constants.BUNDLE.Hutang)
            }
            //isFromValuationActivity = extras.getBoolean(Constants.ScreenFlag.IS_FROM_VALUATION_ACTIVITY)
        }

        if (isBundle)
            setBundleData()
    }

    private fun setBundleData() {
        if (hutang.hutangRadioIndex == 0) {
            et_hutang_add_user.setText(hutang.piutangNama)
        } else {
            et_hutang_add_user.setText(hutang.penghutangNama)
        }

        et_hutang_add_nominal.setText(Utils.setRupiah(hutang.hutangNominal))
        et_hutang_add_desc.setText(hutang.hutangKeperluan)
        et_hutang_add_note.setText(hutang.hutangCatatan)
        et_hutang_add_date.setText(hutang.hutangPinjam)

        if (!hutang.hutangKeluargaId.isNullOrEmpty()) {
            iv_hutang_add_user_clear.visibility = View.VISIBLE
            et_hutang_add_user_family.setText(hutang.hutangKeluargaNama)
        }

        Handler().postDelayed({
            mPresenter.setRadioGroupIndex(rg_hutang_add_user, hutang.hutangRadioIndex).isChecked = true
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
        }

        et_hutang_add_user.addTextChangedListener(watcherValidate)
        et_hutang_add_nominal.addTextChangedListener(watcherValidate)
        et_hutang_add_desc.addTextChangedListener(watcherValidate)
        et_hutang_add_date.addTextChangedListener(watcherValidate)
        et_hutang_add_installment_due_date.addTextChangedListener(watcherValidate)

        et_hutang_add_installment_count.addTextChangedListener(watcherValidateInstallmentCount)

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
            userInvite = null
        }
        iv_hutang_add_user.setOnClickListener { chooseDialogPickUserData() }
        tv_hutang_add_simpan.setOnClickListener { saveHutangValidate() }

        civ_hutang_add_image_0.setOnClickListener { chooseDialogPickImage(this, civ_hutang_add_image_0, 0) }
        civ_hutang_remove_image_0.setOnClickListener {
            if (isBundle) {
                if (hutang.hutangBuktiGambar != null) {
                    if (hutang.hutangBuktiGambar[0].isNotEmpty()) {
                        storageUtil.deleteImage(hutang.hutangBuktiGambar[0]
                                , {
                            removeImage(0)
                            hutang.hutangBuktiGambar.removeAt(0)
                        }
                                , {
                            showSnackbar(it.message)
                            it.stackTrace
                        })
                    } else {
                        showSnackbar("gagal menghapus")
                    }
                } else {
                    removeImage(0)
                }
            } else {
                removeImage(0)
            }
        }
        civ_hutang_add_image_1.setOnClickListener { chooseDialogPickImage(this, civ_hutang_add_image_1, 1) }
        civ_hutang_remove_image_1.setOnClickListener {
            if (isBundle) {
                if (hutang.hutangBuktiGambar != null) {
                    if (hutang.hutangBuktiGambar[1].isNotEmpty()) {
                        storageUtil.deleteImage(hutang.hutangBuktiGambar[1]
                                , {
                            removeImage(1)
                            hutang.hutangBuktiGambar.removeAt(1)
                        }
                                , {
                            showSnackbar(it.message)
                            it.stackTrace
                        })
                    } else {
                        showSnackbar("gagal menghapus")
                    }
                } else {
                    removeImage(1)
                }
            } else {
                removeImage(1)
            }
        }
    }

    private fun removeImage(pos: Int) {
        if (pos == 0) {
            mCurrentPhotoPath0 = ""
            mCurrentPhotoPath0Uri = null
            Glide.with(this).load(File(mCurrentPhotoPath0)).placeholder(R.drawable.ic_profile_picture).into(civ_hutang_add_image_0)
            civ_hutang_remove_image_0.visibility = View.GONE
        } else {
            mCurrentPhotoPath1 = ""
            mCurrentPhotoPath1Uri = null
            Glide.with(this).load(File(mCurrentPhotoPath1)).placeholder(R.drawable.ic_profile_picture).into(civ_hutang_add_image_1)
            civ_hutang_remove_image_1.visibility = View.GONE
        }
    }

    private fun setListTypeInstallmentCount(spinner: Spinner) {
        listTypeInstallmentCount = arrayListOf(Constants.Installment.Month, Constants.Installment.Year, Constants.Installment.Day)
        val adapter = ArrayAdapter(this, R.layout.multiline_spinner_item, listTypeInstallmentCount)
        adapter.setDropDownViewResource(R.layout.multiline_spinner_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    private fun setSuggestInviteUser(resultAccount: String, pickAccount: Int) {
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

    private fun setSuggestGroupUser(completeTextView: AutoCompleteTextView) {
        User.getUsers(databaseUtil.rootRef, true, object : FirebaseDatabaseUtil.ValueListenerData {
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
            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : $parent")
            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : $view")
            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : $position")
            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : $id")

            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : " + (parent.adapter.getItem(position) as User).name)
            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : " + (parent.adapter.getItem(position) as User).getuId())
            Log.d("Lihat", "setSuggestGroupUser HutangAddEditActivity : " + userSuggest[position])

            hutang.hutangKeluargaNama = (parent.adapter.getItem(position) as User).name
            hutang.hutangKeluargaId = (parent.adapter.getItem(position) as User).getuId()
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

                        removeImage(0)
                        removeImage(1)
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
                hutang.piutangId = userInvite?.getuId() ?: hutang.piutangId ?: ""
                hutang.piutangNama = userInvite?.name ?: et_hutang_add_user.text.toString().trim()
                hutang.piutangEmail = userInvite?.email ?: hutang.piutangEmail ?: ""
                hutang.penghutangPersetujuan = true
                hutang.piutangPersetujuan = false
            } else {
                hutang.hutangRadioIndex = 1
                hutang.piutangId = UserPreference.getInstance().uid
                hutang.piutangNama = UserPreference.getInstance().name
                hutang.piutangEmail = UserPreference.getInstance().email
                hutang.penghutangId = userInvite?.getuId() ?: hutang.penghutangId ?: ""
                hutang.penghutangNama = userInvite?.name ?: et_hutang_add_user.text.toString().trim()
                hutang.penghutangEmail = userInvite?.email ?: hutang.penghutangEmail ?: ""
                hutang.piutangPersetujuan = true
                hutang.penghutangPersetujuan = false
            }

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
                hutang.hutangCicilanBerapaKali
                hutang.hutangCicilanBerapaKaliType
                hutang.hutangCicilanBerapaKaliPosisi
                hutang.hutangCicilanNominal
                hutang.hutangisBayarKapanSaja
                hutang.hutangCicilanTanggalAkhir
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

    private fun saveImageData() {
        if (mCurrentPhotoPath0Uri != null || mCurrentPhotoPath1Uri != null) {
            val listUri = PictureUtil2.convertArrayUriToArrayListUri(mCurrentPhotoPath0Uri, mCurrentPhotoPath1Uri)
            storageUtil.uploadImages("Hutang", listUri, object : FirebaseStorageUtil.DoneListener {
                override fun isFinised(imageDonwloadUrls: ArrayList<String>?) {
                    hutang.hutangBuktiGambar = imageDonwloadUrls
                    mPresenter.saveEditHutang(hutang, databaseUtil, isBundle)
                }

                override fun isFailed(message: String) {
                    showSnackbar(getString(R.string.message_image_save_failed))
                }
            })
        } else {
            mPresenter.saveEditHutang(hutang, databaseUtil, isBundle)
        }
    }
}
