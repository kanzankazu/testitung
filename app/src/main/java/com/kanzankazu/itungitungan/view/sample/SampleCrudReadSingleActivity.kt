package com.kanzankazu.itungitungan.view.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.*
import com.kanzankazu.itungitungan.R
import com.kanzankazu.itungitungan.view.base.BaseActivity


/**
 * Created by Hafizh Herdi on 10/15/2017.
 */

class SampleCrudReadSingleActivity : BaseActivity() {

    private var barang: Barang? = null
    private lateinit var database: DatabaseReference
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

         barang = intent.getSerializableExtra("data") as Barang
        if (barang != null) {
            updateData(barang)
        }

        database = FirebaseDatabase.getInstance().reference

        database.child("barang").child(barang?.key).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Lihat", "onDataChange SampleCrudReadSingleActivity children " + dataSnapshot.children)
                Log.d("Lihat", "onDataChange SampleCrudReadSingleActivity key " + dataSnapshot.key)
                Log.d("Lihat", "onDataChange SampleCrudReadSingleActivity childrenCount " + dataSnapshot.childrenCount)
                Log.d("Lihat", "onDataChange SampleCrudReadSingleActivity priority " + dataSnapshot.priority)
                Log.d("Lihat", "onDataChange SampleCrudReadSingleActivity ref " + dataSnapshot.ref)
                Log.d("Lihat", "onDataChange SampleCrudReadSingleActivity value " + dataSnapshot.value)

                barang = dataSnapshot.getValue(Barang::class.java)
                updateData(barang)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Lihat", "onCancelled SampleCrudReadSingleActivity code " + databaseError.code)
                Log.d("Lihat", "onCancelled SampleCrudReadSingleActivity details " + databaseError.details)
                Log.d("Lihat", "onCancelled SampleCrudReadSingleActivity message " + databaseError.message)
            }
        })

        database.child("barang").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Lihat", "onDataChange addListenerForSingleValueEvent SampleCrudReadSingleActivity children " + dataSnapshot.children)
                Log.d("Lihat", "onDataChange addListenerForSingleValueEvent SampleCrudReadSingleActivity key " + dataSnapshot.key)
                Log.d("Lihat", "onDataChange addListenerForSingleValueEvent SampleCrudReadSingleActivity childrenCount " + dataSnapshot.childrenCount)
                Log.d("Lihat", "onDataChange addListenerForSingleValueEvent SampleCrudReadSingleActivity priority " + dataSnapshot.priority)
                Log.d("Lihat", "onDataChange addListenerForSingleValueEvent SampleCrudReadSingleActivity ref " + dataSnapshot.ref)
                Log.d("Lihat", "onDataChange addListenerForSingleValueEvent SampleCrudReadSingleActivity value " + dataSnapshot.value)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Lihat", "onCancelled addListenerForSingleValueEvent SampleCrudReadSingleActivity code " + databaseError.code)
                Log.d("Lihat", "onCancelled addListenerForSingleValueEvent SampleCrudReadSingleActivity details " + databaseError.details)
                Log.d("Lihat", "onCancelled addListenerForSingleValueEvent SampleCrudReadSingleActivity message " + databaseError.message)
            }
        })

        database.child("barang").removeEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("Lihat", "onDataChange removeEventListener SampleCrudReadSingleActivity children " + dataSnapshot.children)
                Log.d("Lihat", "onDataChange removeEventListener SampleCrudReadSingleActivity key " + dataSnapshot.key)
                Log.d("Lihat", "onDataChange removeEventListener SampleCrudReadSingleActivity childrenCount " + dataSnapshot.childrenCount)
                Log.d("Lihat", "onDataChange removeEventListener SampleCrudReadSingleActivity priority " + dataSnapshot.priority)
                Log.d("Lihat", "onDataChange removeEventListener SampleCrudReadSingleActivity ref " + dataSnapshot.ref)
                Log.d("Lihat", "onDataChange removeEventListener SampleCrudReadSingleActivity value " + dataSnapshot.value)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Lihat", "onCancelled removeEventListener SampleCrudReadSingleActivity code " + databaseError.code)
                Log.d("Lihat", "onCancelled removeEventListener SampleCrudReadSingleActivity details " + databaseError.details)
                Log.d("Lihat", "onCancelled removeEventListener SampleCrudReadSingleActivity message " + databaseError.message)
            }
        })

        /*database.child("barang").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                Log.d("Lihat", "onCancelled SampleCrudReadSingleActivity ${dataSnapshot.code}")
                Log.d("Lihat", "onCancelled SampleCrudReadSingleActivity ${dataSnapshot.details}")
                Log.d("Lihat", "onCancelled SampleCrudReadSingleActivity ${dataSnapshot.message}")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, key: String?) {
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity ${dataSnapshot.children}")
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity ${dataSnapshot.key}")
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity ${dataSnapshot.childrenCount}")
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity ${dataSnapshot.priority}")
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity ${dataSnapshot.ref}")
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity ${dataSnapshot.value}")
                Log.d("Lihat", "onChildMoved SampleCrudReadSingleActivity $key")
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, key: String?) {
                Log.d("Lihat", "onChildChanged SampleCrudReadSingleActivity $dataSnapshot")
                Log.d("Lihat", "onChildChanged SampleCrudReadSingleActivity $key")
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, key: String?) {
                Log.d("Lihat", "onChildAdded SampleCrudReadSingleActivity $dataSnapshot")
                Log.d("Lihat", "onChildAdded SampleCrudReadSingleActivity $key")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("Lihat", "onChildRemoved SampleCrudReadSingleActivity" + dataSnapshot.children)
                startActivity(Intent(this@SampleCrudReadSingleActivity, SampleCrudReadActivity::class.java))
            }
        })*/
    }

    private fun updateData(barang: Barang?) {
        etNama.setText(barang?.nama)
        etMerk.setText(barang?.merk)
        etHarga.setText(barang?.harga)
    }

    companion object {

        fun getActIntent(activity: Activity): Intent {
            return Intent(activity, SampleCrudReadSingleActivity::class.java)
        }
    }
}
