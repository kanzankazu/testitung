package com.kanzankazu.itungitungan.view.adapter

import android.app.Activity
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.util.InputValidUtil
import com.kanzankazu.itungitungan.util.widget.gallery2.ImageModel
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_image_add.view.*
import java.io.File

class ImageListAdapter(val mActivity: Activity, val mView: ImageListContract) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: MutableList<ImageModel> = mutableListOf()
    private var removeDatas: MutableList<ImageModel> = mutableListOf()

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
                h.setView()
                h.setOnClickListener(datas[position], position)
            }
        }

    }

    fun isNotEmptyData(): Boolean {
        return datas.size != 0
    }

    fun getDatas(): MutableList<ImageModel> {
        return datas
    }

    fun getData(position: Int): ImageModel {
        return datas[position]
    }

    fun getDatasStringAll(): MutableList<String> {
        val imagePaths = mutableListOf<String>()
        for (models in datas) {
            imagePaths.add(models.path!!)
        }
        return imagePaths
    }

    fun getDatasString(isUrl: Boolean): MutableList<String> {
        val imagePaths = mutableListOf<String>()
        for (models in datas) {
            if (isUrl) {
                if (InputValidUtil.isLinkUrl(models.path))
                    imagePaths.add(models.path!!)
            } else {
                if (!InputValidUtil.isLinkUrl(models.path))
                    imagePaths.add(models.path!!)
            }
        }
        return imagePaths
    }

    fun getDatasUri(): MutableList<Uri> {
        val imagePaths = mutableListOf<Uri>()
        for (models in datas) {
            if (!InputValidUtil.isLinkUrl(models.path))
                imagePaths.add(Uri.fromFile(File(models.path!!)))
        }
        return imagePaths
    }

    fun getRemoveDataString(isUrl: Boolean): MutableList<String> {
        val imagePaths = mutableListOf<String>()
        for (models in removeDatas) {
            if (isUrl) {
                if (InputValidUtil.isLinkUrl(models.path))
                    imagePaths.add(models.path!!)
            } else {
                if (!InputValidUtil.isLinkUrl(models.path))
                    imagePaths.add(models.path!!)
            }
        }
        return imagePaths
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
        fun setView() {}

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
        notifyDataSetChanged()
    }

    fun addDataAt(data: ImageModel, pos: Int) {
        this.datas.add(pos, data)
        notifyItemInserted(pos)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        this.removeDatas.add(this.datas[position])
        this.datas.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.datas.size)
    }

    fun removeDataFirst() {
        val position = 0
        this.removeDatas.add(this.datas[position])
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
