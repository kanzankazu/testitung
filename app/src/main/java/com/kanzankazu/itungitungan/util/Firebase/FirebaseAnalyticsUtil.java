package com.kanzankazu.itungitungan.util.Firebase;

import android.os.Bundle;

import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.MyApplication;
import com.kanzankazu.itungitungan.UserPreference;

public class FirebaseAnalyticsUtil {

    public static FirebaseAnalyticsUtil mInstance;

    public interface param {
        String ID = "id";
        String ACTIVITY = "mActivity";
    }

    public interface event {
        String CLICKED_RATE = "rate_clicked";
        String OPEN_PLAYSTORE_ERROR = "open_playstore_error";
        String DIALOG_ACTIVITY = "dialog_activity";
        String REMOTE_CONFIG_ERROR = "remote_config_error";
    }

    public interface screen {
    }

    public static synchronized FirebaseAnalyticsUtil getInstance() {
        if (mInstance == null) new FirebaseAnalyticsUtil();
        return mInstance;
    }

    /**
     * Constructor init
     */
    private FirebaseAnalyticsUtil() {
        mInstance = this;
        if (MyApplication.getInstance().getmFirebaseAnalytics() == null)
            MyApplication.getInstance().setmFirebaseAnalytics();
    }

    public void getUserPropertyAndId() {
        String email = UserPreference.getInstance().getEmail();
        if (!email.equals("")) {
            MyApplication.getInstance().getmFirebaseAnalytics().setUserId(email);
        }
    }

    public void trackEvent(String pageName) {
        Bundle bundle = new Bundle();

        getUserPropertyAndId();
        MyApplication.getInstance().getmFirebaseAnalytics().logEvent(pageName, bundle);
    }

    public void trackEventWithParameter(String pageName, Bundle bundle) {
        getUserPropertyAndId();
        MyApplication.getInstance().getmFirebaseAnalytics().logEvent(pageName, bundle);
    }

    public void firebaseDialogActivityEvent(String activity) {
        Bundle bundle = new Bundle();

        getUserPropertyAndId();
        bundle.putString(param.ACTIVITY, activity);

        MyApplication.getInstance().getmFirebaseAnalytics().logEvent(event.DIALOG_ACTIVITY, bundle);
    }

    public void firebaseAppRateClickedEvent() {
        Bundle bundle = new Bundle();

        if (UserPreference.getInstance().getLoginStatus().equals(Constants.LogInStatus.LOGIN)) {

            getUserPropertyAndId();
            MyApplication.getInstance().getmFirebaseAnalytics().logEvent(event.CLICKED_RATE, bundle);
        }
    }

    public void firebaseAppOpenPlayStoreErrorEvent() {
        Bundle bundle = new Bundle();

        if (UserPreference.getInstance().getLoginStatus().equals(Constants.LogInStatus.LOGIN)) {

            getUserPropertyAndId();
            MyApplication.getInstance().getmFirebaseAnalytics().logEvent(event.OPEN_PLAYSTORE_ERROR, bundle);
        }
    }

    public void firebaseRemoteConfigError() {
        Bundle bundle = new Bundle();

        getUserPropertyAndId();
        MyApplication.getInstance().getmFirebaseAnalytics().logEvent(event.REMOTE_CONFIG_ERROR, bundle);
    }
}


