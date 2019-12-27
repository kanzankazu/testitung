package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang
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

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val h = p0 as HutangListAdapterHolder
        h.setView(datas[p1])
        h.setListener(datas[p1], p1)
    }

    inner class HutangListAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun setView(data: Hutang) {
            itemView.iv_item_hutang_list_user

            if (data.hutangRadioIndex == 0) {
                itemView.tv_hutang_list_name.text = data.piutangNama
                itemView.tv_hutang_list_nominal.setTextColor(activity.resources.getColor(R.color.red))
            } else {
                itemView.tv_hutang_list_name.text = data.penghutangNama
                itemView.tv_hutang_list_nominal.setTextColor(activity.resources.getColor(R.color.green))
            }

            itemView.tv_hutang_list_transaksi.text = ""
            itemView.tv_hutang_list_nominal.text = data.hutangNominal
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
