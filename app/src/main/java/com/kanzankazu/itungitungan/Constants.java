package com.kanzankazu.itungitungan;

import android.Manifest;

public class Constants {

    public static final long TIMEOUT_CONNECTION = 30;
    //public static final int FETCH_FIREBASE = BuildConfig.FETCH_FIREBASE;
    public static final int SPLASH = 2000;
    public static final int REQUEST_INVITE = 2008;
    //public static final String DYNAMIC_LINK_DOMAIN = BuildConfig.DYNAMIC_LINK_DOMAIN;
    //public static final String DYNAMIC_LINK_PARAM = BuildConfig.DYNAMIC_LINK_PARAM;

    public interface SharedPreference {
        String EMAIL = "EMAIL";
        String PASSWORD = "PASSWORD";
        String NAME = "NAME";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String LOGIN_STATUS = "LOGIN_STATUS";
        String LOGIN = "LOGIN";
        String INTRO = "INTRO";
        String TUTORIAL = "TUTORIAL";
        String FCM_TOKEN = "FCM_TOKEN";
        String DISPLAY_PICT = "DISPLAY_PICT";
        String PHONE_NUMBER = "PHONE_NUMBER";
        String LINK_REFERRAL = "LINK_REFERRAL";
        String LOGIN_STATUS_SOCMED = "LOGIN_STATUS_SOCMED";
        String NOTIF_MENU = "NOTIF_MENU";
        String FIRST_TIME_OPEN = "FIRST_TIME_OPEN";
        String LOGIN_BY = "LOGIN_BY";
        String AB_ACTIVE = "AB_ACTIVE";
        String AB_ALL_DEVICES = "AB_ALL_DEVICES";
        String DEVICES_ARRAY = "DEVICES_ARRAY";
        String LOGIN_ARRAY = "LOGIN_ARRAY";
        String TOTAL_VEHICLE = "TOTAL_VEHICLE";
        String USER_PROPERTY = "USER_PROPERTY";
        String PROMO_ACTIVE = "PROMO_ACTIVE";
        String PROMO_TITLE = "PROMO_TITLE";
        String PROMO_TYPE = "PROMO_TYPE";
        String PROMO_CONTENT = "PROMO_CONTENT";
        String PROMO_MESSAGE = "PROMO_MESSAGE";
        String PROMO_IMAGE_URL = "PROMO_IMAGE_URL";
        String PROMO_LAST_DATE = "PROMO_LAST_DATE";
        String IS_PROMO_SHOW = "IS_PROMO_SHOW";
        String IS_POPUP_SHOW = "IS_POPUP_SHOW";
        String IS_LOGIN = "IS_LOGIN";
        String OCCUPATION = "OCCUPATION";
        String SUMO_CLICK = "SUMO_CLICK";
        String ADDRESS = "ADDRESS";
        String AREA = "AREA";
        String TOTAL_BOOKING_REVIEW = "TOTAL_BOOKING_REVIEW";
        String TOTAL_UNREAD_NOTIFICATION = "TOTAL_UNREAD_NOTIFICATION";
        String FIRST_TIME_CLICK_SERVICE = "FIRST_TIME_CLICK_SERVICE";
        String LATITUDE = "LATITUDE";
        String LONGITUDE = "LONGITUDE";
        String CERTIFICATION_SERVICE_CHECKED = "CERTIFICATION_SERVICE_CHECKED";
        String IS_VALUATION = "IS_VALUATION";
        String IS_CHANGE_VEHICLE = "IS_CHANGE_VEHICLE";
        String RATING_NOTIFICATION_ID = "RATING_NOTIFICATION_ID";
        String JUBELMOTO_LINK = "JUBELMOTO_LINK";
        String PASSED_INTRODUCTION = "PASSED_INTRODUCTION";
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


    public interface SearchType {
        String NEARBY = "NEARBY";
        String WORKSHOP = "BENGKEL";
    }

    public interface HistoryType {
        String ONGOING = "ONGOING";
        String PAST = "PAST";
    }

    public interface HomeBannerIntent {
        String HOME_BANNER_BOOKING = "BOOKING";
        String HOME_BANNER_REGISTER = "REGISTER";
        String HOME_BANNER_FORM = "FORM";
        String HOME_BANNER_GARAGE = "GARAGE";
        String HOME_BANNER_NOTIFICATION_DETAIL = "NOTIFICATION_DETAILS";
    }

