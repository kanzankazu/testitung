package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
import com.kanzankazu.itungitungan.util.Utils
import kotlinx.android.synthetic.main.item_hutang_list.view.*

class HutangListAdapter(private val activity: Activity, private val view: HutangListContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: MutableList<Hutang> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hutang_list, parent, false)
        return HutangListAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        val h = p0 as HutangListAdapterHolder
        h.setView(datas[position], position)
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

            var name = ""
            if (data.hutangRadioIndex == 0) {
                if (data.piutangNama.isNullOrEmpty()) {
                    if (!data.piutangEmail.isNullOrEmpty()) {
                        name = data.piutangEmail
                    }
                } else {
                    name = data.piutangNama
                }

                itemView.tv_hutang_list_name.text = name
                itemView.tv_hutang_list_nominal.setTextColor(activity.resources.getColor(R.color.red))
            } else {
                if (data.penghutangNama.isNullOrEmpty()) {
                    if (!data.penghutangEmail.isNullOrEmpty()) {
                        name = data.penghutangEmail
                    }
                } else {
                    name = data.penghutangNama
                }

                itemView.tv_hutang_list_name.text = name
                itemView.tv_hutang_list_nominal.setTextColor(activity.resources.getColor(R.color.green))
            }

            if (data.piutangPersetujuan) itemView.cv_item_hutang_list_apprv_piutang.visibility = View.GONE else itemView.cv_item_hutang_list_apprv_piutang.visibility = View.VISIBLE
            if (data.penghutangPersetujuan) itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.GONE else itemView.cv_item_hutang_list_apprv_penghutang.visibility = View.VISIBLE

            itemView.tv_hutang_list_transaksi.text = ""
            itemView.tv_hutang_list_nominal.text = Utils.setRupiah(data.hutangNominal)
        }

        fun setListener(hutang: Hutang, position: Int) {
            itemView.setOnClickListener { view.itemViewOnClick(hutang) }
        }

    }

    fun setData(datas: List<Hutang>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<Hutang>
        } else {
            this.datas = datas as ArrayList<Hutang>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<Hutang>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<Hutang>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: Hutang) {
        val position = 0
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun removeAt(position: Int) {
        this.datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.datas.size)
    }

    fun removeDataFirst() {
        val position = 0
        this.datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.datas.size)
    }

    fun restoreData(data: Hutang, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: Hutang, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }

    init {
        this.datas = arrayListOf()
    }
}
