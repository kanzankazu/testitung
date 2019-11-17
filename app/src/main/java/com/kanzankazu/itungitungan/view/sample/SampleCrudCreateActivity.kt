package com.kanzankazu.itungitungan.view.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.TextUtils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_sample_crud_create.*


/**
 * Created by Herdi_WORK on 08.08.17.
 */

class SampleCrudCreateActivity : BaseActivity() {

    // variable yang merefers ke Firebase Realtime Database
    private var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_create)

        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().reference

        val barang = intent.getSerializableExtra("data") as Barang?

        if (barang != null) {
            et_namabarang.setText(barang.nama)
            et_merkbarang.setText(barang.merk)
            et_hargabarang.setText(barang.harga)
            bt_submit.setOnClickListener {
                barang.nama = et_namabarang.text.toString()
                barang.merk = et_merkbarang.text.toString()
                barang.harga = et_hargabarang.text.toString()

                updateBarang(barang)
            }
        } else {
            bt_submit.setOnClickListener {
                if (!isEmpty(et_namabarang.text.toString()) && !isEmpty(et_merkbarang.text.toString()) && !isEmpty(et_hargabarang.text.toString()))
                    submitBarang(Barang(et_namabarang.text.toString(), et_merkbarang.text.toString(), et_hargabarang.text.toString()))
                else
                    Snackbar.make(findViewById(R.id.bt_submit), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show()

                hideKeyboard()
            }
        }
    }

    private fun isEmpty(s: String): Boolean {
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s)
    }

    private fun updateBarang(barang: Barang) {
        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database!!.child("barang") //akses parent index, ibaratnya seperti nama tabel
                .child(barang.key) //select barang berdasarkan key
                .setValue(barang) //set value barang yang baru
                .addOnSuccessListener(this) {
                    /**
                     * Baris kode yang akan dipanggil apabila proses update barang sukses
                     */
                    /**
                     * Baris kode yang akan dipanggil apabila proses update barang sukses
                     */
                    Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke") { finish() }.show()
                }.addOnFailureListener(this) {
                    showSnackbar(it.message)
                }
    }

    private fun submitBarang(barang: Barang) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database!!.child("barang").push().setValue(barang).addOnSuccessListener(this) {
            et_namabarang.setText("")
            et_merkbarang.setText("")
            et_hargabarang.setText("")
            Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show()
        }.addOnFailureListener(this) {
            showSnackbar(it.message)
        }
    }

    companion object {

        fun getActIntent(activity: Activity): Intent {
            // kode untuk pengambilan Intent
            return Intent(activity, SampleCrudCreateActivity::class.java)
        }
    }
}