    public interface BUNDLE {
        String LATITUDE = "LATITUDE";
        String LONGITUDE = "LONGITUDE";
        String IS_ON_SITE = "IS_ON_SITE";
        String GEOCODING_ADDRESS = "GEOCODING_ADDRESS";
        String ONSITE_ADDRESS = "ONSITE_ADDRESS";
        String ON_SITE_LATITUDE = "ON_SITE_LATITUDE";
        String ON_SITE_LONGITUDE = "ON_SITE_LONGITUDE";
        String ON_SITE_ADDRESS = "ON_SITE_ADDRESS";
        String OTHER_SERVICE_NOTES = "OTHER_SERVICE_NOTES";
        String SELECTED_VEHICLE = "SELECTED_VEHICLE";
        String VEHICLE_DISTANCE = "VEHICLE_DISTANCE";
        String SELECTED_CATEGORY_TYPE_MODEL = "SELECTED_CATEGORY_TYPE_MODEL";
        String IS_VALUATION = "IS_VALUATION";
        String LOW_PRICE_VALUATION = "LOW_PRICE_VALUATION";
        String HIGH_PRICE_VALUATION = "HIGH_PRICE_VALUATION";
        String BOTTOM_PRICE_STRING = "BOTTOM_PRICE";
        String TOP_PRICE_STRING = "TOP_PRICE";
        String KM = "KM";
        String VEHICLE = "VEHICLE";
        String VEHICLE_MARKETING_TYPE_IMAGE = "VEHICLE_MARKETING_TYPE_IMAGE";
        String DISCLAIMER = "DISCLAIMER";
        String MFC_CODE = "MFC_CODE";
        String VB_ID = "VB_ID";
        String VTRANS_ID = "VTRANS_ID";
        String VT_ID = "VT_ID";
        String SERVICE_MODEL = "SERVICE_MODEL";
        String CATEGORY_TYPE_MODEL = "CATEGORY_TYPE_MODEL";
        String SELECTED_TYPE = "SELECTED_TYPE";
        String BOOKING_SCENARIO = "BOOKING_SCENARIO";
        String BOOKING_TAB = "BOOKING_TAB";
        String TITLE_LOAN = "TITLE_LOAN";
        String DESC_LOAN = "DESC_LOAN";
        String TITLE_INSPECTION = "TITLE_INSPECTION";
        String DESC_INSPECTION = "DESC_INSPECTION";
        String INTRODUCTION_PREAPPROVAL = "INTRODUCTION_PREAPPROVAL";
        String INTRODUCTION_INSPECTION = "INTRODUCTION_INSPEKSI";
        String IS_INSPECTION = "IS_INSPECTION";
        String IS_WORKSHOP_LIST_SHOWN = "IS_WORKSHOP_LIST_SHOWN";
        String PROVINSI_MODEL = "PROVINSI_MODEL";
        String VALUE_MODEL = "VALUE_MODEL";
        String SHARE_TEXT = "SHARE_TEXT";
        String SELECTED_PROVINCE_ID = "SELECTED_PROVINCE_ID";
        String SELECTED_PROVINCE_LABEL = "SELECTED_PROVINCE_LABEL";
        String POPUP_BUY = "POPUP_BUY";
        String POPUP_SELL = "POPUP_SELL";
        String LOW_PRICE_B2B = "LOW_PRICE_B2B";
        String LOW_PRICE_C2C = "LOW_PRICE_C2C";
        String HIGH_PRICE_B2B = "HIGH_PRICE_B2B";
        String HIGH_PRICE_C2C = "HIGH_PRICE_C2C";
        String SUPPORT_CATEGORY = "SUPPORT_CATEGORY";
        String CUSTOM_CATEGORY = "CUSTOM_CATEGORY";
        String SUPPORT_CASE = "SUPPORT_CASE";
        String CATEGORY_LABEL = "CATEGORY_LABEL";
        String TICKET_ID = "TICKET_ID";
        String CAMERA_FRAME_EXTRA = "CAMERA_FRAME_EXTRA";
        String SELECTED_VEHICLE_VALUATION = "SELECTED_VEHICLE_VALUATION";
        String IS_FROM_BUYSELL_VEHICLE = "IS_FROM_BUYSELL_VEHICLE";
        String VEHICLE_TYPE = "VEHICLE_TYPE";
        String VEHICLE_TYPE_MOTOR = "motor";
        String VEHICLE_TYPE_CAR = "car";
        String IS_FROM_SCAN_QR = "isFromScanQr";
        String IS_FROM_PUSH_NOTIFICATION = "isFromPushNotification";
        String QUIZ_ID = "QUIZ_ID";
        String SELF_INSPECTION_RESULT = "SELF_INSPECTION_RESULT";
        String FROM = "FROM";
        String WS_ID = "WS_ID";
        String UV_ID = "UV_ID";
        String BOOKING_ID = "BOOKING_ID";
        String JUBEL_STATUS = "JUBEL_STATUS";
        String SLUG = "SLUG";
        String UNIT_ID = "UNIT_ID";
        String TAB_POSITION = "TAB_POSITION";
        String REWARD_DETAIL_ID = "REWARD_DETAIL_ID";
        String IS_REDEEMED = "IS_REDEEMED";
        String IS_FROM_FCM = "IS_FROM_FCM";
        String CURRENT_DATE = "CURRENT_DATE";
        String DATE = "DATE";
        String BOOKING_TYPE = "BOOKING_TYPE";
        String IS_FROM_BOOKING_STEP_TWO = "IS_FROM_BOOKING_STEP_TWO";
        String IS_PROMO_CHOOSEN = "IS_PROMO_CHOOSEN";
    }

    public interface EXTRA {
        String DATA = "DATA";
    }

