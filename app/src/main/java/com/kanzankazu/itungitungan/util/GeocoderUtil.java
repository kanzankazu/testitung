package com.kanzankazu.itungitungan.util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocoderUtil {

    public static String getAddress(Activity activity, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            return "error - geoccoder not found";
        }

    }

    public static String getAddressComplete(Activity activity, double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                Log.d("Lihat", "getCompleteAddressString GeocoderUtil : " + returnedAddress);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Toast.makeText(activity, "Tidak bisa mendapatkan alamat, Silahkan klik ulang kembali", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Tidak bisa mendapatkan alamat, Silahkan klik ulang kembali", Toast.LENGTH_SHORT).show();
        }
        return strAdd;
    }

    public static double convertLatLng(TextView textView) {
        return Double.parseDouble(textView.getText().toString());
    }

    public static double convertLatLng(EditText editText) {
        return Double.parseDouble(editText.getText().toString());
    }

}
