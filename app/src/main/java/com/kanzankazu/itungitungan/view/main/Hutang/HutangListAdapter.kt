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

        fun setView(data: Hutang, position: Int) {

            isIInclude = if (!data.piutang_penghutang_id.isNullOrEmpty()) UserPreference.getInstance().uid.contains(data.piutang_penghutang_id, true) else false
            isIPenghutang = if (!data.penghutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(data.penghutangId, true) else false
            isIPiutang = if (!data.piutangId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(data.piutangId, true) else false
            isIFamily = if (!data.hutangKeluargaId.isNullOrEmpty()) UserPreference.getInstance().uid.equals(data.hutangKeluargaId, true) else false
            isDataPenghutang = data.hutangRadioIndex == 0

            if (data.piutangPersetujuan && data.penghutangPersetujuan) {
                setViewPersetujuan(true, data)
            } else if (data.piutangPersetujuan && isIPiutang) {
                setViewPersetujuan(true, data)
            } else if (data.penghutangPersetujuan && isIPenghutang) {
                setViewPersetujuan(true, data)
            } else {
                if (!data.piutangId.isNullOrEmpty() && data.penghutangId.isNullOrEmpty()) {
                    setViewPersetujuan(true, data)
                } else if (data.piutangId.isNullOrEmpty() && !data.penghutangId.isNullOrEmpty()) {
                    setViewPersetujuan(true, data)
                } else {
                    setViewPersetujuan(false, data)
                }
            }

            if (!data.piutangPersetujuan) itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE else itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE
            if (!data.penghutangPersetujuan) itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE else itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE

            if (isIPenghutang) {
                setViewPiutang(data)
            } else if (isIPiutang) {
                setViewPenghutang(data)
            }

            if (isDataPenghutang && isIFamily) {
                setViewPiutang(data)
            } else if (!isDataPenghutang && isIFamily) {
                setViewPenghutang(data)
            }

            itemView.tv_hutang_list_keperluan.text = data.hutangKeperluan

            itemView.tv_hutang_list_nominal.text = Utils.setRupiah(data.hutangNominal)
            if (!data.hutangCicilanBerapaKali.isNullOrEmpty() && !data.hutangCicilanNominal.isNullOrEmpty()) {
                itemView.cv_item_hutang_list_installment.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_count.text = mActivity.getString(R.string.installment_count, data.hutangCicilanBerapaKali, data.hutangCicilanBerapaKaliType)
                itemView.tv_hutang_list_nominal_installment_nominal.text = Utils.setRupiah(data.hutangCicilanNominal)
            } else {
                itemView.cv_item_hutang_list_installment.visibility = View.GONE
            }
            if (!data.hutangCicilanTanggalAkhir.isNullOrEmpty()) {
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_due_date.text = mActivity.getString(R.string.installment_duedate, data.hutangCicilanTanggalAkhir)
            } else {
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.GONE
            }
        }

        private fun setViewPersetujuan(isAgree: Boolean, data: Hutang) {
            if (isAgree) {
                itemView.ll_item_hutang_list.alpha = 1F
                itemView.ll_item_hutang_list.isEnabled = true
                setListener(data, position)
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

        private fun setListener(hutang: Hutang, position: Int) {
            itemView.setOnClickListener { setOnClickListener(hutang) }
        }

        fun setOnClickListener(hutang: Hutang) {
            val strings = arrayOf("Ubah", "Bayar", "Hapus")
            Utils.listDialog(mActivity, strings) { _, which ->
                when (which) {
                    0 -> {
                        mView.onHutangUbahClick(hutang)
                    }
                    1 -> {
                        mView.onHutangBayarClick(hutang)
                    }
                    else -> {
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
