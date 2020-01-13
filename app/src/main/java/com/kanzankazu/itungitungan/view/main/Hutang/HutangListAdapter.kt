package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Utils
import kotlinx.android.synthetic.main.item_hutang_list.view.*

class HutangListAdapter(private val mActivity: Activity, private val mView: HutangListContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mainModel: MutableList<Hutang> = arrayListOf()
    private var tempModel: MutableList<Hutang> = arrayListOf()
    private var subjectDataFilter: SubjectDataFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hutang_list, parent, false)
        return HutangListAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return tempModel.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        val h = p0 as HutangListAdapterHolder
        h.setView(tempModel[position], position)
    }

    inner class HutangListAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private var name = ""
        private var email = ""
        private var isIInclude = false
        private var isIPenghutang = false
        private var isIPiutang = false
        private var isIFamily = false
        private var isDataPenghutang = false

        fun setView(hutang: Hutang, position: Int) {

            isIInclude = if (!hutang.piutang_penghutang_id.isNullOrEmpty()) UserPreference.getInstance().uid.contains(hutang.piutang_penghutang_id, true) else false
            isIPenghutang = if (!hutang.penghutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.penghutangId, true) else false
            isIPiutang = if (!hutang.piutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.piutangId, true) else false
            isIFamily = if (!hutang.hutangKeluargaId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(hutang.hutangKeluargaId, true) else false
            isDataPenghutang = hutang.hutangRadioIndex == 0

            if (hutang.piutangPersetujuan && hutang.penghutangPersetujuan) {
                setViewPersetujuan(true, hutang)
            } else if (hutang.piutangPersetujuan && isIPiutang) {
                setViewPersetujuan(true, hutang)
            } else if (hutang.penghutangPersetujuan && isIPenghutang) {
                setViewPersetujuan(true, hutang)
            } else {
                if (!hutang.piutangId.isNullOrEmpty() && hutang.penghutangId.isNullOrEmpty()) {
                    setViewPersetujuan(true, hutang)
                } else if (hutang.piutangId.isNullOrEmpty() && !hutang.penghutangId.isNullOrEmpty()) {
                    setViewPersetujuan(true, hutang)
                } else {
                    setViewPersetujuan(false, hutang)
                }
            }

            if (!hutang.piutangPersetujuan) itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE else itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE
            if (!hutang.penghutangPersetujuan) itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE else itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE

            if (isIPenghutang) {
                setViewPiutang(hutang)
            } else if (isIPiutang) {
                setViewPenghutang(hutang)
            } else if (isIFamily) {
                if (isDataPenghutang) {
                    setViewPiutang(hutang)
                } else {
                    setViewPenghutang(hutang)
                }
            }

            itemView.tv_hutang_list_keperluan.text = hutang.hutangKeperluan

            itemView.tv_hutang_list_nominal.text = Utils.setRupiah(hutang.hutangNominal)
            if (!hutang.hutangCicilanBerapaKali.isNullOrEmpty() && !hutang.hutangCicilanNominal.isNullOrEmpty()) {
                itemView.cv_item_hutang_list_installment.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_count.text = mActivity.getString(R.string.installment_count, hutang.hutangCicilanBerapaKali, hutang.hutangCicilanBerapaKaliType)
                itemView.tv_hutang_list_nominal_installment_nominal.text = Utils.setRupiah(hutang.hutangCicilanNominal)
            } else {
                itemView.cv_item_hutang_list_installment.visibility = View.GONE
            }
            if (!hutang.hutangCicilanTanggalAkhir.isNullOrEmpty()) {
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_due_date.text = mActivity.getString(R.string.installment_duedate, hutang.hutangCicilanTanggalAkhir)
            } else {
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.GONE
            }
        }

        private fun setViewPersetujuan(isAgree: Boolean, data: Hutang) {
            if (isAgree) {
                itemView.ll_item_hutang_list.alpha = 1F
                itemView.ll_item_hutang_list.isEnabled = true
                itemView.setOnClickListener { setOnClickListener(data) }
            } else {
                itemView.ll_item_hutang_list.alpha = 0.5F
                itemView.ll_item_hutang_list.isEnabled = false
                itemView.setOnClickListener { mView.onAgreementApproveClick(data) }
            }
        }

        private fun setViewPiutang(data: Hutang) {
            if (!data.piutangNama.isNullOrEmpty()) {
                name = data.piutangNama
                itemView.tv_hutang_list_name.text = name
                itemView.tv_hutang_list_name.visibility = View.VISIBLE
            } else {
                itemView.tv_hutang_list_name.visibility = View.GONE
            }
            if (!data.piutangEmail.isNullOrEmpty()) {
                email = data.piutangEmail
                itemView.tv_hutang_list_email.text = email
                itemView.tv_hutang_list_email.visibility = View.VISIBLE
            } else {
                itemView.tv_hutang_list_email.visibility = View.GONE
            }

            itemView.tv_hutang_list_nominal.setTextColor(mActivity.resources.getColor(R.color.red))
        }

        private fun setViewPenghutang(data: Hutang) {
            if (!data.penghutangNama.isNullOrEmpty()) {
                name = data.penghutangNama
                itemView.tv_hutang_list_name.text = name
                itemView.tv_hutang_list_name.visibility = View.VISIBLE
            } else {
                itemView.tv_hutang_list_name.visibility = View.GONE
            }
            if (!data.penghutangEmail.isNullOrEmpty()) {
                email = data.penghutangEmail
                itemView.tv_hutang_list_email.text = email
                itemView.tv_hutang_list_email.visibility = View.VISIBLE
            } else {
                itemView.tv_hutang_list_email.visibility = View.GONE
            }

            itemView.tv_hutang_list_nominal.setTextColor(mActivity.resources.getColor(R.color.green))
        }

        fun setOnClickListener(hutang: Hutang) {
            val strings: Array<String> = if (isIFamily) {
                arrayOf("Ubah", "Lihat")
            } else {
                arrayOf("Ubah", "Lihat", "Bayar", "Hapus")
            }
            Utils.listDialog(mActivity, strings) { _, which ->
                when (strings[which]) {
                    "Ubah" -> {
                        mView.onHutangUbahClick(hutang)
                    }
                    "Lihat" -> {
                        mView.onHutangLihatClick(hutang)
                    }
                    "Bayar" -> {
                        mView.onHutangBayarClick(hutang)
                    }
                    "Hapus" -> {
                        mView.onHutangHapusClick(hutang, position)
                    }
                }
            }
        }
    }

    fun setData(datas: List<Hutang>) {
        if (datas.isNotEmpty()) {
            this.mainModel.clear()
            this.tempModel.clear()

            this.mainModel = datas as ArrayList<Hutang>
            this.tempModel = datas
        } else {
            this.mainModel = datas as ArrayList<Hutang>
            this.tempModel = datas
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<Hutang>) {
        this.mainModel.clear()
        this.mainModel.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<Hutang>) {
        this.mainModel.addAll(datas)
        notifyItemRangeInserted(this.mainModel.size, datas.size)
    }

    fun addDataFirst(data: Hutang) {
        val position = 0
        this.mainModel.add(position, data)
        notifyItemInserted(position)
    }

    fun removeAt(position: Int) {
        this.mainModel.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mainModel.size)
    }

    fun removeDataFirst() {
        val position = 0
        this.mainModel.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mainModel.size)
    }

    fun restoreData(data: Hutang, position: Int) {
        this.mainModel.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: Hutang, position: Int) {
        this.mainModel.set(position, data)
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        if (subjectDataFilter == null) {
            subjectDataFilter = SubjectDataFilter()
        }
        return subjectDataFilter as SubjectDataFilter
    }

    private inner class SubjectDataFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            var charSequence = charSequence

            charSequence = charSequence.toString().toLowerCase()
            val filterResults = FilterResults()

            if (!TextUtils.isEmpty(charSequence)) {
                val arrayList1 = java.util.ArrayList<Hutang>()

                for (subject in mainModel) {
                    if (subject.hutangRadioIndex == 0) {//saya berhutang(piutang)
                        if (subject.piutangEmail.toLowerCase().contains(charSequence.toString().toLowerCase()) || subject.piutangNama.toLowerCase().contains(charSequence.toString().toLowerCase()) || subject.hutangNominal.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList1.add(subject)
                        }
                    } else {// saya pemberi hutang(penghutang)
                        if (subject.piutangEmail.toLowerCase().contains(charSequence.toString().toLowerCase()) || subject.piutangNama.toLowerCase().contains(charSequence.toString().toLowerCase()) || subject.hutangNominal.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList1.add(subject)
                        }
                    }
                }

                filterResults.count = arrayList1.size
                filterResults.values = arrayList1
            } else {
                synchronized(this) {
                    filterResults.count = mainModel.size
                    filterResults.values = mainModel
                }
            }
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

            tempModel = filterResults.values as java.util.ArrayList<Hutang>

            mView.onHutangFilter(tempModel)

            notifyDataSetChanged()
        }
    }
}
