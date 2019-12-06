package com.kanzankazu.itungitungan.util.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
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

    private static final int RP_ACCESS = 123;
    private final String[] permissions;
    private Activity activity;

    public AndroidPermissionUtil(Activity activity, String... permissions) {
        this.activity = activity;
        this.permissions = permissions;
        checkPermission(permissions);
    }

    public boolean checkPermission(String[] permissions) {
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
            }
        }
        return true;
    }

    /**
     * call when onRequestPermissionsResult
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public boolean checkResultPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RP_ACCESS) {
            Log.d("Lihat", "checkResultPermission AndroidPermissionUtil : " + grantResults.length);
            Log.d("Lihat", "checkResultPermission AndroidPermissionUtil : " + permissions.length);
            boolean isPerpermissionForAllGranted = false;
            ArrayList<String> listStat = new ArrayList<>();
            if (grantResults.length > 0 && permissions.length == grantResults.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        isPerpermissionForAllGranted = true;
                        listStat.add("1");
                    } else {
                        isPerpermissionForAllGranted = false;
                        listStat.add("0");
                    }
                }
            } else {
                isPerpermissionForAllGranted = true;
            }

            int frequency0 = Collections.frequency(listStat, "0");
            int frequency1 = Collections.frequency(listStat, "1");

            if (frequency1 != grantResults.length) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("you denied some permission, you must give all permission to next proccess?");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        checkPermission(permissions);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        activity.finish();
                        //SystemUtil.showToast(activity, "Izin tidak di berikan", Toast.LENGTH_LONG, Gravity.TOP);
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public String[] getCurrentpermission(){
        return permissions;
    }
}
