package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.InboxHistory

class InboxHistoryAdapter(var mActivity: Activity, var mView: InboxHistoryContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: MutableList<InboxHistory> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inbox, parent, false)
        return InboxAdapterAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as InboxAdapterAdapterHolder
        h.setView(datas[position])

    }

    inner class InboxAdapterAdapterHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        fun setView(model: InboxHistory) {

        }

        fun setListener(model: InboxHistory, itemView: View) {

        }
    }

    fun setData(datas: List<InboxHistory>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<InboxHistory>
        } else {
            this.datas = datas as ArrayList<InboxHistory>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<InboxHistory>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<InboxHistory>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: InboxHistory) {
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

    fun restoreData(data: InboxHistory, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: InboxHistory, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }
}
