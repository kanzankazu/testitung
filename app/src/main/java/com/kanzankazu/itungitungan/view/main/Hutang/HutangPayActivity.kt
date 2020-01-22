package com.kanzankazu.itungitungan.view.main.Hutang

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangCicilan
import com.kanzankazu.itungitungan.util.DateTimeUtil
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.android.AndroidPermissionUtil
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_hutang_pay.*
import retrofit2.Call
import java.io.File
import java.util.*


class HutangPayActivity : BaseActivity(), HutangPayContract.View, View.OnClickListener {
    private var mPresenter: HutangPayPresenter = HutangPayPresenter(this, this)
    private var isEdit: Boolean = false
    private var huCil: HutangCicilan = HutangCicilan()
    private var hutang: Hutang = Hutang()
    private var mCurrentPhotoPath: String = ""
    private var mCurrentPhotoPathUri: Uri? = null
    private lateinit var pictureUtil2: PictureUtil2
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var payNoteAdapter: HutangPayNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hutang_pay)

        setView()
        setListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pictureUtil2.REQUEST_IMAGE_CAMERA || requestCode == pictureUtil2.REQUEST_IMAGE_GALLERY) {
            mCurrentPhotoPath = pictureUtil2.onActivityResult(requestCode, resultCode, data)
            mCurrentPhotoPathUri = Uri.fromFile(File(mCurrentPhotoPath))

            imageListAdapter.addDataFirst(ImageModel(mCurrentPhotoPath, ""))
        }
    }

    override fun showToastView(message: String?) {
        showToast(message)
    }

    override fun showSnackbarView(message: String?) {
        showSnackbar(message)
    }

    override fun showRetryDialogView(call: Call<*>?) {}

    override fun showProgressDialogView() {
        showProgressDialog()
    }

    override fun dismissProgressDialogView() {
        dismissProgressDialog()
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_hutang_pay -> {
                saveSubHutangValidate()
            }
        }
    }

    override fun checkData(isFocus: Boolean): Boolean {
        return if (InputValidUtil.isEmptyField(getString(R.string.message_field_empty), til_hutang_pay_nominal, et_hutang_pay_nominal, isFocus)) false
        else InputValidUtil.isLenghtCharOver("Data kurang dari 6", til_hutang_pay_nominal, et_hutang_pay_nominal, 5)

    }

    override fun setSuggestNote(suggestNote: ArrayList<String>) {
        payNoteAdapter.setData(suggestNote)
    }

    override fun onSuggestItemClick(s: String) {
        et_hutang_pay_note.setText(s)
    }

    private fun setView() {
        initSuggestAdapter()
        initImageAdapter()

        mPresenter.getSuggestNote()

        getBundle()
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (bundle != null) {
            isEdit = true
            if (bundle.containsKey(Constants.Bundle.HUTANG)) {
                hutang = bundle.getParcelable(Constants.Bundle.HUTANG) as Hutang
            }

            setBundleData()
        }
    }

    private fun setBundleData() {
        civ_hutang_pay_user.setImageDrawable(Utils.getInitialNameDrawable(hutang.debtorName))
        et_hutang_pay_nominal.setText(if (hutang.hutangCicilanIs) Utils.setRupiah(hutang.hutangCicilanNominal) else Utils.setRupiah(hutang.hutangNominal))

        if (hutang.hutangCicilanIs) {
            ll_hutang_pay_cicilan.visibility = View.VISIBLE
            tv_hutang_pay_cicilan_ke.text = (hutang.hutangCicilanSub.size + 1).toString()
            tv_hutang_pay_cicilan_type.text = hutang.hutangCicilanBerapaKaliType
            if (!hutang.hutangCicilanIsBayarKapanSaja) {
                ll_hutang_pay_due_dt.visibility = View.VISIBLE
                tv_hutang_pay_due_dt.text = hutang.hutangCicilanTanggalAkhir
            } else {
                ll_hutang_pay_due_dt.visibility = View.GONE
            }
        } else {
            ll_hutang_pay_cicilan.visibility = View.GONE
        }
    }

    private fun setListener() {
        tv_hutang_pay.setOnClickListener(this)
    }

    private fun initSuggestAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        payNoteAdapter = HutangPayNoteAdapter(this, this)
        rv_hutang_pay_note.layoutManager = linearLayoutManager
        rv_hutang_pay_note.adapter = payNoteAdapter
    }

    private fun initImageAdapter() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageListAdapter = ImageListAdapter(this, object : ImageListAdapter.ImageListContract {
            override fun onImageListRemove(data: ImageModel, position: Int) {
                imageListAdapter.removeAt(position)
            }

            override fun onImageListAdd(data: ImageModel, position: Int) {
                val permissionUtil = AndroidPermissionUtil(this@HutangPayActivity, *AndroidPermissionUtil.permCameraGallery)
                pictureUtil2 = PictureUtil2(this@HutangPayActivity)
                if (permissionUtil.checkPermission(*AndroidPermissionUtil.permCameraGallery)) {
                    pictureUtil2.chooseGetImageDialog(this@HutangPayActivity)
                }
            }
        })
        rv_hutang_pay_image.layoutManager = linearLayoutManager
        rv_hutang_pay_image.adapter = imageListAdapter

        imageListAdapter.addData(ImageModel("", "other"))
    }

    private fun saveSubHutangValidate() {
        if (checkData(true)) {
            if (!isEdit) {
                huCil.createAt = DateTimeUtil.getCurrentDate().toString()
                huCil.createBy = UserPreference.getInstance().uid
            } else {
                huCil.updateAt = DateTimeUtil.getCurrentDate().toString()
                huCil.updateBy = UserPreference.getInstance().uid
            }
            huCil.hIdSub = hutang.hId + "_" + Date()
            huCil.hId = hutang.hId
            if (hutang.hutangCicilanIs) {
                huCil.paymentInstallmentTo = tv_hutang_pay_cicilan_ke.text.toString().toInt()
            }
            huCil.paymentNominal = et_hutang_pay_nominal.text.toString().trim()
            huCil.paymentDesc = et_hutang_pay_note.text.toString().trim()
            huCil.approvalCreditor
            huCil.approvalDebtor

        } else {
            showSnackbar(getString(R.string.message_validation_failed))
        }
    }
}
