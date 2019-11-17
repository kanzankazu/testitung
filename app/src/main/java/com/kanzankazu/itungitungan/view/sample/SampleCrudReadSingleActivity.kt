package com.kanzankazu.itungitungan.view.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText

import com.kanzankazu.itungitungan.R

/**
 * Created by Hafizh Herdi on 10/15/2017.
 */

class SampleCrudReadSingleActivity : AppCompatActivity() {

    private lateinit var btSubmit: Button
    private lateinit var etHarga: EditText
    private lateinit var etMerk: EditText
    private lateinit var etNama: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_crud_create)
        etNama = findViewById<View>(R.id.et_namabarang) as EditText
        etMerk = findViewById<View>(R.id.et_merkbarang) as EditText
        etHarga = findViewById<View>(R.id.et_hargabarang) as EditText
        btSubmit = findViewById<View>(R.id.bt_submit) as Button

        etNama.isEnabled = false
        etMerk.isEnabled = false
        etHarga.isEnabled = false
        btSubmit.visibility = View.GONE

        val barang = intent.getSerializableExtra("data") as Barang?
        if (barang != null) {
            etNama.setText(barang.nama)
            etMerk.setText(barang.merk)
            etHarga.setText(barang.harga)
        }
    }

    companion object {

        fun getActIntent(activity: Activity): Intent {
            return Intent(activity, SampleCrudReadSingleActivity::class.java)
        }
    }
}
