package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.kanzankazu.itungitungan.Constants
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Firebase.FirebaseConnectionUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseHandler
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.Firebase.FirebaseStorageUtil
import com.kanzankazu.itungitungan.util.InputValidUtil

class HutangAddEditPresenter(private val mActivity: Activity, private val mView: HutangAddEditContract.View) : HutangAddEditContract.Presenter, FirebaseDatabaseUtil.ValueListenerStringSaveUpdate {

    private var hutang: Hutang = Hutang()
    private var inviteUid: String = ""
    private var isNotSilent: Boolean = false

    override fun showProgressDialogPresenter() {
        mView.showProgressDialogView()
    }

    override fun dismissProgressDialogPresenter() {
        mView.dismissProgressDialogView()
    }

    override fun onNoConnection(message: String?) {
        mView.showRetryDialogView()
    }

    override fun onSuccessSaveUpdate(message: String?) {
        if (isNotSilent) {
            dismissProgressDialogPresenter()
            mView.showSnackbarView(message)
            sendNotifAddEditHutang()
            mActivity.finish()
        }
    }

    override fun onFailureSaveUpdate(message: String?) {
        if (isNotSilent) {
            dismissProgressDialogPresenter()
            mView.showSnackbarView(message)
        }
    }

    override fun getSuggestUserValidate(resultAccount: String, textView: TextView) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                mView.showProgressDialogView()
                if (!InputValidUtil.isEmail(resultAccount)) {
                    FirebaseDatabaseHandler.getUserByPhone(mActivity, InputValidUtil.getPhoneNumber62(resultAccount), object : FirebaseDatabaseUtil.ValueListenerObject {
                        override fun onSuccessData(dataSnapshot: Any?) {
                            mView.dismissProgressDialogView()
                            mView.setCheckSuggestUsers(dataSnapshot)
                        }

                        override fun onFailureData(message: String?) {
                            mView.dismissProgressDialogView()
                            mView.showSnackbarView(message)
                            textView.text = ""
                        }
                    })
                } else {
                    FirebaseDatabaseHandler.getUserByEmail(mActivity, resultAccount, object : FirebaseDatabaseUtil.ValueListenerObject {
                        override fun onSuccessData(dataSnapshot: Any?) {
                            mView.dismissProgressDialogView()
                            mView.setCheckSuggestUsers(dataSnapshot)
                        }

                        override fun onFailureData(message: String?) {
                            mView.dismissProgressDialogView()
                            mView.showSnackbarView(message)
                            textView.text = ""
                        }
                    })
                }
            }

            override fun noInternet(message: String) {
                onNoConnection(message)
            }
        })
    }

    override fun getSuggestUserFamily() {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.getUsers(true, object : FirebaseDatabaseUtil.ValueListenerData {
                    override fun onSuccessData(dataSnapshot: DataSnapshot?) {
                        mView.setSuggestUserFamily(dataSnapshot)
                    }

                    override fun onFailureData(message: String?) {
                        Log.d("Lihat", "onFailureString HutangAddEditActivity : $message")
                    }
                })
            }

            override fun noInternet(message: String?) {
                Log.d("Lihat noInternet HutangAddEditPresenter", message)
            }
        })
    }

    override fun getRadioGroupIndex(radioGroup: RadioGroup): Int {
        val radioButtonID = radioGroup.checkedRadioButtonId
        val view = radioGroup.findViewById<View>(radioButtonID)
        val index: Int = radioGroup.indexOfChild(view)
        val radioButton = radioGroup.getChildAt(index) as RadioButton
        radioButton.text.toString()
        return index
    }

    override fun setRadioGroupIndex(rg_hutang_add_user: RadioGroup, index: Int): RadioButton {
        return (rg_hutang_add_user.getChildAt(index) as RadioButton)
    }

    override fun saveImageHutang(hutang: Hutang, isEdit: Boolean, datasUri: MutableList<Uri>) {
        FirebaseStorageUtil.uploadImages(mActivity, "hutang", datasUri, object : FirebaseStorageUtil.DoneListenerMultiple {
            override fun isFinised(imageDownloadUrls: ArrayList<String>) {
                hutang.hutangProofImage = imageDownloadUrls
                saveEditHutang(hutang, isEdit, true)
            }

            override fun isFailed(message: String) {
                mView.showSnackbarView(message)
            }
        })
    }

    override fun saveEditHutang(hutang: Hutang, isEdit: Boolean, isNotSilent: Boolean) {
        this.hutang = hutang
        this.isNotSilent = isNotSilent
        this.inviteUid = ""
        if (hutang.hutangRadioIndex == 0) {
            if (hutang.creditorId.isNotEmpty()) {
                inviteUid = hutang.creditorId
            }
        } else {
            if (hutang.debtorId.isNotEmpty()) {
                inviteUid = hutang.debtorId
            }
        }

        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                showProgressDialogPresenter()
                if (!isEdit) {
                    FirebaseDatabaseHandler.setHutang(mActivity, hutang, this@HutangAddEditPresenter)
                } else {
                    FirebaseDatabaseHandler.updateHutang(mActivity, hutang, this@HutangAddEditPresenter)
                }
            }

            override fun noInternet(message: String?) {
                mView.showSnackbarView(message)
            }
        })
    }

    override fun getHutangByHid(hutangId: String?) {
        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.getHutangByHid(hutangId, true, object : FirebaseDatabaseUtil.ValueListenerDataTrueFalse {
                    override fun onSuccessDataExist(dataSnapshot: DataSnapshot?, isExsist: Boolean?) {
                        if (isExsist!!) {
                            mView.setHutangData(dataSnapshot)
                        } else {
                            mView.showSnackbarView(mActivity.getString(R.string.message_database_data_not_exist))
                        }
                    }

                    override fun onFailureDataExist(message: String?) {
                        mView.showSnackbarView(message)
                    }
                })
            }

            override fun noInternet(message: String?) {
                onNoConnection(message)
            }
        })
    }

    override fun sendNotifAddEditHutang() {
        val title: String
        val message: String
        val type: String = Constants.FirebasePushNotif.TypeNotif.hutangList

        if (hutang.hutangRadioIndex == 0) {
            title = "Piutang Baru"
            message = "Anda menjadi piutang baru, silahkan di buka aplikasinya"
        } else {
            title = "Piutang Baru"
            message = "Anda menjadi penghutang baru, silahkan di buka aplikasinya"
        }

        FirebaseConnectionUtil.isConnect(mActivity, object : FirebaseConnectionUtil.FirebaseConnectionListener {
            override fun hasInternet() {
                FirebaseDatabaseHandler.sendPushNotif(mActivity,inviteUid,title, message, type, "", "")
            }

            override fun noInternet(message: String?) {
                onNoConnection(message)
            }
        })
    }
}
