package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import com.kanzankazu.itungitungan.model.Hutang

class HutangPayPresenter(val mActivity: Activity, val mView: HutangPayContract.View) : HutangPayContract.Presenter {
    override fun getSuggestNote() {
        val list = arrayListOf<String>()
        list.addAll(list)

        mView.setSuggestNote(list)
    }

    override fun getSuggestNoteManual(): ArrayList<String> {
        return arrayListOf("Terima kasih", "Maaf telat", "Lunas ya bos")
    }

    override fun saveData(hutang: Hutang) {

    }

    override fun deleteImage() {

    }
}
