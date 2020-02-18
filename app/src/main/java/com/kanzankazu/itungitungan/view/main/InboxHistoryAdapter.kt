package com.kanzankazu.itungitungan.view.main

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.model.InboxHistory
import com.kanzankazu.itungitungan.util.DateTimeUtil
import kotlinx.android.synthetic.main.item_inbox.view.*

class InboxHistoryAdapter(var mActivity: Activity, var mView: InboxHistoryContract.View) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var subjectDataFilter: SubjectDataFilter? = null
    private var mainModel: MutableList<InboxHistory> = mutableListOf()
    private var tempModel: MutableList<InboxHistory> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inbox, parent, false)
        return InboxAdapterAdapterHolder(view)
    }

    override fun getItemCount(): Int {
        return tempModel.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as InboxAdapterAdapterHolder
        h.setView(tempModel[position])

    }

    inner class InboxAdapterAdapterHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        fun setView(model: InboxHistory) {
            itemView.tv_item_inbox_name.text = model.inboxTitle
            itemView.tv_item_inbox_isi.text = model.inboxMessage
            itemView.tv_item_inbox_time.text = DateTimeUtil.getTimeAgoByDate(DateTimeUtil.convertStringToDateTime(model.createAt))
            itemView.iv_item_inbox_new.visibility = if (model.inboxIsRead) View.GONE else View.VISIBLE
        }

        fun setListener(model: InboxHistory, itemView: View) {
itemView.setOnClickListener{mView.onListItemClick()}
        }
    }

    fun getFilter(): Filter {
        if (subjectDataFilter == null) {
            subjectDataFilter = SubjectDataFilter()
        }
        return subjectDataFilter as SubjectDataFilter
    }

    fun setData(models: List<InboxHistory>) {
        if (models.isNotEmpty()) {
            this.mainModel.clear()
            this.tempModel.clear()

            this.mainModel = models as ArrayList<InboxHistory>
            this.tempModel = models
        } else {
            this.mainModel = models as ArrayList<InboxHistory>
            this.tempModel = models
        }
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<InboxHistory>) {
        this.mainModel.addAll(datas)
        this.tempModel.addAll(datas)
        notifyItemRangeInserted(this.tempModel.size, datas.size)
    }

    private inner class SubjectDataFilter : Filter() {
        @SuppressLint("DefaultLocale")
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            var charSequence = charSequence

            charSequence = charSequence.toString()
            val filterResults = FilterResults()

            if (!TextUtils.isEmpty(charSequence)) {
                val arrayList1 = java.util.ArrayList<InboxHistory>()
                Log.d("Lihat performFiltering SubjectDataFilter", charSequence)

                for (data in mainModel) {
                    if (data.toString().toLowerCase().contentEquals(charSequence.toString().toLowerCase())) {
                        arrayList1.add(data)
                    }
                }

                filterResults.count = arrayList1.size
                filterResults.values = arrayList1
            } else {
                synchronized(this) {
                    filterResults.count = mainModel.size
                    filterResults.values = mainModel
                }
            }
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {

            tempModel = filterResults.values as java.util.ArrayList<InboxHistory>

            notifyDataSetChanged()
        }
    }
}
