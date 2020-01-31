package com.kanzankazu.itungitungan;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;

public class UserPreference {
    public static final String PREFS_NAME = "SHARED_PREF";
    private SharedPreferences sharedPreferences;
    private static UserPreference userPreference;

    GoogleSignInAccount googleSignInAccount = null;

    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
    }

    private UserPreference(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static UserPreference getInstance(Context context) {
        if (userPreference == null) {
            userPreference = new UserPreference(context.getApplicationContext());
        }
        return userPreference;
    }

    public static UserPreference getInstance() {
        if (userPreference != null) {
            return userPreference;
        } else {
            return getInstance(MyApplication.getInstance());
        }
    }

    /**
     * Preference Helper
     */
    public void removeSharedPrefByKey(String key) {
        this.sharedPreferences.edit().remove(key).apply();
    }

    public boolean isContainKey(String key) {
        return this.sharedPreferences.contains(key);
    }

    public void signout() {
        //this.sharedPreferences.edit().clear().apply();
        setEmail(null);
        setName(null);
        setUid(null);
        setAccessToken(null);
        setEmail(null);
        setName(null);
        setUid(null);
        setAccessToken(null);
        setFCMToken(null);
        setLoginStatus(null);

        setIsFromNotification(false);
        setIsLogin(false);
        setIsOtp(false);
    }

    /**
     * Preference Editor
     */
    private void putSharedPrefString(String key, String value) {
        this.sharedPreferences.edit().putString(key, value).apply();
    }

    private String getSharedPrefString(String key) {
        return this.sharedPreferences.getString(key, "");
    }

    private void putSharedPrefInt(String key, int value) {
        this.sharedPreferences.edit().putInt(key, value).apply();
    }

    private int getSharedPrefInt(String key) {
        return this.sharedPreferences.getInt(key, 0);
    }

    private void putSharedPrefLong(String key, long value) {
        this.sharedPreferences.edit().putLong(key, value).apply();
    }

    private Long getSharedPrefLong(String key) {
        return this.sharedPreferences.getLong(key, 0);
    }

    private void putSharedPrefBoolean(String key, Boolean value) {
        this.sharedPreferences.edit().putBoolean(key, value).apply();
    }

    private Boolean getSharedPrefBoolean(String key) {
        return this.sharedPreferences.getBoolean(key, false);
    }

    private void putSharedPrefFloat(String key, Float value) {
        this.sharedPreferences.edit().putFloat(key, value).commit();
    }

    private float getSharedPrefFloat(String key) {
        return this.sharedPreferences.getFloat(key, 0);
    }

    private void putSharedPrefStringArray(String key, List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            putSharedPrefString(key + "_" + Integer.toString(i), values.get(i));
        }
        putSharedPrefInt(key + "_size", values.size());
    }

    private ArrayList<String> getSharedPrefStringArray(String key) {
        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < getSharedPrefInt(key + "_size"); i++) {
            values.add(getSharedPrefString(key + "_" + Integer.toString(i)));
        }
        return values;
    }

    public void setIsFromNotification(Boolean isFromNotification) {
        putSharedPrefBoolean(Constants.SharedPreference.FROM_NOTIFICATION, isFromNotification);
    }

    public Boolean getIsFromNotification() {
        return getSharedPrefBoolean(Constants.SharedPreference.FROM_NOTIFICATION);
    }

    /**
     * Preference Dictionary
     */
    public String getEmail() {
        return getSharedPrefString(Constants.SharedPreference.EMAIL);
    }

    public void setEmail(String email) {
        putSharedPrefString(Constants.SharedPreference.EMAIL, email);
    }

    public String getName() {
        return getSharedPrefString(Constants.SharedPreference.NAME);
    }

    public void setName(String name) {
        putSharedPrefString(Constants.SharedPreference.NAME, name);
    }

    public String getUid() {
        return getSharedPrefString(Constants.SharedPreference.UID);
    }

    public void setUid(String uid) {
        putSharedPrefString(Constants.SharedPreference.UID, uid);
    }

    public String getAccessToken() {
        return getSharedPrefString(Constants.SharedPreference.ACCESS_TOKEN);
    }

    public void setAccessToken(String accessToken) {
        putSharedPrefString(Constants.SharedPreference.ACCESS_TOKEN, accessToken);
    }

    public String getFCMToken() {
        return getSharedPrefString(Constants.SharedPreference.FCM_TOKEN);
    }

    public void setFCMToken(String fcmToken) {
        putSharedPrefString(Constants.SharedPreference.FCM_TOKEN, fcmToken);
    }

    public String getLoginStatus() {
        return getSharedPrefString(Constants.SharedPreference.LOGIN_STATUS);
    }

    public void setLoginStatus(String loginStatus) {
        putSharedPrefString(Constants.SharedPreference.LOGIN_STATUS, loginStatus);
    }

    public void setLoginGuestStatus() {
        putSharedPrefString(Constants.SharedPreference.LOGIN_STATUS, Constants.LogInStatus.GUEST);
    }

    public void setIsLogin(Boolean isLogin) {
        putSharedPrefBoolean(Constants.SharedPreference.LOGIN, isLogin);
    }

    public Boolean getIsLogin() {
        return getSharedPrefBoolean(Constants.SharedPreference.LOGIN);
    }

    public void setIsOtp(Boolean otpStatus) {
        putSharedPrefBoolean(Constants.SharedPreference.OTP_STATUS, otpStatus);
    }

    public Boolean getIsOtp() {
        return getSharedPrefBoolean(Constants.SharedPreference.OTP_STATUS);
    }


}
