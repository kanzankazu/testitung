package com.kanzankazu.itungitungan.view.main

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Home
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mActivity: AppCompatActivity
    private lateinit var mView: HomeContract.View
    private lateinit var datas: MutableList<Home>

    constructor(mActivity: AppCompatActivity, mView: HomeContract.View) : this() {
        this.mActivity = mActivity
        this.mView = mView
        this.datas = arrayListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ProfileListAdapterAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        val list = arrayListOf<Home>()
        for (model in datas) {
            if (model.isShow) list.add(model)
        }
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as ProfileListAdapterAdapterHolder
        h.setView(datas[position])

    }

    inner class ProfileListAdapterAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        fun setView(model: Home) {

            if (model.image != 0) {
                Glide.with(mActivity)
                    .load(model.image)
                    .placeholder(R.drawable.ic_profile)
                    .into(itemView.iv_item_home_image)
                itemView.iv_item_home_image.visibility = View.VISIBLE
            } else {
                itemView.iv_item_home_image.visibility = View.GONE
            }

            itemView.tv_item_home_title.text = model.title

            if (model.isComingSoon) {
                itemView.ll_item_home.alpha = 0.5f
                itemView.ll_item_home.isEnabled = false
                itemView.cv_item_home_comming_soon.visibility = View.VISIBLE
                itemView.setOnClickListener(null)
            } else {
                itemView.ll_item_home.alpha = 1f
                itemView.ll_item_home.isEnabled = true
                itemView.cv_item_home_comming_soon.visibility = View.GONE
                setListener(model)
            }
        }

        private fun setListener(model: Home) {
            itemView.setOnClickListener { mView.itemAdapterClick(model) }
        }
    }

    fun setData(datas: List<Home>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<Home>
        } else {
            this.datas = datas as ArrayList<Home>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<Home>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<Home>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: Home) {
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

    fun restoreData(data: Home, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: Home, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }
}
