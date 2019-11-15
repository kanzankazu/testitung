package com.kanzankazu.itungitungan.util.Firebase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Faisal Bahri on 2019-11-15.
 */
public class FirebaseConnectionUtil {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    String checkException(int statusCode) {
        switch (statusCode) {
            /*The operation was successful, but was used the device's cache. If this is a write, the data will be written when the device is online; errors will be written to the logs. If this is a read, the data was read from a device cache and may be stale.*/
            case -1:
                return "SUCCESS_CACHE";
            /*The operation was successful.*/
            case 0:
                return "SUCCESS";
            case 2:
                return "SERVICE_VERSION_UPDATE_REQUIRED";
            case 3:
                return "SERVICE_DISABLED";
            case 4:
                return "SIGN_IN_REQUIRED";
            case 5:
                return "INVALID_ACCOUNT";
            case 6:
                return "RESOLUTION_REQUIRED";
            case 7:
                return "NETWORK_ERROR";
            case 8:
                return "INTERNAL_ERROR";
            case 10:
                return "DEVELOPER_ERROR";
            case 13:
                return "ERROR";
            case 14:
                return "INTERRUPTED";
            case 15:
                return "TIMEOUT";
            case 16:
                return "CANCELED";
            case 17:
                return "API_NOT_CONNECTED";
            default:
                return "";
        }

    }
}
