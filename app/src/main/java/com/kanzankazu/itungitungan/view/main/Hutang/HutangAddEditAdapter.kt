package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.R
import kotlinx.android.synthetic.main.item_image.view.*
import java.io.File

class HutangAddEditAdapter(val mActivity: Activity, val mView: HutangAddEditContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: MutableList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return HutangAddEditAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as HutangAddEditAdapterHolder
        h.setView(position)
        h.setListener(position)
    }

    inner class HutangAddEditAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun setView(position: Int) {
            Glide.with(mActivity)
                    .load(File(datas[position]))
                    .placeholder(R.drawable.ic_profile)
                    .into(itemView.civ_item_image)
        }

        fun setListener(position: Int) {
            itemView.civ_profile_photo_edit.setOnClickListener {  }
        }
    }

    fun getItemData(position: Int): String {
        return datas[position]
    }

    fun getItemDatas(): MutableList<String> {
        return datas
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

    fun addData(data: String) {
        this.datas.add(data)
        notifyDataSetChanged()
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