    public interface AREA {
        String PROVINSI = "PROVINSI";
        String KABUPATEN = "KABUPATEN";
        String KECAMATAN = "KECAMATAN";
        String KELURAHAN = "KELURAHAN";
    }

    public interface LOAN_STATUS {
        String OPEN = "OPEN";
        String DOCUMENT = "DOCUMENT";
        String REVIEW = "REVIEW";
        String PREAPPROVED = "PREAPPROVED";
        String REJECTED = "REJECTED";
        String APPROVED = "APPROVED";
        String DENIED = "DENIED";
        String ACCEPTED = "ACCEPTED";
        String CANCELED = "CANCELED";
        String EXPIRED = "EXPIRED";
        String BOOK = "BOOK";
        String VEHICLE_DOCUMENT = "VEHICLE DOCUMENT";
        String VERIFY = "VERIFY";
    }

    public interface JUBEL_STATUS {
        String CANCELLED = "cancelled";
        String CLOSED = "closed";
        String OPEN = "open";
        String PROCESSING = "processing";
        String PENDING = "pending";
        String DEAL = "deal";
    }

    public interface ObjectJSON {
        String ACTIVE = "ACTIVE";
        String ALL_DEVICE = "ALL_DEVICES";
        String DEVICES = "DEVICES";
        String LOGIN = "LOGIN";
        String TITLE = "TITLE";
        String MESSAGE = "MESSAGE";
        String IMAGE_URL = "IMAGE_URL";
        String TYPE = "TYPE";
        String CONTENT = "CONTENT";
    }

    public interface SumoPointAction {
        String ACTION = "ACTION";
        String REDEEM = "REDEEM";
    }

    public interface RedeemType {
        String REWARD = "REWARD";
        String PRODUCT = "PRODUCT";
        String VOUCHER = "VOUCHER";
        String ETC = "ETC";
    }

    public interface OtpAuth {
        String VISIBLE = "VISIBLE";
        String GONE = "GONE";
        String OTP_PHONE = "PHONE";
        String OTP_CODE = "CODE";
        String OTP_VERIF_CODE = "OTP_CODE";
        String TIMER_STOP = "STOP";
        String TIMER_START = "START";

        interface OtpFor {
            String OTP_FOR = "OTP_FOR";
            String OTP_LOGIN_REGIS = "OTP_LOGIN_REGIS";
            String OTP_UPDATE_PROFILE = "OTP_UPDATE_PROFILE";
        }

        interface OtpObject {
            String PROFILE = "PROFILE";
        }

        interface OtpAuthLogin {
            String LOGIN_REGIS_BY = "LOGIN_BY";

            String TOKEN = "TOKEN";
            String FCM_TOKEN = "FCM_TOKEN";
            String REFERAL_CODE_STRING = "REFERAL_CODE_STRING";
            String CURRENT_LAT = "CURRENT_LAT";
            String CURRENT_LONG = "CURRENT_LONG";
            String NAME = "NAME";
            String EMAIL = "EMAIL";
            String PASSWORD = "PASSWORD";
            String NOTIF_TOKEN = "NOTIF_TOKEN";
        }

        interface OtpCallApi {
            String LOGIN_FACEBOOK = "LOGIN_FACEBOOK";
            String LOGIN_GOOGLE = "LOGIN_GOOGLE";
            String LOGIN_MANUAL = "LOGIN_MANUAL";
            String UPDATE_PROFILE = "UPDATE_PROFILE";
        }
    }

    public interface PriceEngine {
        String RESULT_FOR = "RESULT_FOR";

        interface ResultFor {
            String HOME_JUAL = "HOME_JUAL";
            String HOME_BELI = "HOME_BELI";
        }
    }

    public interface Dialog {
        interface Identifier {
            String PROVINCE_DIALOG = "PROVINCE";
            String BRAND_DIALOG = "BRAND";
            String SERIES_DIALOG = "SERIES";
            String MARKETING_TYPE_DIALOG = "MARKETING_TYPE";
            String ASSEMBLE_YEAR_DIALOG = "ASSEMBLE_YEAR";
        }
    }

    public interface Permission {
        String[] LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String[] CALL_PHONE = {Manifest.permission.CALL_PHONE};
    }

    public interface Boolean {
        String IS_FROM_PUSH_NOTIFICATION = "IS_FROM_PUSH_NOTIFICATION";
    }

    public interface DEEPLINK_TYPE {
        String PRICE_ENGINE_CAR = "price-engine-car";
        String PRICE_ENGINE_BIKE = "price-engine-bike";
        String JUBELMOTO = "jubelmoto";
    }

    public interface OTO_QUIZ {
        interface UserAction {
            String TRY_AGAIN = "TRY_AGAIN";
            String BACK_TO_HOME = "BACK_TO_HOME";
        }
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String PACKAGE_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_TWITTER = "com.twitter.android";
    public static final String PACKAGE_INSTAGRAM = "com.instagram.android";
    public static final String FACEBOOK_PAGE_ID = "158637887922970";
    public static final String FACEBOOK_NEW_VERSION_BASE_URL = "fb://facewebmodal/f?href=";
    public static final String FACEBOOK_OLD_VERSION_BASE_URL = "fb://page/";
}