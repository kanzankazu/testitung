package com.kanzankazu.itungitungan;

import android.Manifest;
import org.jetbrains.annotations.NotNull;

public class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String PACKAGE_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_TWITTER = "com.twitter.android";
    public static final String PACKAGE_INSTAGRAM = "com.instagram.android";
    public static final String FACEBOOK_PAGE_ID = "158637887922970";
    public static final String FACEBOOK_NEW_VERSION_BASE_URL = "fb://facewebmodal/f?href=";
    public static final String FACEBOOK_OLD_VERSION_BASE_URL = "fb://page/";

    public interface SharedPreference {
        String EMAIL = "EMAIL";
        String NAME = "NAME";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String LOGIN_STATUS = "LOGIN_STATUS";
        String FCM_TOKEN = "FCM_TOKEN";
        String UID = "UID";
    }

    public interface LogInStatus {
        String LOGIN = "LOGIN";
        String NOT_LOGIN = "NOT_LOGIN";
        String GUEST = "GUEST";
    }

    public interface FirebaseRemoteConfig {
        String IS_MAINTENANCE = "is_maintenance";
        String CURRENT_VERSION = "current_version";
        String MIN_VERSION = "min_version";
        String TEST_AB = "test_ab";
        String WELCOME_MESSAGE = "welcome_message";
        String IS_LOAN = "show_loan";
        String IS_INSPECTION = "show_inspection";
        String IS_BUY = "show_buy";
        String IS_SELL = "show_sell";
    }

    public interface BUNDLE {

    }

    public interface EXTRA {
    }

    public interface DATABASE_FIREBASE {
        interface TABLE {
            String USER = "user";
        }

        interface ROW {
            String EMAIL = "email";
            String TOKEN_FCM = "tokenFcm";
            String UID = "uid";
        }
    }
}