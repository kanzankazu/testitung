package com.kanzankazu.itungitungan.view.util.Facebook;

import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.kanzankazu.itungitungan.MyApplication;

public class FacebookAnalyticsUtil {
    public static FacebookAnalyticsUtil mInstance;

    private FacebookAnalyticsUtil() {
        mInstance = this;
        if (MyApplication.getInstance().getmAppEventsLogger() == null)
            MyApplication.getInstance().setmAppEventsLogger();
    }

    public static synchronized FacebookAnalyticsUtil getInstance() {
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        FacebookSdk.setIsDebugEnabled(true);
        if (mInstance == null) new FacebookAnalyticsUtil();
        return mInstance;
    }

    public void logEvent(String eventName) {
        MyApplication.getInstance().getmAppEventsLogger().logEvent(eventName);
    }

    public void logEventWithBundle(String eventName, Bundle params) {
        MyApplication.getInstance().getmAppEventsLogger().logEvent(eventName, params);
    }
}
