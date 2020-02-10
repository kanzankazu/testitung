package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.Utils
import kotlinx.android.synthetic.main.item_title_desc_icon_option.view.*

/**
 * Created by Faisal Bahri on 2019-12-05.
 */
class ProfileAccountOptionAdapter(private val mActivity: Activity, private val mView: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: MutableList<ProfileAccountModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title_desc_icon_option, parent, false)
        return ProfileListAdapterAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        val list = arrayListOf<ProfileAccountModel>()
        for (model in datas) {
            if (model.isShow) list.add(model)
        }
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ProfileListAdapterAdapterHolder
        h.setView(datas[position], position)
        h.setListener(datas[position], position)

    }

    inner class ProfileListAdapterAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun setView(model: ProfileAccountModel, position: Int) {
            itemView.tv_item_description.visibility = View.GONE
            if (model.isShow) {
                Utils.setDrawableImageView(mActivity, itemView.ib_item_left_icon, model.icon)
                itemView.tv_item_title.text = model.title
            } else {
                itemView.visibility = View.GONE
            }

        }

        fun setListener(model: ProfileAccountModel, position: Int) {
            itemView.setOnClickListener { mView.onItemAdapterClick(position, model) }
        }
    }

    fun setData(datas: List<ProfileAccountModel>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<ProfileAccountModel>
        } else {
            this.datas = datas as ArrayList<ProfileAccountModel>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<ProfileAccountModel>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<ProfileAccountModel>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: ProfileAccountModel) {
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

    fun restoreData(data: ProfileAccountModel, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: ProfileAccountModel, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemAdapterClick(position: Int, data: ProfileAccountModel)
    }

}
