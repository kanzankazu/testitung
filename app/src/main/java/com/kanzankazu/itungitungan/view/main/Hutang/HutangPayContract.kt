package com.kanzankazu.itungitungan.view.main.Hutang

import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil
import com.kanzankazu.itungitungan.util.PictureUtil2
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import com.kanzankazu.itungitungan.view.base.BasePresenterContractWOCheckMessage
import com.kanzankazu.itungitungan.view.base.BaseViewContract

interface HutangPayContract {
    interface View : BaseViewContract {
        fun checkData(isFocus: Boolean): Boolean
        fun setSuggestNote(list: MutableList<NoteModel>)
        fun onSuggestItemClick(s: String)
        fun onSuggestItemRemoveClick(model: NoteModel)
    }

    interface Presenter : BasePresenterContractWOCheckMessage {
        fun initSuggestAdapter(rv_hutang_pay_note: RecyclerView): HutangPayNoteAdapter
        fun initImageAdapter(rv_hutang_pay_image: RecyclerView, listener: ImageListAdapter.ImageListContract): ImageListAdapter
        fun getSuggestNote()
        fun addImage(): PictureUtil2
        fun saveSubHutangValidate(isNew: Boolean, huCil: HutangPembayaran, hutang: Hutang, payNominal: String, payNote: String, imageListAdapter: ImageListAdapter, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
        fun saveImageData(hutang: Hutang, huCil: HutangPembayaran, imageLocalUri: MutableList<Uri>, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
        fun saveUpdateData(hutang: Hutang, huCil: HutangPembayaran, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
        fun saveValidateImageData(imageListAdapter: ImageListAdapter, isNew: Boolean, hutang: Hutang, huCil: HutangPembayaran, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
    }

    interface Interactor {
        fun UpdateHutang(hutang: Hutang, listener: FirebaseDatabaseUtil.ValueListenerStringSaveUpdate)
    }

}
