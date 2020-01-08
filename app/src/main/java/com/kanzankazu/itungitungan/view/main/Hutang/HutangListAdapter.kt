package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Utils
import kotlinx.android.synthetic.main.item_hutang_list.view.*

class HutangListAdapter(private val activity: Activity, private val view: HutangListContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        fun setView(data: Hutang, position: Int) {

            if (data.piutangPersetujuan || data.penghutangPersetujuan) {
                itemView.ll_item_hutang_list.alpha = 1F
                itemView.ll_item_hutang_list.isEnabled = true
                setListener(data, position)
            } else {
                itemView.ll_item_hutang_list.alpha = 0.5F
                itemView.ll_item_hutang_list.isEnabled = false
                itemView.setOnClickListener(null)
            }

            if (data.piutangPersetujuan) itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE else itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE
            if (data.penghutangPersetujuan) itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE else itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE

            var name = ""
            var email = ""
            if (data.hutangRadioIndex == 0) {//saya berhutang(piutang)
                if (data.piutangNama.isNullOrEmpty()) {
                    itemView.tv_hutang_list_name.visibility = View.GONE
                } else {
                    name = data.piutangNama
                    itemView.tv_hutang_list_name.text = name
                    itemView.tv_hutang_list_name.visibility = View.VISIBLE
                }

                if (data.piutangEmail.isNullOrEmpty()) {
                    itemView.tv_hutang_list_email.visibility = View.GONE
                } else {
                    email = data.piutangEmail
                    itemView.tv_hutang_list_email.text = email
                    itemView.tv_hutang_list_email.visibility = View.VISIBLE
                }

                if (data.piutangId.isNullOrEmpty()) {
                    itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE
                    itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE
                    itemView.cv_item_hutang_list_apprv_none.visibility = View.VISIBLE
                } else {
                    itemView.cv_item_hutang_list_apprv_none.visibility = View.GONE
                }

                itemView.tv_hutang_list_nominal.setTextColor(activity.resources.getColor(R.color.red))
            } else {// saya pemberi hutang(penghutang)
                if (data.penghutangNama.isNullOrEmpty()) {
                    itemView.tv_hutang_list_name.visibility = View.GONE
                } else {
                    name = data.penghutangNama
                    itemView.tv_hutang_list_name.text = name
                    itemView.tv_hutang_list_name.visibility = View.VISIBLE
                }

                if (data.penghutangEmail.isNullOrEmpty()) {
                    itemView.tv_hutang_list_email.visibility = View.GONE
                } else {
                    email = data.penghutangEmail
                    itemView.tv_hutang_list_email.text = email
                    itemView.tv_hutang_list_email.visibility = View.VISIBLE
                }

                if (data.penghutangId.isNullOrEmpty()) {
                    itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE
                    itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE
                    itemView.cv_item_hutang_list_apprv_none.visibility = View.VISIBLE
                } else {
                    itemView.cv_item_hutang_list_apprv_none.visibility = View.GONE
                }

                itemView.tv_hutang_list_nominal.setTextColor(activity.resources.getColor(R.color.green))
            }

            itemView.tv_hutang_list_keperluan.text = data.hutangKeperluan
            itemView.tv_hutang_list_nominal.text = Utils.setRupiah(data.hutangNominal)

            if (!data.hutangCicilanBerapaKali.isNullOrEmpty()) {
                itemView.tv_hutang_list_nominal_installment_count.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_count.text = activity.getString(R.string.installment_count, data.hutangCicilanBerapaKali)
            } else {
                itemView.tv_hutang_list_nominal_installment_count.visibility = View.GONE
            }
            if (!data.hutangCicilanNominal.isNullOrEmpty()) {
                itemView.tv_hutang_list_nominal_installment_nominal.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_nominal.text = Utils.setRupiah(data.hutangCicilanNominal)
            } else {
                itemView.tv_hutang_list_nominal_installment_nominal.visibility = View.GONE
            }
            if (!data.hutangCicilanTanggalAkhir.isNullOrEmpty()) {
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.VISIBLE
                itemView.tv_hutang_list_nominal_installment_due_date.text = activity.getString(R.string.installment_duedate, data.hutangCicilanTanggalAkhir)
            } else {
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.GONE
            }
        }

        fun setListener(hutang: Hutang, position: Int) {
            itemView.setOnClickListener { view.itemViewOnClick(hutang) }
        }
    }

    fun setData(datas: List<Hutang>) {
        if (datas.isNotEmpty()) {
            this.mainModel.clear()
            this.tempModel.clear()

            this.mainModel = datas as ArrayList<Hutang>
            this.tempModel = datas as ArrayList<Hutang>
        } else {
            this.mainModel = datas as ArrayList<Hutang>
            this.tempModel = datas as ArrayList<Hutang>
        }
        notifyDataSetChanged()
    }

    /*fun replaceData(datas: List<Hutang>) {
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
    }*/

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
                        if (subject.piutangEmail.contains(charSequence) || subject.piutangNama.contains(charSequence) || subject.hutangNominal.contains(charSequence)) {
                            arrayList1.add(subject)
                        }
                    } else {// saya pemberi hutang(penghutang)
                        if (subject.piutangEmail.contains(charSequence) || subject.piutangNama.contains(charSequence) || subject.hutangNominal.contains(charSequence)) {
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

            notifyDataSetChanged()
        }
    }
}
