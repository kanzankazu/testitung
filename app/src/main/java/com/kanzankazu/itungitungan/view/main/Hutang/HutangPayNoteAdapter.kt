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
    private var datas: ArrayList<NoteModel> = arrayListOf()

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
        h.setListener(datas[position])
    }

    inner class HutangPayNoteAdapterHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        fun setView(model: NoteModel) {
            itemView.tv_item_suggest.text = model.title
            itemView.iv_item_suggest_clear.visibility = if (model.uId.isNotEmpty()) View.VISIBLE else View.GONE
        }

        fun setListener(model: NoteModel) {
            itemView.tv_item_suggest.setOnClickListener {
                mView.onSuggestItemClick(model.title)
            }

            itemView.iv_item_suggest_clear.setOnClickListener {
                mView.onSuggestItemRemoveClick(model)
            }
        }
    }

    fun setData(datas: List<NoteModel>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<NoteModel>
        } else {
            this.datas = datas as ArrayList<NoteModel>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<NoteModel>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<NoteModel>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addDataFirst(data: NoteModel) {
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

    fun restoreData(data: NoteModel, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: NoteModel, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }
}
