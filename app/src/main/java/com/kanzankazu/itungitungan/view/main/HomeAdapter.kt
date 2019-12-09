package com.kanzankazu.itungitungan.view.main

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.R
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
     private lateinit var mActivity: AppCompatActivity
     private lateinit var mView: HomeContract.View
     private lateinit var datas: MutableList<HomeModel>

    constructor(mActivity: AppCompatActivity, mView: HomeContract.View) : this() {
        this.mActivity = mActivity
        this.mView = mView
        this.datas = arrayListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mActivity).inflate(R.layout.item_home, parent, false)
        return ProfileListAdapterAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        val list = arrayListOf<HomeModel>()
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
        fun setView(model: HomeModel) {
            if (model.isShow) {
                Glide.with(mActivity).load(model.image).placeholder(R.drawable.ic_profile_picture).into(itemView.iv_item_home_image)
                itemView.tv_item_home_title.text = model.title
            } else {
                itemView.visibility = View.GONE
            }

        }

        fun setListener(model: HomeModel, position: Int) {
            itemView.setOnClickListener { mView.itemAdapterClick(model) }
        }
    }

    fun setData(datas: List<HomeModel>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<HomeModel>
        } else {
            this.datas = datas as ArrayList<HomeModel>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<HomeModel>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<HomeModel>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: HomeModel) {
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

    fun restoreData(data: HomeModel, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: HomeModel, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }
}
