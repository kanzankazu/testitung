package com.kanzankazu.itungitungan.view.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kanzankazu.itungitungan.R


/**
 * Created by Herdi_WORK on 08.08.17.
 */

class SampleCrudCreateActivity : AppCompatActivity() {

    // variable yang merefers ke Firebase Realtime Database
    private var database: DatabaseReference? = null

    // variable fields EditText dan Button
    private var btSubmit: Button? = null
    private var etNama: EditText? = null
    private var etMerk: EditText? = null
    private var etHarga: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_create)

        // inisialisasi fields EditText dan Button
        etNama = findViewById<View>(R.id.et_namabarang) as EditText
        etMerk = findViewById<View>(R.id.et_merkbarang) as EditText
        etHarga = findViewById<View>(R.id.et_hargabarang) as EditText
        btSubmit = findViewById<View>(R.id.bt_submit) as Button

        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().reference

        val barang = intent.getSerializableExtra("data") as Barang

        if (barang != null) {
            etNama!!.setText(barang.nama)
            etMerk!!.setText(barang.merk)
            etHarga!!.setText(barang.harga)
            btSubmit!!.setOnClickListener {
                barang.nama = etNama!!.text.toString()
                barang.merk = etMerk!!.text.toString()
                barang.harga = etHarga!!.text.toString()

                updateBarang(barang)
            }
        } else {
            btSubmit!!.setOnClickListener {
                if (!isEmpty(etNama!!.text.toString()) && !isEmpty(etMerk!!.text.toString()) && !isEmpty(etHarga!!.text.toString()))
                    submitBarang(Barang(etNama!!.text.toString(), etMerk!!.text.toString(), etHarga!!.text.toString()))
                else
                    Snackbar.make(findViewById(R.id.bt_submit), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show()

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    etNama!!.windowToken, 0
                )
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
            }
    }

    private fun submitBarang(barang: Barang) {
        /**
         * Ini adalah kode yang digunakan untuk mengirimkan data ke Firebase Realtime Database
         * dan juga kita set onSuccessListener yang berisi kode yang akan dijalankan
         * ketika data berhasil ditambahkan
         */
        database!!.child("barang").push().setValue(barang).addOnSuccessListener(this) {
            etNama!!.setText("")
            etMerk!!.setText("")
            etHarga!!.setText("")
            Snackbar.make(findViewById(R.id.bt_submit), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {

        fun getActIntent(activity: Activity): Intent {
            // kode untuk pengambilan Intent
            return Intent(activity, SampleCrudCreateActivity::class.java)
        }
    }
}
