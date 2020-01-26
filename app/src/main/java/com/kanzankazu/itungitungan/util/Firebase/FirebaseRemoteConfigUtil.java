package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.view.SplashActivity;

public class FirebaseRemoteConfigUtil {

    private int minVer;
    private int currVer;
    private boolean isMaintenance;
    private Activity mActivity;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseRemoteConfigOnNext firebaseRemoteConfigurationOnNext;

    public FirebaseRemoteConfigUtil(Activity mActivity, FirebaseRemoteConfigOnNext firebaseRemoteConfigurationOnNext) {
        this.mActivity = mActivity;
        this.firebaseRemoteConfigurationOnNext = firebaseRemoteConfigurationOnNext;
    }

    public void setupFirebaseRemoteConfig() {
        try {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            mFirebaseRemoteConfig.setConfigSettings(configSettings);
            mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
            mFirebaseRemoteConfig.fetch(Constants.FETCH_FIREBASE).addOnCompleteListener(mActivity, task -> {
                if (task.isSuccessful()) {
                    try {
                        mFirebaseRemoteConfig.activateFetched();
                        isMaintenance = mFirebaseRemoteConfig.getBoolean(Constants.FirebaseRemoteConfig.IS_MAINTENANCE);
                        minVer = Integer.valueOf(mFirebaseRemoteConfig.getString(Constants.FirebaseRemoteConfig.MIN_VERSION));
                        currVer = Integer.valueOf(mFirebaseRemoteConfig.getString(Constants.FirebaseRemoteConfig.CURRENT_VERSION));
                        validationRemoteConfig();
                    } catch (NumberFormatException e) {
                        firebaseRemoteConfigurationOnNext.onNextAction();
                    }
                } else {
                    firebaseRemoteConfigurationOnNext.onNextAction();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validationRemoteConfig() {
        int deviceVerCode = BuildConfig.VERSION_CODE;
        Log.d("Lihat", "validationRemoteConfig FirebaseRemoteConfigUtil : " + deviceVerCode + " " + minVer + " " + currVer);
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            firebaseRemoteConfigurationOnNext.onNextAction();
        } else {
            if (isMaintenance) {
                initMaintenanceDialog(mActivity);
            } else if (deviceVerCode < minVer) {
                Log.d("Lihat", "validationRemoteConfig FirebaseRemoteConfigUtil : " + deviceVerCode + " < " + minVer);
                initForceUpdateDialog(mActivity);
            } else if (deviceVerCode < currVer) {
                if (mActivity instanceof SplashActivity) {
                    Log.d("Lihat", "validationRemoteConfig FirebaseRemoteConfigUtil : " + deviceVerCode + " < " + currVer);
                    initSuggestUpdateDialog(mActivity, firebaseRemoteConfigurationOnNext);
                } else {
                    firebaseRemoteConfigurationOnNext.onNextAction();
                }
            } else {
                firebaseRemoteConfigurationOnNext.onNextAction();
            }
        }
    }

    private static void initMaintenanceDialog(Activity mActivity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getString(R.string.title_maintenance));
        dialog.setPositiveButton(mActivity.getString(R.string.confirm_exit), (dialogInterface, i) -> {
            if (mActivity instanceof SplashActivity) {
                mActivity.finish();
            } else {
                mActivity.moveTaskToBack(true);
            }

            dialogInterface.dismiss();
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

        Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        posButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
    }

    private static void initForceUpdateDialog(Activity mActivity) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getString(R.string.title_force_update));

        dialog.setPositiveButton(mActivity.getString(R.string.confirm_update), (dialogInterface, i) -> {
            goToPackagePlayStore(mActivity);
            dialogInterface.dismiss();
            mActivity.finish();
        });

        dialog.setNegativeButton(mActivity.getString(R.string.confirm_exit), (dialogInterface, i) -> {
            if (mActivity instanceof SplashActivity) {
                mActivity.finish();
            } else {
                mActivity.moveTaskToBack(true);
            }

            dialogInterface.dismiss();
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

        Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        posButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
        negButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
    }

    private static void initSuggestUpdateDialog(Activity mActivity, FirebaseRemoteConfigOnNext firebaseRemoteConfigurationOnNext) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle(mActivity.getString(R.string.title_suggest_update));

        dialog.setPositiveButton(mActivity.getString(R.string.confirm_update), (dialogInterface, i) -> {
            goToPackagePlayStore(mActivity);
            dialogInterface.dismiss();
            firebaseRemoteConfigurationOnNext.onNextAction();
        });

        dialog.setNegativeButton(mActivity.getString(R.string.confirm_continue), (dialogInterface, i) -> {
            firebaseRemoteConfigurationOnNext.onNextAction();
            dialogInterface.dismiss();
        });

        AlertDialog alert = dialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();

        Button posButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button negButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        posButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
        negButton.setTextColor(ContextCompat.getColor(mActivity, R.color.color_red_A700));
    }

    private static void goToPackagePlayStore(Activity mActivity) {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + mActivity.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            mActivity.startActivity(myAppLinkToMarket);
            FirebaseAnalyticsUtil.getInstance().firebaseAppRateClickedEvent();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity, " unable to find market app", Toast.LENGTH_LONG).show();
            FirebaseAnalyticsUtil.getInstance().firebaseAppOpenPlayStoreErrorEvent();
        }
    }
}
