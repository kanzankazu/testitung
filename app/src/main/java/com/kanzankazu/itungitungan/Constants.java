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

    public interface LogInBy {
        String FACEBOOK = "FACEBOOK";
        String GOOGLE = "GOOGLE";
        String MANUAL = "MANUAL";
    }

    public interface URL_API {
        //String BASE_URL = BuildConfig.BASE_URL;
        String BASE_URL = BuildConfig.BASE_URL;

        String LOGOUT = "logout";
        String OPEN_NOTIFICATION = "openNotification";
        String RECEIVE_NOTIFICATION = "receiveNotification";
        String GET_SHORT_LINK = "getShortLink";

        /*
        * USER
        */
        String LOGIN = "login";
        String CHECKPASS = "checkpass";
        String CHANGEPASS = "changepass";
        String CHECKMAIL = "checkmail";
        String USERDETAIL = "userdetail";
        String FORGET1 = "forget1";
        String FORGET2 = "forget1";
        String QNAR = "qnar";
        String QNAW = "qnaw";
        String REGIS = "regis";
        String UPDATE = "update";
        String MAIL = "mail";
        String APPUPDATE = "appupdate";
        /*
        * DAGANGAN
        */
        String PEDAGANGAN_BARANG_ADD = "pedagangan/barang/add";
        String PEDAGANGAN_BARANG_DELETE = "pedagangan/barang/delete";
        String PEDAGANGAN_BUKA_ALL = "pedagangan/buka_all";
        String PEDAGANGAN_BUKA_TUTUP = "pedagangan/buka_tutup";
        String PEDAGANGAN_CATEGORY_LIST = "pedagangan/category_list";
        String PEDAGANGAN_DAGANGAN_ADD = "pedagangan/dagangan/add";
        String PEDAGANGAN_DAGANGAN_UPDATE = "pedagangan/dagangan/update";
        String PEDAGANGAN_DAGANGAN_DELETE = "pedagangan/dagangan/delete";
        String PEDAGANGAN_FAVORIT_ADD = "pedagangan/favorit/add";
        String PEDAGANGAN_FAVORIT_DELETE = "pedagangan/favorit/delete";
        String PEDAGANGAN_FAVORIT_GET = "pedagangan/favorit/get";
        String PEDAGANGAN_TUTUP_ALL = "pedagangan/tutup_all";
        String PEDAGANGAN_BARANG_LIST = "pedagangan/barang_list";
        String PEDAGANGAN_DETAIL = "pedagangan/detail";
        String PEDAGANGAN_SEARCH_BARANG = "pedagangan/search/barang";
        String PEDAGANGAN_SEARCH_DAGANGAN = "pedagangan/search/dagangan";
        String PEDAGANGAN_SEARCH_PEMILIK = "pedagangan/search/pemilik";
        String PEDAGANGAN_SEARCH_ID = "pedagangan/search/code";
    }

    public interface APIObject {
        String STATUS = "STATUS";
        String MESSAGE = "MESSAGE";
        String TOKEN = "TOKEN";
        String EXPIRED = "EXPIRED";

        //SignInUp
        String EMAIL = "EMAIL";
        String NAME = "NAME";
        String PASSWORD = "PASSWORD";
        String SOSMEDTOKEN = "SOSMEDTOKEN";
        String REFERRAL_CODE = "REFERRAL_CODE";
        String LATITUDE = "LATITUDE";
        String LONGITUDE = "LONGITUDE";
        String USER_LAT = "USER_LAT";
        String USER_LONG = "USER_LONG";
        String LINK_REFERRAL = "LINK_REFERRAL";
        String SAME_PHONE = "SAME_PHONE";
        String PHONE_NUMBER = "PHONE_NUMBER";
        String PHONE_AUTH_CREDENTIAL = "PHONE_AUTH_CREDENTIAL";

        //Profile
        String ACTIVE_STATUS = "ACTIVE_STATUS";
        String ADDRESS = "ADDRESS";
        String DOB = "DOB";
        String KTP = "KTP";
        String DISPLAY_PICT = "DISPLAY_PICT";
        String PROVINSI_ID = "PROVINSI_ID";
        String KABUPATEN_ID = "KABUPATEN_ID";
        String KECAMATAN_ID = "KECAMATAN_ID";
        String KELURAHAN_ID = "KELURAHAN_ID";
        String PHONE = "PHONE";
        String VERIFIED_PHONE = "VERIFIED_PHONE";
        String SIM_A_EXP_DATE = "SIM_A_EXP_DATE";
        String SIM_C_EXP_DATE = "SIM_C_EXP_DATE";
        String POSTAL_CODE = "POSTAL_CODE";
        String PROVINSI_LABEL = "PROVINSI_LABEL";
        String KABUPATEN_LABEL = "KABUPATEN_LABEL";
        String KECAMATAN_LABEL = "KECAMATAN_LABEL";
        String KELURAHAN_LABEL = "KELURAHAN_LABEL";
        String GENDER = "GENDER";
        String OCCUPATION = "OCCUPATION";
        String OCCUPATION_ID = "OCCUPATION_ID";
        String OCCUPATION_LABEL = "OCCUPATION_LABEL";

        //Favorite Workshop
        String WORKSHOP = "WORKSHOP";
        String WS_ID = "WS_ID";
        String WSG_ID = "WSG_ID";
        String WS_NAME = "WS_NAME";
        String WS_ADDRESS = "WS_ADDRESS";
        String WS_PHONE = "WS_PHONE";
        String WS_LONGITUDE = "WS_LONGITUDE";
        String WS_LATITUDE = "WS_LATITUDE";
        String WS_DESC = "WS_DESC";
        String SUMO_METER = "SUMO_METER";
        String RATING = "RATING";
        String PICT_NAME = "PICT_NAME";
        String USEFUL_INFO = "USEFUL_INFO";
        String METADATA = "METADATA";
        String WD_OPEN_TIME = "WD_OPEN_TIME";
        String WD_CLOSED_TIME = "WD_CLOSED_TIME";
        String SAT_OPEN_TIME = "SAT_OPEN_TIME";
        String SAT_CLOSED_TIME = "SAT_CLOSED_TIME";
        String SAT_STATUS = "SAT_STATUS";
        String SUN_OPEN_TIME = "SUN_OPEN_TIME";
        String SUN_CLOSED_TIME = "SUN_CLOSED_TIME";
        String SUN_STATUS = "SUN_STATUS";
        String PH_OPEN_TIME = "PH_OPEN_TIME";
        String PH_CLOSED_TIME = "PH_CLOSED_TIME";
        String PH_STATUS = "PH_STATUS";
        String WE_OPEN_DAY = "WE_OPEN_DAY";
        String PAYMENT_METHOD = "PAYMENT_METHOD";
        String AUTHORIZED = "AUTHORIZED";
        String WS_POSTAL_CODE = "WS_POSTAL_CODE";
        String DESC_WORKSHOP_GRADE = "DESC_WORKSHOP_GRADE";
        String pivot = "pivot";
        String FAVORITES = "FAVORITES";
        String SERVICE_CATEGORY = "SERVICE_CATEGORY";
        String FACILITIES = "FACILITIES";
        String FB = "FB";
        String TWITTER = "TWITTER";
        String REVIEW = "REVIEW";
        String PIT = "PIT";
        String CATEGORY = "CATEGORY";
        String KM = "KM";
        String BRAND = "BRAND";
        String FILTER = "FILTER";
        String DISTANCE = "DISTANCE";
        String OPERATION_STATUS = "OPERATION_STATUS";
        String SUPPORT_TRADE_IN = "SUPPORT_TRADE_IN";
        String SUPPORT_SMART_INSPECTION = "SUPPORT_SMART_INSPECTION";

        //Insurance
        String INSURANCE = "INSURANCE";
        String INSURANCE_ID = "INSURANCE_ID";
        String INSURANCE_NAME = "INSURANCE_NAME";
        String INSURANCE_DESC = "INSURANCE_DESC";
        String INSURANCE_PICT = "INSURANCE_PICT";

        //Add Vehicle
        String USER_VEHICLE_ID = "USER_VEHICLE_ID";
        String VT_ID = "VT_ID";
        String VB_ID = "VB_ID";
        String VS_ID = "VS_ID";
        String VRS_ID = "VRS_ID";
        String VM_ID = "VM_ID";
        String CC_ID = "CC_ID";
        String VF_ID = "VF_ID";
        String VTRANS_ID = "VTRANS_ID";
        String PLAT_NO = "PLAT_NO";
        String STNK_EXP_DATE = "STNK_EXP_DATE";
        String NO_MESIN = "NO_MESIN";
        String VB_DESC = "VB_DESC";
        String DESC_TYPE = "DESC_TYPE";
        String REAL_SERIES = "REAL_SERIES";
        String YEAR = "YEAR";
        String PROVINSI = "PROVINSI";

        //Get User Vehicle
        String VEHICLE = "VEHICLE";
        String TAHUN_PEMBUATAN = "TAHUN_PEMBUATAN";
        String DESC_CC = "DESC_CC";
        String DESC_SERIES = "DESC_SERIES";
        String DESC_SERIESREAL = "DESC_SERIESREAL";
        String DESC_TRANSMISSION = "DESC_TRANSMISSION";
        String DESC_BRAND = "DESC_BRAND";
        String VIN = "VIN";
        String UV_ASSEMBLE_YEAR = "UV_ASSEMBLE_YEAR";
        String DESC_FUEL = "DESC_FUEL";
        String DESC_VEHICLEMARKETINGTYPE = "DESC_VEHICLEMARKETINGTYPE";
        String DESC_COLOR = "DESC_COLOR";
        String INTRODUCTION_ESTIMASI = "INTRODUCTION_ESTIMASI";
        String INTRODUCTION_INSPECTION = "INTRODUCTION_INSPECTION";

        //Promo
        String WS_PROMO_ID = "WS_PROMO_ID";
        String PROMO = "PROMO";
        String DESCRIPTION = "DESCRIPTION";
        String BANNER_IMAGE = "BANNER_IMAGE";
        String END_DATE = "END_DATE";
        String TNC = "TNC";
        String TITLE = "TITLE";
        String PROVIDER = "PROVIDER";
        String BANNER_PROMO_ID = "BANNER_PROMO_ID";
        String PC_DESC = "PC_DESC";
        String PROMO_TYPE = "PROMO_TYPE";
        String PROMO_GROUP = "PROMO_GROUP";
        String CODE = "CODE";
        String URL = "URL";
        String IS_DETAIL_AVAILABLE = "IS_DETAIL_AVAILABLE";
        String SERVICE_TYPE = "SERVICE_TYPE";
        String INSPECTION = "INSPECTION";
        String SERVICE = "SERVICE";

        //BOOKING
        String BOOKING = "BOOKING";
        String BOOKING_ID = "BOOKING_ID";
        String KELURAHAN = "KELURAHAN";
        String AUDIENCE = "AUDIENCE";
        String IMAGE = "IMAGE";
        String DISC_PERCENTAGE = "DISC_PERCENTAGE";
        String DISC_AMOUNT = "DISC_AMOUNT";
        String START_DATE = "START_DATE";
        String CREATED_AT = "CREATED_AT";
        String CREATED_BY = "CREATED_BY";
        String WS_IMAGE = "WS_IMAGE";
        String REASON = "REASON";
        String TOTAL_BOOKING_REVIEW = "TOTAL_BOOKING_REVIEW";
        String BOOKING_REVIEW = "BOOKING_REVIEW";
        String TOTAL_UNREAD_NOTIFICATION = "TOTAL_UNREAD_NOTIFICATION";

        //CHANGE PASSWORD
        String OLD_PASSWORD = "OLD_PASSWORD";
        String NEW_PASSWORD = "NEW_PASSWORD";
        String NEW_PASSWORD_CONFIRM = "NEW_PASSWORD_CONFIRM";

        String AVG_RATING = "AVG_RATING";
        String RC_DESC = "RC_DESC";

        //RATING AND FEEDBACK
        String RC_ID = "RC_ID";
        String FEEDBACK = "FEEDBACK";
        String FEEDBACK_CATEGORY = "FEEDBACK_CATEGORY";
        String RC_IMAGE = "RC_IMAGE";
        String TOTAL = "TOTAL";

        //DELETE VEHICLE
        String UV_ID = "UV_ID";

        String NOTIFICATION_TOKEN = "NOTIFICATION_TOKEN";
        String KEYWORD = "KEYWORD";
        String DATE = "DATE";
        String TIME = "TIME";

        String RC_LABEL = "RC_LABEL";
        String REDEEM_ID = "REDEEM_ID";

        //HOME
        String TOTAL_POINT = "TOTAL_POINT";
        String POP_UP = "POP_UP";
        String TYPE = "TYPE";
        String CONTENT = "CONTENT";
        String SHARE = "SHARE";
        String INTRODUCTION_INSPEKSI = "INTRODUCTION_INSPEKSI";
        String TEXT_BANNER = "TEXT_BANNER";
        String TOTAL_VEHICLE = "TOTAL_VEHICLE";
        String TOTAL_VOUCHER = "TOTAL_VOUCHER";
        String JUBELMOTO_LINK = "JUBELMOTO_LINK";
        String QUIZ = "QUIZ";
        String QUIZ_RESULT = "QUIZ_RESULT";
        String IS_ON_PERIOD = "IS_ON_PERIOD";

        //PUSH NOTIFICATION
        String ID = "ID";

        //BONUS POINT
        String BONUS_CODE = "BONUS_CODE";

        //WORKSHOP
        String BOOKING_TYPE = "BOOKING_TYPE";
        String ONSITE = "ONSITE";
        String SUMO = "SUMO";
        String REPLY = "REPLY";

        //Notification
        String NOTIFICATION_HISTORY = "NOTIF_HISTORY";
        String NOTIFICATION = "NOTIFICATION";
        String NOTIFICATION_ID = "NOTIFICATION_ID";
        String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";
        String NOTIFICATION_DATE = "NOTIFICATION_DATE";
        String NOTIFICATION_DATETIME = "NOTIFICATION_DATETIME";
        String IS_READ = "READ";
        String NOTIFICATION_DETAIL = "NOTIF_HISTORY";
        String NOTIFICATION_DESC = "NOTIFICATION_DESC";
        String NOTIFICATION_IMAGE = "NOTIFICATION_IMAGE";
        String BUTTON_LABEL = "BUTTON_LABEL";
        String ACTION = "ACTION";
        String HIGH = "HIGH";
        String LOW = "LOW";
        String USER_VEHICLE = "USER_VEHICLE";
        String DATA = "DATA";
        String DISCLAIMER = "DISCLAIMER";

        //Valuation
        String ANSWER = "ANSWER";
        String EXPECTED_PRICE = "EXPECTED_PRICE";
        String TOP_PRICE = "TOP_PRICE";
        String BOTTOM_PRICE = "BOTTOM_PRICE";
        String VALUE = "VALUE";
        String JUBEL_ID = "JUBEL_ID";

        //MFC
        String MFC = "MFC";
        String MFC_BRANCH = "branch";
        String MFC_TENOR = "tenor";
        String MFC_GROUP_DESC = "MFC_GROUP_DESC";
        String MFC_GROUP_ID = "MFC_GROUP_ID";
        String MFC_GROUP_NAME = "MFC_GROUP_NAME";
        String MFC_GROUP_ADDRESS = "MFC_GROUP_ADDRESS";
        String MFC_DISPLAY_PICT = "DISPLAY_PICT";

        String MFC_BRANCH_ID = "MFC_BRANCH_ID";
        String MFC_BRANCH_GROUP_ID = "MFC_GROUP_ID";
        String MFC_BRANCH_NAME = "MFC_BRANCH_NAME";
        String MFC_BRANCH_PROVINSI_ID = "PROVINSI_ID";
        String MFC_BRANCH_KABUPATEN_ID = "KABUPATEN_ID";
        String MFC_BRANCH_KECAMATAN_ID = "KECAMATAN_ID";
        String MFC_BRANCH_KELURAHAN_ID = "KELURAHAN_ID";
        String MFC_BRANCH_ADDRESS = "MFC_BRANCH_ADDRESS";
        String MFC_BRANCH_POSTAL_CODE = "POSTAL_CODE";
        String MFC_BRANCH_DESC = "MFC_BRANCH_DESC";
        String MFC_BRANCH_DISPLAY_PICT = "DISPLAY_PICT";

        String MFC_TENOR_ID = "TENOR_ID";
        String MFC_TENOR_LABEL = "TENOR_LABEL";

        //LOAN APPROVAL
        String PREAPPROVAL_ID = "PREAPPROVAL_ID";
        String CANCEL_REASON_ID = "CANCEL_REASON_ID";
        String CANCEL_REASON_DESCRIPTION_OTHERS = "Lainnya";

        //STATUS REDEEM
        String REDEEM_HISTORY = "REDEEM_HISTORY";

        //SUPPORT HELLO
        String TICKET_ID = "TICKET_ID";
        String CATEGORY_ID = "CATEGORY_ID";
        String CASE_ID = "CASE_ID";
        String STATUS_ID = "STATUS_ID";
        String RESPONSE = "RESPONSE";

        //VEHICLE CATALOGUE
        String OTR_PRICE = "OTR_PRICE";
        String VM_DESC = "VM_DESC";
        String VCAT_ID = "VCAT_ID";
        String IMAGE_LINK = "IMAGE_LINK";

        //PRICE ENGINE
        String PRICE_ENGINE = "PRICE_ENGINE";

        //OTO QUIZ
        String QUESTION = "QUESTION";
        String BACKGROUND_IMAGE = "BACKGROUND_IMAGE";

        String CLICKABLE = "CLICKABLE";
        String THUMBNAIL = "THUMBNAIL";
        String QUIZ_ID = "QUIZ_ID";
        String INTENT = "INTENT";
        String QUIZ_MESSAGE = "QUIZ_MESSAGE";
        String BOTTOM_MESSAGE = "BOTTOM_MESSAGE";
        String BOTTOM_MESSAGE_INTENT = "BOTTOM_MESSAGE_INTENT";
        String POINTS = "POINTS";
        String IS_CORRECT = "IS_CORRECT";

        //SELF INSPECTION
        String MESIN = "MESIN";
        String KELISTRIKAN = "KELISTRIKAN";
        String FRAME = "FRAME";
        String FAKTUR = "FAKTUR";
        String STNK = "STNK";
        String SKPD = "SKPD";

    }

    public interface MessageStatus {
        String SENDER = "SENDER";
        String RECEIVER = "RECEIVER";
    }

    public interface MessageContentType {
        String IMAGE = "IMAGE";
        String TEXT = "TEXT";
    }

    public interface Result {
        String SUCCESS = "success";
        String FAILED = "failed";
        String MAINTENANCE = "maintenance";
        String CRITICAL_UPDATE = "critical update";
        String INVALID_TOKEN = "invalid token";
        String TOKEN_EXPIRED = "token expired";
        String PERMISSION_DENIED = "permission denied";
    }

    public interface BookingStatusType {
        String NOT_COME = "NOT COME";
        String WORK = "WORK";
        String CANCEL = "CANCEL";
        String DONE = "DONE";
        String BOOK = "BOOK";

    }

    public interface BookingHistoryType {
        String RESCHEDULE = "RESCHEDULE";
        String BOOKING = "BOOKING";
    }

    public interface FCMType {
        String REMINDER_BOOKING = "REMINDER_BOOKING";
        String REMINDER_RATING = "REMINDER_RATING";
        String REMINDER_SIM = "REMINDER_SIM";
        String REMINDER_STNK = "REMINDER_STNK";
        String VTS = "VTS";
        String REDEEM_LIST = "REDEEM_LIST";
        String REDEEM_DETAIL = "REDEEM_DETAIL";
        String PROMO_LIST = "PROMO_LIST";
        String PROMO_DETAIL = "PROMO_DETAIL";
        String SHARE = "SHARE";
        String BROWSER = "BROWSER";
        String VALUATION = "VALUATION";
        String VALUATION_FORM = "VALUATION_FORM";
        String NOTIFICATION_DETAIL = "NOTIFICATION_DETAIL";
        String LOAN_APPROVED = "LOAN_APPROVED";
        String LIST_APPROVAL = "LIST_APPROVAL";
        String BOOKING_SERVICE = "BOOKING_SERVICE";
        String INSPECTION = "INSPECTION";
        String LOAN_APPLICATION = "LOAN_APPLICATION";
        String LOAN_HISTORY = "LOAN_HISTORY";
        String HISTORY_POIN = "HISTORY_POIN";
        String HISTORY_SERVICE = "HISTORY_SERVICE";
        String WORKSHOP_LIST = "WORKSHOP_LIST";
        String NOTIFICATION = "NOTIFICATION";
        String PROFILE = "PROFILE";
        String EDIT_PROFILE = "EDIT_PROFILE";
        String GET_POIN = "GET_POIN";
        String GARAGE = "GARAGE";
        String TICKET_DETAIL = "TICKET_DETAIL";
        String CAMPAIGN = "CAMPAIGN";
        String VALUATION_CAR_FORM = "VALUATION_CAR_FORM";
        String JUBELMOTO = "JUBELMOTO";
        String SELF_INSPECTION = "SELF_INSPECTION";
        String GAMES_ROOM = "GAMES_ROOM";
        String WORKSHOP_REVIEW = "WORKSHOP_REVIEW";
        String REWARDS_EXPLORE = "REWARDS_EXPLORE";
        String REWARDS_MY_VOUCHER = "REWARDS_MY_VOUCHER";
        String REWARDS_EXPLORE_DETAIL = "REWARDS_EXPLORE_DETAIL";
        String REWARDS_MY_VOUCHER_DETAIL = "REWARDS_MY_VOUCHER_DETAIL";
        String JUBEL_STATUS = "JUBEL_STATUS";
    }

    public interface ScreenFlag {
        String IS_FROM_NOTIFICATION_FRAGMENT = "IS_FROM_NOTIFICATION_FRAGMENT";
        String IS_FROM_VALUATION_ACTIVITY = "IS_FROM_VALUATION_ACTIVITY";
        String IS_FROM_HISTORY_FRAGMENT = "IS_FROM_HISTORY_FRAGMENT";
    }

    public interface PromoType {
        String PACKAGE = "Paket";
        String DISCOUNT = "Discount";
        String FREE = "Gratis";
    }

    public interface PromoGroup {
        String INSPECTION = "INSPECTION";
        String SERVICE = "SERVICE";
    }

    public interface VehicleType {
        String MOTOR = "MOTOR";
        String MOBIL = "MOBIL";
    }

    public interface VehicleTypeID {
        String TYPE_MOTOR = "VT02";
        String TYPE_MOBIL = "VT01";
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