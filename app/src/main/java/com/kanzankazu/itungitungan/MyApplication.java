package com.kanzankazu.itungitungan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseConnectionHandler;

import io.fabric.sdk.android.Fabric;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static MyApplication mInstance;
    public Activity activity;
    private FirebaseAnalytics mFirebaseAnalytics;
    private AppEventsLogger mAppEventsLogger;
    private String identifierCrashlytics = Constants.LogInStatus.GUEST;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();

        /*FACEBOOK START*/
       FacebookSdk.sdkInitialize(getApplicationContext());
       AppEventsLogger.activateApp(this);
        /*FACEBOOK END*/

        //setupCalligraphy();
        printKeyHash();
        Fabric.with(this, new Crashlytics());

        if (!UserPreference.getInstance().getEmail().equals("")) {
            identifierCrashlytics = UserPreference.getInstance().getEmail();
        }

        // Manage firebase database connection when in background and foreground
        registerActivityLifecycleCallbacks(new FirebaseDatabaseConnectionHandler());

        byte[] encrpt = new byte[0];
        encrpt = identifierCrashlytics.getBytes(StandardCharsets.UTF_8);

        String base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);

        Crashlytics.setUserIdentifier(base64);

        setmFirebaseAnalytics();
        setmAppEventsLogger();
        setupPRDownloader();

        System.out.println("identifier : " + identifierCrashlytics);
        System.out.println("identifier base64 : " + base64);
        System.out.println("ACCESS_TOKEN : " + UserPreference.getInstance().getAccessToken());

        //byte[] decrypt= Base64.decode(base64, Base64.DEFAULT);
        //try {
        //    String text = new String(decrypt, "UTF-8");
        //    System.out.println("identifier decrypt : "+text);
        //} catch (UnsupportedEncodingException e) {
        //    e.printStackTrace();
        //}
    }

    public static Context getContext() {
        return context;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (isKitkatBelow()) {
            MultiDex.install(this);
        }
    }

    private void setupPRDownloader() {
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }

    public void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getResources().getString(R.string.package_name), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KEY_HASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * FirebaseDatabase ANALYTICS
     */
    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    public void setmFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    /**
     * FACEBOOK ANALYTICs
     */
    public AppEventsLogger getmAppEventsLogger() {
        return mAppEventsLogger;
    }

    public void setmAppEventsLogger() {
        mAppEventsLogger = mAppEventsLogger.newLogger(this);
    }

    public boolean isKitkatBelow() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }
}
