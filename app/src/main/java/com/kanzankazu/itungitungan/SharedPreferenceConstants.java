package com.kanzankazu.itungitungan;

/**
 * Created by Faisal Bahri on 2019-12-04.
 */
class SharedPreferenceConstants {
    public interface PreferenceKey {
        String EMAIL = "EMAIL";
        String PASSWORD = "PASSWORD";
        String NAME = "NAME";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String LOGIN_STATUS = "LOGIN_STATUS";
        String LOGIN = "LOGIN";
        String FCM_TOKEN = "FCM_TOKEN";
        String DISPLAY_PICT = "DISPLAY_PICT";
        String PHONE_NUMBER = "PHONE_NUMBER";
        String FS_CODE = "FS_CODE";
        String FROM_NOTIFICATION = "FROM_NOTIFICATION";
    }

    public interface LoginStatus {
        String LOGIN = "LOGIN";
        String NOT_LOGIN = "NOT_LOGIN";
    }
}
