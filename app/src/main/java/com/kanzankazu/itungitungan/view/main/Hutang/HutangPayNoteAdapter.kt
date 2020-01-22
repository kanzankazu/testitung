package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import kotlinx.android.synthetic.main.item_estimation_text.view.*

/**
 * Created by Faisal Bahri on 2020-01-20.
 */
class HutangPayNoteAdapter(private val activity: Activity, private val mView: HutangPayContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: ArrayList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_estimation_text, parent, false)
        return HutangPayNoteAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as HutangPayNoteAdapterHolder
        h.setView(datas[position])
    }

    inner class HutangPayNoteAdapterHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        fun setView(model: String) {
            itemView.tv_item_suggest.text = model
        }

        fun setListener(model: String, itemView: View) {
            itemView.setOnClickListener {
mView.onSuggestItemClick(model)
            }
        }
    }

    fun setData(datas: List<String>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<String>
        } else {
            this.datas = datas as ArrayList<String>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<String>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<String>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: String) {
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

    fun restoreData(data: String, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: String, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }
}
