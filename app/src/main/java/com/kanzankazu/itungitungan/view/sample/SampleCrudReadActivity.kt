package com.kanzankazu.itungitungan.view.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import com.kanzankazu.itungitungan.R
import java.util.*

/**
 * Created by Herdi_WORK on 18.06.17.
 */

class SampleCrudReadActivity : AppCompatActivity(), AdapterSampleCrudRecyclerView.FirebaseDataListener {

    /**
     * Mendefinisikan variable yang akan dipakai
     */
    private var database: DatabaseReference? = null
    private var rvView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var daftarBarang: ArrayList<Barang>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Mengeset layout
         */
        setContentView(R.layout.activity_sample_crud_read)

        /**
         * Inisialisasi RecyclerView & komponennya
         */
        rvView = findViewById<View>(R.id.rv_main) as RecyclerView
        rvView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        rvView!!.layoutManager = layoutManager

        /**
         * Inisialisasi dan mengambil Firebase Database Reference
         */
        database = FirebaseDatabase.getInstance().reference

        /**
         * Mengambil data barang dari Firebase Realtime DB
         */
        database!!.child("barang").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarBarang = ArrayList()
                for (noteDataSnapshot in dataSnapshot.children) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Barang
                     * Dan juga menyimpan primary key pada object Barang
                     * untuk keperluan Edit dan Delete data
                     */
                    val barang = noteDataSnapshot.getValue(Barang::class.java)
                    barang!!.key = noteDataSnapshot.key

                    /**
                     * Menambahkan object Barang yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarBarang!!.add(barang)
                }

                /**
                 * Inisialisasi adapter dan data barang dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                adapter = AdapterSampleCrudRecyclerView(daftarBarang!!, this@SampleCrudReadActivity)
                rvView!!.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                println(databaseError.details + " " + databaseError.message)
            }
        })
    }

    override fun onDeleteData(barang: Barang, position: Int) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key barang.
         * Jika sukses akan memunculkan SnackBar
         */
        if (database != null) {
            database!!.child("barang").child(barang.key).removeValue().addOnSuccessListener { Toast.makeText(this@SampleCrudReadActivity, "success delete", Toast.LENGTH_LONG).show() }

        }
    }
}
