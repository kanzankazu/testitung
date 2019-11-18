package com.kanzankazu.itungitungan.view.sample;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Herdi_WORK on 18.06.17.
 */

@IgnoreExtraProperties
public class Barang implements Serializable {

    private String nama;
    private String merk;
    private String harga;
    private String key;

    public Barang(){

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    @Override
    public String toString() {
        return " "+nama+"\n" +
                " "+merk +"\n" +
                " "+harga;
    }

    public Barang(String nm, String mrk, String hrg){
        nama = nm;
        merk = mrk;
        harga = hrg;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nama", nama);
        result.put("merk", merk);
        result.put("harga", harga);
        result.put("key", key);

        return result;
    }
}
