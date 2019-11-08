package com.kanzankazu.itungitungan.view.sample

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.kanzankazu.itungitungan.R

import java.util.ArrayList


/**
 * Created by Hafizh Herdi on 10/8/2017.
 */

class AdapterSampleCrudRecyclerView(private val daftarBarang: ArrayList<Barang>, private val context: Context) : RecyclerView.Adapter<AdapterSampleCrudRecyclerView.ViewHolder>() {

    private var listener: FirebaseDataListener = context as SampleCrudReadActivity

    /**
     * Inisiasi data dan variabel yang akan digunakan
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        /**
         * Inisiasi View
         * Di tutorial ini kita hanya menggunakan data String untuk tiap item
         * dan juga view nya hanyalah satu TextView
         */
        var tvTitle: TextView = v.findViewById<View>(R.id.tv_namabarang) as TextView
        var cvMain: CardView = v.findViewById<View>(R.id.cv_main) as CardView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /**
         * Inisiasi ViewHolder
         */
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /**
         * Menampilkan data pada view
         */
        val name = daftarBarang[position].nama
        println("BARANG DATA one by one " + position + daftarBarang.size)
        holder.cvMain.setOnClickListener {
            /**
             * Kodingan untuk tutorial Selanjutnya :p Read detail data
             */
            /**
             * Kodingan untuk tutorial Selanjutnya :p Read detail data
             */
            context.startActivity(SampleCrudReadSingleActivity.getActIntent(context as Activity).putExtra("data", daftarBarang[position]))
        }
        holder.cvMain.setOnLongClickListener {
            /**
             * Kodingan untuk tutorial Selanjutnya :p Delete dan update data
             */
            /**
             * Kodingan untuk tutorial Selanjutnya :p Delete dan update data
             */
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_view)
            dialog.setTitle("Pilih Aksi")
            dialog.show()

            val editButton = dialog.findViewById<View>(R.id.bt_edit_data) as Button
            val delButton = dialog.findViewById<View>(R.id.bt_delete_data) as Button

            //apabila tombol edit diklik
            editButton.setOnClickListener {
                dialog.dismiss()
                context.startActivity(SampleCrudCreateActivity.getActIntent(context as Activity).putExtra("data", daftarBarang[position]))
            }

            //apabila tombol delete diklik
            delButton.setOnClickListener {
                dialog.dismiss()
                listener.onDeleteData(daftarBarang[position], position)
            }
            true
        }
        holder.tvTitle.text = name
    }

    override fun getItemCount(): Int {
        /**
         * Mengembalikan jumlah item pada barang
         */
        return daftarBarang.size
    }

    interface FirebaseDataListener {
        fun onDeleteData(barang: Barang, position: Int)
    }

}
