package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.Hutang

class HutangDetailAdapter(private val mActivity: Activity, private val mView: HutangDetailContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var datas: MutableList<Hutang>

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hutang_list_detail, parent, false)
        return HutangDetailAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
    }

    inner class HutangDetailAdapterHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    }

}