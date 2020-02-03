package com.kanzankazu.itungitungan;

public class Constants {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String PACKAGE_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_TWITTER = "com.twitter.android";
    public static final String PACKAGE_INSTAGRAM = "com.instagram.android";
    public static final String FACEBOOK_PAGE_ID = "158637887922970";
    public static final String FACEBOOK_NEW_VERSION_BASE_URL = "fb://facewebmodal/f?href=";
    public static final String FACEBOOK_OLD_VERSION_BASE_URL = "fb://page/";
    public static final long FETCH_FIREBASE = 3600;

    public interface SharedPreference {
        String EMAIL = "EMAIL";
        String NAME = "NAME";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String LOGIN_STATUS = "LOGIN_STATUS";
        String LOGIN = "LOGIN";
        String FCM_TOKEN = "FCM_TOKEN";
        String UID = "UID";
        String FROM_NOTIFICATION = "FROM_NOTIFICATION";
        String OTP_STATUS = "OTP_STATUS";
    }

    public interface LogInStatus {
        String LOGIN = "LOGIN";
        String NOT_LOGIN = "NOT_LOGIN";
        String GUEST = "GUEST";
    }

    public interface Bundle {
        String HUTANG = "hutang";
        String HUTANG_NEW = "hutang_new";
    }

    public interface FirebaseRemoteConfig {
        String IS_MAINTENANCE = "is_maintenance";
        String CURRENT_VERSION = "current_version";
        String MIN_VERSION = "min_version";

    }

    public interface FirebaseDatabase {
        interface TABLE {
            String USER = "user";
            String HUTANG = "hutang";
        }

        interface ROW {
            String EMAIL = "email";
            String TOKEN_FCM = "tokenFcm";
            String PHONE = "phone";
            String UID = "uid";
            String HID = "hid";
            String PIUHUTANGID = "debtorCreditorId";
        }
    }

    public interface HOME {
        String Anggaran = "Anggaran";
        String Arisan = "Arisan";
        String Banding = "Banding harga";
        String Hutang = "Hutang";
        String Keuangan = "Keuangan";
        String Stok = "Stok Barang";
    }

    public interface Hutang {

        interface Installment {
            String Year = "Tahun";
            String Month = "Bulan";
            String Day = "Hari";

        }

        interface Status {
            String Lunas = "Lunas";
            String Berlebih = "Berlebih";
            String Kurang = "Kurang";
        }
    }
}