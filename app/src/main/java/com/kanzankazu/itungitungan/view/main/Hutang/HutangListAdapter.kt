package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.UserPreference
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.AppUtil
import com.kanzankazu.itungitungan.util.Utils
import kotlinx.android.synthetic.main.item_hutang_list.view.*
import java.util.*


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
        h.setView(tempModel[position])
    }

    inner class HutangListAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        private val generator = ColorGenerator.MATERIAL
        private var color: Int = 0
        private var builder: TextDrawable.IBuilder? = null
        private var textDrawable: TextDrawable? = null
        private var name = ""
        private var email = ""
        private var isIInclude = false
        private var isIPenghutang = false
        private var isIPiutang = false
        private var isIFamily = false
        private var isDataPenghutang = false
        private var isLunas: Boolean = false

        fun setView(hutang: Hutang) {
            isIInclude = if (hutang.debtorCreditorId.isNotEmpty()) UserPreference.getInstance().uid.contains(hutang.debtorCreditorId, true) else false
            isIPenghutang = if (hutang.debtorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorId, true) else false
            isIPiutang = if (hutang.creditorId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorId, true) else false
            isIFamily = if (hutang.creditorFamilyId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.creditorFamilyId, true) else false
            isIFamily = if (hutang.debtorFamilyId.isNotEmpty()) UserPreference.getInstance().uid.equals(hutang.debtorFamilyId, true) else false
            isDataPenghutang = hutang.hutangRadioIndex == 0
            isLunas = hutang.statusLunas

            builder = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(4)
                    .width(80)
                    .height(80)
                    .endConfig()
                    .roundRect(20)

            checkPersetujuan(hutang)
            setPersetujuan(hutang)

            if (isIPenghutang) {
                setViewIPenghutang(hutang)
            } else if (isIPiutang) {
                setViewIPiutang(hutang)
            } else if (isIFamily) {
                if (isDataPenghutang) {
                    setViewIPenghutang(hutang)
                } else {
                    setViewIPiutang(hutang)
                }
            }

            itemView.tv_hutang_list_keperluan.text = hutang.hutangKeperluan

            itemView.tv_hutang_list_nominal.text = Utils.setRupiah(hutang.hutangNominal)
            if (hutang.hutangCicilanIs) {
                if (hutang.hutangCicilanBerapaKali.isNotEmpty() && hutang.hutangCicilanNominal.isNotEmpty()) {
                    itemView.cv_item_hutang_list_installment.visibility = View.VISIBLE
                    itemView.tv_hutang_list_nominal_installment_count.text = mActivity.getString(R.string.installment_count, hutang.hutangCicilanBerapaKali, hutang.hutangCicilanBerapaKaliType)
                    itemView.tv_hutang_list_nominal_installment_nominal.text = Utils.setRupiah(hutang.hutangCicilanNominal)
                } else {
                    itemView.cv_item_hutang_list_installment.visibility = View.GONE
                }
                if (hutang.hutangCicilanTanggalAkhir.isNotEmpty()) {
                    itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.VISIBLE
                    itemView.tv_hutang_list_nominal_installment_due_date.text = mActivity.getString(R.string.installment_duedate, hutang.hutangCicilanTanggalAkhir)
                } else {
                    itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.GONE
                }
            } else {
                itemView.cv_item_hutang_list_installment.visibility = View.GONE
                itemView.tv_hutang_list_nominal_installment_due_date.visibility = View.GONE
            }


            if (hutang.hutangBuktiGambar != null) {
                itemView.iv_hutang_list_is_image.visibility = View.VISIBLE
            } else {
                itemView.iv_hutang_list_is_image.visibility = View.GONE
            }

            if (hutang.hutangEditableis) {
                itemView.iv_hutang_list_is_editable.visibility = View.GONE
            } else {
                itemView.iv_hutang_list_is_editable.visibility = View.VISIBLE
            }

            if (isIFamily) {
                itemView.iv_hutang_list_is_family.visibility = View.VISIBLE
            } else {
                itemView.iv_hutang_list_is_family.visibility = View.GONE
            }

            itemView.iv_item_hutang_list_user.setImageDrawable(textDrawable)
        }

        fun checkPersetujuan(hutang: Hutang) {
            checkStatusLunas(hutang)
        }

        private fun checkStatusLunas(hutang: Hutang): Boolean {
            return if (isLunas) {
                setViewPersetujuan(false, false, false, false, true, hutang)
                false
            } else {
                checkPersetujuanPembayaran(hutang)
                true
            }
        }

        private fun checkPersetujuanPembayaran(hutang: Hutang): Boolean {
            var approvePay = false
            val listStat = mutableListOf<String>()
            for (hutangSub in hutang.hutangPembayaranSub) {
                if (hutangSub.approvalCreditor) {
                    listStat.add("approve")
                } else {
                    listStat.add("upapprove")
                }

                val approve = Collections.frequency(listStat, "approve")
                val upapprove = Collections.frequency(listStat, "upapprove")

                if (approve == hutang.hutangPembayaranSub.size) {
                    approvePay = true
                }
            }

            return if (hutang.hutangPembayaranSub.isNotEmpty() && !approvePay) {
                setViewPersetujuan(false, false, false, true, false, hutang)
                false
            } else {
                checkPersetujuanBaru(hutang)
                true
            }
        }

        private fun checkPersetujuanBaru(hutang: Hutang): Boolean {
            if (hutang.creditorApprovalNew && hutang.debtorApprovalNew) {
                checkPersetujuanUbah(hutang)
                return true
            } else {
                if (hutang.creditorApprovalNew && isIPiutang) {
                    setViewPersetujuan(false, false, false, false, false, hutang)
                    return false
                } else {
                    if (hutang.debtorApprovalNew && isIPenghutang) {
                        setViewPersetujuan(false, false, false, false, false, hutang)
                        return false
                    } else {
                        if ((!hutang.creditorId.isNullOrEmpty() && hutang.debtorId.isNullOrEmpty()) || (hutang.creditorId.isNullOrEmpty() && !hutang.debtorId.isNullOrEmpty())) {
                            setViewPersetujuan(false, false, false, false, false, hutang)
                            return false
                        } else {
                            setViewPersetujuan(true, false, false, false, false, hutang)
                            return false
                        }
                    }
                }
            }

        }

        private fun checkPersetujuanUbah(hutang: Hutang): Boolean {
            if (hutang.creditorApprovalEdit && hutang.debtorApprovalEdit) {
                checkPersetujuanHapus(hutang)
                return true
            } else {
                if ((hutang.creditorApprovalEdit && isIPiutang) || (hutang.debtorApprovalEdit && isIPenghutang)) {
                    setViewPersetujuan(false, false, false, false, false, hutang)
                    return false
                } else {
                    if ((!hutang.creditorId.isNullOrEmpty() && hutang.debtorId.isNullOrEmpty()) || (hutang.creditorId.isNullOrEmpty() && !hutang.debtorId.isNullOrEmpty())) {
                        setViewPersetujuan(false, false, false, false, false, hutang)
                        return false
                    } else {
                        setViewPersetujuan(false, true, false, false, false, hutang)
                        return false
                    }
                }
            }
        }

        private fun checkPersetujuanHapus(hutang: Hutang): Boolean {
            if (hutang.creditorApprovalDelete && hutang.debtorApprovalDelete) {
                setViewPersetujuan(false, false, false, false, false, hutang)
                return false
            } else {
                if ((hutang.creditorApprovalDelete && isIPiutang) || (hutang.debtorApprovalDelete && isIPenghutang)) {
                    setViewPersetujuan(false, false, false, false, false, hutang)
                    return false
                } else {
                    if ((!hutang.creditorId.isNullOrEmpty() && hutang.debtorId.isNullOrEmpty()) || (hutang.creditorId.isNullOrEmpty() && !hutang.debtorId.isNullOrEmpty())) {
                        setViewPersetujuan(false, false, false, false, false, hutang)
                        return false
                    } else {
                        setViewPersetujuan(false, false, true, false, false, hutang)
                        return false
                    }
                }
            }
        }

        fun setViewPersetujuan(isNeedAgreeNew: Boolean, isNeedAgreeEdit: Boolean, isNeedAgreeDelete: Boolean, isNeedAgreePay: Boolean, isLunas: Boolean, hutang: Hutang) {
            if (isNeedAgreeNew || isNeedAgreeEdit || isNeedAgreeDelete || isNeedAgreePay) {
                itemView.ll_item_hutang_list.alpha = 0.5F
                itemView.tv_item_hutang_list_lunas.visibility = View.GONE
                itemView.ll_item_hutang_list.isEnabled = false
                itemView.setOnClickListener {
                    when {
                        isNeedAgreeNew -> mView.onHutangApproveNewClick(hutang)
                        isNeedAgreeEdit -> mView.onHutangApproveEditClick(hutang)
                        isNeedAgreeDelete -> mView.onHutangApproveDeleteClick(hutang)
                        isNeedAgreePay -> mView.onHutangApprovePayClick(hutang)
                    }
                }
            } else if (isLunas) {
                itemView.ll_item_hutang_list.alpha = 0.5F
                itemView.tv_item_hutang_list_lunas.visibility = View.VISIBLE
                itemView.ll_item_hutang_list.isEnabled = false
                itemView.setOnClickListener {
                    setNormalOnClickListener(hutang)
                }
            } else {
                itemView.ll_item_hutang_list.alpha = 1F
                itemView.tv_item_hutang_list_lunas.visibility = View.GONE
                itemView.ll_item_hutang_list.isEnabled = true
                itemView.setOnClickListener {
                    setNormalOnClickListener(hutang)
                }
            }
        }

        private fun setPersetujuan(hutang: Hutang) {
            if (!hutang.creditorApprovalNew && !hutang.debtorId.isNullOrEmpty()) {
                itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE
            } else {
                if (hutang.creditorApprovalEdit != null && !hutang.creditorApprovalEdit && !hutang.debtorId.isNullOrEmpty()) {
                    itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE
                    itemView.tv_item_hutang_list_apprv_piutang.text = "Persetujuan Piutang Perubahan"
                } else {
                    if (hutang.creditorApprovalDelete != null && !hutang.creditorApprovalDelete && !hutang.debtorId.isNullOrEmpty()) {
                        itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE
                        itemView.tv_item_hutang_list_apprv_piutang.text = "Persetujuan Piutang Hapus"
                    } else {
                        itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE
                    }
                }
            }

            if (!hutang.debtorApprovalNew && !hutang.creditorId.isNullOrEmpty()) {
                itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE
            } else {
                if (hutang.debtorApprovalEdit != null && !hutang.debtorApprovalEdit && !hutang.creditorId.isNullOrEmpty()) {
                    itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE
                    itemView.tv_item_hutang_list_apprv_penghutang.text = "Persetujuan Penghutang Perubahan"
                } else {
                    if (hutang.debtorApprovalDelete != null && !hutang.debtorApprovalDelete && !hutang.creditorId.isNullOrEmpty()) {
                        itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE
                        itemView.tv_item_hutang_list_apprv_penghutang.text = "Persetujuan Penghutang Hapus"
                    } else {
                        itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE
                    }
                }
            }
        }

        private fun setViewIPenghutang(data: Hutang) {
            if (AppUtil.checkStringNVisibilityView(data.creditorName, itemView.tv_hutang_list_name)) {
                name = data.creditorName
                color = generator.getColor(name)
                textDrawable = builder?.build(Utils.getInitialName(name.trim()), color)
            }
            if (AppUtil.checkStringNVisibilityView(data.creditorEmail, itemView.tv_hutang_list_email)) {
                email = data.creditorEmail
            }

            itemView.tv_hutang_list_nominal.setTextColor(mActivity.resources.getColor(R.color.red))
        }

        private fun setViewIPiutang(data: Hutang) {
            if (AppUtil.checkStringNVisibilityView(data.debtorName, itemView.tv_hutang_list_name)) {
                name = data.debtorName
                color = generator.getColor(name)
                textDrawable = builder?.build(Utils.getInitialName(name.trim()), color)
            }
            if (AppUtil.checkStringNVisibilityView(data.debtorEmail, itemView.tv_hutang_list_email)) {
                email = data.debtorEmail
            }

            itemView.tv_hutang_list_nominal.setTextColor(mActivity.resources.getColor(R.color.green))
        }

        private fun setNormalOnClickListener(hutang: Hutang) {
            val strings: Array<String> =
                    if (isIFamily && !isLunas && hutang.hutangEditableis && (isIPenghutang || isIPiutang)) {
                        arrayOf("Detail", "Lihat")
                    } else if (isIFamily && !isLunas && !hutang.hutangEditableis && (isIPenghutang || isIPiutang)) {
                        arrayOf("Lihat")
                    } else if (!isIFamily && !isLunas && hutang.hutangEditableis && isIPenghutang) {
                        arrayOf("Ubah", "Lihat", "Bayar", "Hapus")
                    } else if (!isIFamily && !isLunas && !hutang.hutangEditableis && isIPenghutang) {
                        arrayOf("Lihat", "Bayar", "Hapus")
                    } else if (!isIFamily && !isLunas && hutang.hutangEditableis && isIPiutang) {
                        arrayOf("Ubah", "Lihat", "Hapus")
                    } else if (!isIFamily && !isLunas && !hutang.hutangEditableis && isIPiutang) {
                        arrayOf("Lihat", "Hapus")
                    } else if (!isIFamily && isLunas && (isIPenghutang || isIPiutang)) {
                        arrayOf("Lihat")
                    } else if (isIFamily && isLunas && (!isIPenghutang || !isIPiutang)) {
                        arrayOf("Lihat")
                    } else {
                        arrayOf("Lihat")
                    }
            Utils.listDialog(mActivity, strings) { _, which ->
                when (strings[which]) {
                    "Ubah" -> {
                        mView.onHutangUbahClick(hutang)
                    }
                    "Detail" -> {
                        mView.onHutangUbahClick(hutang)
                    }
                    "Lihat" -> {
                        mView.onHutangLihatClick(hutang)
                    }
                    "Bayar" -> {
                        mView.onHutangBayarClick(hutang)
                    }
                    "Hapus" -> {
                        mView.onHutangHapusClick(hutang, position, (!hutang.creditorApprovalDelete || !hutang.debtorApprovalDelete))
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
                val arrayList1 = ArrayList<Hutang>()

                for (subject in mainModel) {
                    if (subject.hutangRadioIndex == 0) {//saya berhutang(piutang)
                        if (subject.creditorEmail.toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            subject.creditorName.toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            subject.hutangNominal.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            arrayList1.add(subject)
                        }
                    } else {// saya pemberi hutang(penghutang)
                        if (subject.creditorEmail.toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            subject.creditorName.toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            subject.hutangNominal.toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
