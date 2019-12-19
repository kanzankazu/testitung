package com.kanzankazu.itungitungan.view.main

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import kotlinx.android.synthetic.main.item_profile.view.*

/**
 * Created by Faisal Bahri on 2019-12-05.
 */
class ProfileListAdapter(private val mActivity: Activity, private val mView: ProfileFragmentContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: MutableList<ProfileModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ProfileListAdapterAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        val list = arrayListOf<ProfileModel>()
        for (model in datas) {
            if (model.isShow) list.add(model)
        }
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ProfileListAdapterAdapterHolder
        h.setView(datas[position])
        h.setListener(datas[position], position)

    }

    inner class ProfileListAdapterAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun setView(model: ProfileModel) {
            if (model.isShow) {
                itemView.ib_item_profile_icon.setImageDrawable(mActivity.resources.getDrawable(model.icon))
                itemView.tv_item_profile_title.text = model.title
            } else {
                itemView.visibility = View.GONE
            }

        }

        fun setListener(model: ProfileModel, position: Int) {
            itemView.setOnClickListener { mView.itemAdapterClick(position) }
        }
    }

    fun setData(datas: List<ProfileModel>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<ProfileModel>
        } else {
            this.datas = datas as ArrayList<ProfileModel>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<ProfileModel>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<ProfileModel>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: ProfileModel) {
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

    fun restoreData(data: ProfileModel, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: ProfileModel, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }

    init {
        this.datas = arrayListOf()
    }
}
