package com.kanzankazu.itungitungan.util.android;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class AndroidPermissionUtil {
    /*String[] perm = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    }; Example */

    public static String[] permCameraGallery = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    public static String[] permLocation = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public int RP_ACCESS = 123;
    private String[] permissions;
    private Activity activity;
    private boolean isPerpermissionForAllGranted = false;


    public AndroidPermissionUtil(Activity activity) {
        this.activity = activity;
    }

    public AndroidPermissionUtil(Activity activity, Boolean isInitCheckPermission, String... permissions) {
        this.activity = activity;
        this.permissions = permissions;
        if (isInitCheckPermission) checkPermission(permissions);
    }

    public boolean checkPermission(String... permissions) {
        this.permissions = permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> listStat = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
                    listStat.add("1");
                } else {
                    listStat.add("0");
                }
            }

            int frequency0 = Collections.frequency(listStat, "0");
            int frequency1 = Collections.frequency(listStat, "1");

            if (frequency1 != permissions.length) {
                ActivityCompat.requestPermissions(activity, permissions, RP_ACCESS);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * call when onRequestPermissionsResult
     *
     * @param isFinish     jika semua izin tidak di berikan akan keluar activity
     * @param listener
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public boolean onRequestPermissionsResult(boolean isFinish, AndroidPermissionUtilListener listener, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RP_ACCESS) {
            Log.d("Lihat", "checkResultPermission AndroidPermissionUtil : " + grantResults.length);
            Log.d("Lihat", "checkResultPermission AndroidPermissionUtil : " + permissions.length);
            ArrayList<String> listStat = new ArrayList<>();
            if (grantResults.length > 0 && permissions.length == grantResults.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        listStat.add("1");
                    } else {
                        listStat.add("0");
                    }
                }
            }

            int frequency0 = Collections.frequency(listStat, "0");
            int frequency1 = Collections.frequency(listStat, "1");

            if (frequency1 != grantResults.length) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("you denied some permission, you must give all permission to next proccess?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", (arg0, arg1) -> {
                    checkPermission(permissions);
                });
                alertDialogBuilder.setNegativeButton("No", (arg0, arg1) -> {
                    Snackbar.make(activity.findViewById(android.R.id.content), "Izin tidak di berikan", Snackbar.LENGTH_SHORT).show();
                    if (isFinish) activity.finish();
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                isPerpermissionForAllGranted = false;
                listener.onPermissionDenied("Izin tidak di berikan");
                return false;
            } else {
                listener.onPermissionGranted();
                isPerpermissionForAllGranted = true;
                return true;
            }
        } else {
            listener.onPermissionDenied("Izin tidak di berikan");
            isPerpermissionForAllGranted = false;
            return false;
        }
    }

    public String[] getCurrentpermission() {
        return permissions;
    }

    public boolean isPermissionGranted() {
        return isPerpermissionForAllGranted;
    }

    public interface AndroidPermissionUtilListener {
        void onPermissionGranted();

        void onPermissionDenied(String message);
    }
}
