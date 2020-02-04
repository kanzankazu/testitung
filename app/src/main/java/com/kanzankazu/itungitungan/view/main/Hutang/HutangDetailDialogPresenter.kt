package com.kanzankazu.itungitungan.view.main.Hutang

import android.support.v7.app.AppCompatActivity
import com.kanzankazu.itungitungan.model.Hutang

class HutangDetailDialogPresenter(var mActivity: AppCompatActivity) : HutangDetailDialogContract.Presenter {
    override fun showProgressDialoPresenter() {}

    override fun dismissProgressDialogPresenter() {}

    override fun onNoConnection(message: String?) {}

    override fun approveHutangNew(hutang: Hutang, isCancel: Boolean) {}

    override fun approveHutangEdit(hutang: Hutang, isCancel: Boolean) {}

    override fun requestHutangHapus(hutang: Hutang, isCancel: Boolean) {}

    override fun approveHutangHapus(hutang: Hutang) {}

    override fun approveHutangCicilanPay(hutang: Hutang) {}

    override fun hapusHutangCheckImage(hutang: Hutang) {}
}
