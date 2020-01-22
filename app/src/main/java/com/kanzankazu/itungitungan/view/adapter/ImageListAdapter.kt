package com.kanzankazu.itungitungan.view.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_image_add.view.*

class ImageListAdapter(val mActivity: Activity, val mView: ImageListContract) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var datas: MutableList<ImageModel> = mutableListOf()

    companion object {
        const val IMAGE_TYPE = 0
        const val IMAGE_OTHER_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            datas[position].type.equals("other", true) -> IMAGE_OTHER_TYPE
            else -> IMAGE_TYPE
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        return when (p1) {
            IMAGE_TYPE -> ImageViewHolder(inflater.inflate(R.layout.item_image, p0, false))
            else -> ImageAddViewHolder(inflater.inflate(R.layout.item_image_add, p0, false))
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            IMAGE_TYPE -> {
                val h = p0 as ImageViewHolder
                h.setView(datas[position])
                h.setOnClickListener(datas[position], position)
            }
            else -> {
                val h = p0 as ImageAddViewHolder
                h.setView(datas[position])
                h.setOnClickListener(datas[position], position)
            }
        }

    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setView(data: ImageModel) {
            Glide.with(mActivity).load(data.path).into(itemView.civ_item_image)
        }

        fun setOnClickListener(data: ImageModel, position: Int) {
            itemView.civ_item_image_remove.setOnClickListener {
                mView.onImageListRemove(data, position)
            }
        }

    }

    inner class ImageAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setView(data: ImageModel) {}

        fun setOnClickListener(data: ImageModel, position: Int) {
            itemView.civ_item_image_add.setOnClickListener {
                mView.onImageListAdd(data, position)
            }
        }

    }

    interface ImageListContract {
        fun onImageListRemove(data: ImageModel, position: Int)
        fun onImageListAdd(data: ImageModel, position: Int)
    }

    fun getItemData(position: Int): ImageModel {
        return datas[position]
    }

    fun getItemDatas(): MutableList<ImageModel> {
        return datas
    }

    fun setData(datas: List<ImageModel>) {
        if (datas.isNotEmpty()) {
            this.datas.clear()
            this.datas = datas as ArrayList<ImageModel>
        } else {
            this.datas = datas as ArrayList<ImageModel>
        }
        notifyDataSetChanged()
    }

    fun replaceData(datas: List<ImageModel>) {
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun addDatas(datas: List<ImageModel>) {
        this.datas.addAll(datas)
        notifyItemRangeInserted(this.datas.size, datas.size)
    }

    fun addData(data: ImageModel) {
        this.datas.add(data)
        notifyDataSetChanged()
    }

    fun addDataFirst(data: ImageModel) {
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

    fun restoreData(data: ImageModel, position: Int) {
        this.datas.add(position, data)
        notifyItemInserted(position)
    }

    fun updateSingleData(data: ImageModel, position: Int) {
        this.datas.set(position, data)
        notifyDataSetChanged()
    }

}
