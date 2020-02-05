package com.kanzankazu.itungitungan.view.main.Hutang

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.HutangPembayaran
import com.kanzankazu.itungitungan.util.Utils
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
        h.setOnClickListener(position)

    }

    inner class HutangDetailDialogPayAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        }

        fun setOnClickListener(position: Int) {}
    }
}
