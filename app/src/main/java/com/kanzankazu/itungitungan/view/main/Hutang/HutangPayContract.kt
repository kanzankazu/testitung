package com.kanzankazu.itungitungan.view.main.Hutang

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.TextView
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangCicilan
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangPayContract {
    interface View : BaseViewContract {
        fun checkData(isFocus: Boolean): Boolean
        fun setSuggestNote(list: MutableList<NoteModel>)
        fun onSuggestItemClick(s: String)
        fun onSuggestItemRemoveClick(model: NoteModel)
    }

    interface Presenter {
        fun initSuggestAdapter(rv_hutang_pay_note: RecyclerView): HutangPayNoteAdapter
        fun initImageAdapter(rv_hutang_pay_image: RecyclerView, listener: ImageListAdapter.ImageListContract): ImageListAdapter
        fun getSuggestNote()
        fun addImage(): PictureUtil2
        fun saveSubHutangValidate(isNew: Boolean, huCil: HutangCicilan, hutang: Hutang, tv_hutang_pay_cicilan_ke: TextView, et_hutang_pay_nominal: EditText, et_hutang_pay_note: EditText, imageListAdapter: ImageListAdapter, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
        fun checkOverPay(etHutangPayNominal: TextView, hutang: Hutang, isNew: Boolean)
        fun saveImageData(hutang: Hutang, huCil: HutangCicilan, imageLocalUri: MutableList<Uri>, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
        fun saveUpdateData(hutang: Hutang, huCil: HutangCicilan, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
    }

}
