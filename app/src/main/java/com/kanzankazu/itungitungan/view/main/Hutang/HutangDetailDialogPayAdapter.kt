package com.kanzankazu.itungitungan.view.main.Hutang

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.Utils
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import com.kanzankazu.itungitungan.view.adapter.ImageListAdapter
import kotlinx.android.synthetic.main.item_hutang_list_detail_pay.view.*

class HutangDetailDialogPayAdapter(var mActivity: FragmentActivity?, var mView: HutangDetailDialogContract.View, var datas: MutableList<HutangPembayaran>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hutang_list_detail_pay, parent, false)
        return HutangDetailDialogPayAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as HutangDetailDialogPayAdapterHolder
        h.setView(datas[position], position)
    }

    inner class HutangDetailDialogPayAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var imageListAdapter: ImageListAdapter

        fun setView(data: HutangPembayaran, position: Int) {
            if (position == 0) {
                itemView.v_hutang_list_detail_pay_separator.visibility = View.GONE
            } else {
                itemView.v_hutang_list_detail_pay_separator.visibility = View.VISIBLE
            }

            itemView.tv_hutang_list_detail_pay_nominal.text = Utils.setRupiah(data.paymentNominal)
            itemView.tv_hutang_list_detail_pay_payment_to.text = data.paymentTo.toString()
            itemView.tv_hutang_list_detail_pay_desc.text = data.paymentDesc
            itemView.tv_hutang_list_detail_pay_debt_approval.text = data.approvalDebtor.toString()
            itemView.tv_hutang_list_detail_pay_credit_approval.text = data.approvalCreditor.toString()

            imageListAdapter = initImageAdapter(mActivity, itemView.rv_hutang_list_detail_pay_image, object : ImageListAdapter.ImageListContract {
                override fun onImageListView(data: ImageModel, position: Int) {}

                override fun onImageListRemove(data: ImageModel, position: Int) {}

                override fun onImageListAdd(data: ImageModel, position: Int) {}
            })

            if (data.paymentProofImage.isNotEmpty()) {
                itemView.rv_hutang_list_detail_pay_image.visibility = View.VISIBLE

                imageListAdapter.addDatasString(data.paymentProofImage,"view")
            } else {
                itemView.rv_hutang_list_detail_pay_image.visibility = View.GONE
            }
        }

        fun setOnClickListener(position: Int) {}

        private fun initImageAdapter(mActivity: FragmentActivity?, rv_hutang_pay_image: RecyclerView, listener: ImageListAdapter.ImageListContract): ImageListAdapter {
            val linearLayoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
            val imageListAdapter = ImageListAdapter(mActivity as Activity, listener)
            rv_hutang_pay_image.layoutManager = linearLayoutManager
            rv_hutang_pay_image.adapter = imageListAdapter

            return imageListAdapter
        }

    }
}
