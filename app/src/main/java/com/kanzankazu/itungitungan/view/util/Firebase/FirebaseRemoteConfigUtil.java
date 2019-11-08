package com.kanzankazu.itungitungan.view.util.Firebase;

import android.app.Activity;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kanzankazu.itungitungan.BuildConfig;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.view.SplashActivity;

import static com.kanzankazu.itungitungan.view.util.DialogUtil.initForceUpdateDialog;
import static com.kanzankazu.itungitungan.view.util.DialogUtil.initMaintenanceDialog;
import static com.kanzankazu.itungitungan.view.util.DialogUtil.initSuggestUpdateDialog;

public class FirebaseRemoteConfigUtil {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private int minVer;
    private int currVer;
    private boolean isMaintenance;
    private Activity mActivity;
    private FirebaseRemoteConfigOnNext firebaseRemoteConfigurationOnNext;
    private String versionName;
    private int versionCode;

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
            mFirebaseRemoteConfig.setDefaults(R.layout.activity_splash);

            mFirebaseRemoteConfig.fetch(3600)
                    .addOnCompleteListener(mActivity, task -> {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();

                            try {
                                isMaintenance = mFirebaseRemoteConfig.getBoolean(Constants.FirebaseRemoteConfig.IS_MAINTENANCE);
                                minVer = Integer.valueOf(mFirebaseRemoteConfig.getString(Constants.FirebaseRemoteConfig.MIN_VERSION));
                                currVer = Integer.valueOf(mFirebaseRemoteConfig.getString(Constants.FirebaseRemoteConfig.CURRENT_VERSION));

                                validationFirebaseRemoteConfig();
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

    private void validationFirebaseRemoteConfig() {
            int deviceVerCode = BuildConfig.VERSION_CODE;
            System.out.println("RC VALIDATION : " + deviceVerCode + " " + minVer + " " + currVer);

            if (BuildConfig.FLAVOR.equalsIgnoreCase("AdminLive")) {
                 firebaseRemoteConfigurationOnNext.onNextAction();
            } else {
                if (isMaintenance) {
                    initMaintenanceDialog(mActivity);
                } else if (deviceVerCode < minVer) {
                    System.out.println("RC FORCE : " + deviceVerCode + " < " + minVer);
                    initForceUpdateDialog(mActivity);
                } else if (deviceVerCode < currVer) {
                    if (mActivity instanceof SplashActivity) {
                        System.out.println("RC SUGGEST : " + deviceVerCode + " < " + currVer);
                        initSuggestUpdateDialog(mActivity, firebaseRemoteConfigurationOnNext);
                    } else {
                        firebaseRemoteConfigurationOnNext.onNextAction();
                    }
                } else {
                    firebaseRemoteConfigurationOnNext.onNextAction();
                }
            }
    }
}
