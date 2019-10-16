package com.kanzankazu.itungitungan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UserPreference {
    public static final String PREFS_NAME = "SHARED_PREF";
    private SharedPreferences sharedPreferences;
    private static UserPreference userPreference;
    public String promoCode = "";
    public String activityForOtp = "";
    public String phoneNumberForOtpProfile = "";

    public String workshopLocation = "";
    public String provinsiIdFilter = "";
    public String kabupatenIdFilter = "";

    public boolean isHomeRefresh = false;
    public boolean isCheckedRating = false;
    public boolean isCheckedDistance = false;

    GoogleSignInAccount googleSignInAccount = null;

    public GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        this.googleSignInAccount = googleSignInAccount;
    }

    private UserPreference(Context context){
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
        }else{
            return getInstance(MyApplication.getInstance());
        }
    }
    /**
     * Preference Helper
     * */
    private void removeSharedPrefByKey(String key) {
        this.sharedPreferences.edit().remove(key).apply();
    }

    public void clearSharedPrefs(){
        this.sharedPreferences.edit().clear().apply();
    }

    public boolean isContainKey(String key){
        return this.sharedPreferences.contains(key);
    }

    /**
     * Preference Editor
     * */
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
        for(int i  = 0; i < values.size(); i++){
            putSharedPrefString(key + "_" + Integer.toString(i), values.get(i));
        }
        putSharedPrefInt(key + "_size", values.size());
    }

    private ArrayList<String> getSharedPrefStringArray(String key) {
        ArrayList<String> values = new ArrayList<>();
        for(int i = 0; i < getSharedPrefInt(key + "_size"); i++){
            values.add(getSharedPrefString(key + "_" + Integer.toString(i)));
        }
        return values;
    }

    /**
     * Preference Dictionary
     * */

    public String getEmail() {
        return getSharedPrefString(Constants.SharedPreference.EMAIL);
    }

    public void setEmail(String email) {
        putSharedPrefString(Constants.SharedPreference.EMAIL, email);
    }

    public String getPassword() {
        return getSharedPrefString(Constants.SharedPreference.PASSWORD);
    }

    public void setPassword(String password) {
        putSharedPrefString(Constants.SharedPreference.PASSWORD, password);
    }

    public String getName() {
        return getSharedPrefString(Constants.SharedPreference.NAME);
    }

    public void setName(String name) {
        putSharedPrefString(Constants.SharedPreference.NAME, name);
    }

    public String getPhoneNumber(){
        return getSharedPrefString(Constants.SharedPreference.PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber){
        putSharedPrefString(Constants.SharedPreference.PHONE_NUMBER, phoneNumber);
    }

    public int getUserTotalVehicle() {
        return getSharedPrefInt(Constants.SharedPreference.TOTAL_VEHICLE);
    }

    public void setUserTotalVehicle(int totalVehicle) {
        putSharedPrefInt(Constants.SharedPreference.TOTAL_VEHICLE, totalVehicle);
    }

    public boolean isNotifBook() {
        return getSharedPrefBoolean(Constants.SharedPreference.NOTIF_MENU);
    }

    public void setNotifBook(boolean isLogin) {
        putSharedPrefBoolean(Constants.SharedPreference.NOTIF_MENU, isLogin);
    }

    public String getDisplayPict() {
        return getSharedPrefString(Constants.SharedPreference.DISPLAY_PICT);
    }

    public void setDisplayPict(String displayPict) {
        putSharedPrefString(Constants.SharedPreference.DISPLAY_PICT, displayPict);
    }

    public String getAccessToken() {
        return getSharedPrefString(Constants.SharedPreference.ACCESS_TOKEN);
    }

    public void setAccessToken(String accessToken) {
        putSharedPrefString(Constants.SharedPreference.ACCESS_TOKEN, accessToken);
    }

    public String getFCMToken(){
        return getSharedPrefString(Constants.SharedPreference.FCM_TOKEN);
    }

    public void setFCMToken(String fcmToken){
        putSharedPrefString(Constants.SharedPreference.FCM_TOKEN,fcmToken);
    }

    public boolean isIntroductionShowed() {
        return getSharedPrefBoolean(Constants.SharedPreference.INTRO);
    }

    public void setIntroductionShowed(boolean isShow) {
        putSharedPrefBoolean(Constants.SharedPreference.INTRO, isShow);
    }

    public boolean isTutorialShowed() {
        return getSharedPrefBoolean(Constants.SharedPreference.TUTORIAL);
    }

    public void setTutorialShowed(boolean isShow) {
        putSharedPrefBoolean(Constants.SharedPreference.TUTORIAL, isShow);
    }

    public boolean isLoginSocmed() {
        return getSharedPrefBoolean(Constants.SharedPreference.LOGIN_STATUS_SOCMED);
    }

    public void setLoginSocmed(boolean isLogin) {
        putSharedPrefBoolean(Constants.SharedPreference.LOGIN_STATUS_SOCMED, isLogin);
    }

    public boolean isUserFirstTimeOpenApp() {
        return getSharedPrefBoolean(Constants.SharedPreference.FIRST_TIME_OPEN);
    }

    public void setUserFirstTimeOpenApp(boolean isFirst) {
        putSharedPrefBoolean(Constants.SharedPreference.FIRST_TIME_OPEN, isFirst);
    }

    public void setLoginBy(String loginType) {
        putSharedPrefString(Constants.SharedPreference.LOGIN_BY, loginType);
    }

    public String getLoginBy() {
        return getSharedPrefString(Constants.SharedPreference.LOGIN_BY);
    }

    public void setPassedIntroduction(boolean passedIntroduction) {
        putSharedPrefBoolean(Constants.SharedPreference.PASSED_INTRODUCTION, passedIntroduction);
    }

    public boolean getPassedIntroduction() { return getSharedPrefBoolean(Constants.SharedPreference.PASSED_INTRODUCTION); }

    public void setDevicesArray(List<String> devicesArray) {
        putSharedPrefStringArray(Constants.SharedPreference.DEVICES_ARRAY, devicesArray);
    }

    public ArrayList<String> getDevicesArray(){
        return getSharedPrefStringArray(Constants.SharedPreference.DEVICES_ARRAY);
    }

    public void setLoginArray(List<String> loginArray) {
        putSharedPrefStringArray(Constants.SharedPreference.LOGIN_ARRAY, loginArray);
    }

    public ArrayList<String> getLoginArray(){
        return getSharedPrefStringArray(Constants.SharedPreference.LOGIN_ARRAY);
    }

    public void setABActive(boolean active) {
        putSharedPrefBoolean(Constants.SharedPreference.AB_ACTIVE, active);
    }

    public boolean getABActive() {
        return getSharedPrefBoolean(Constants.SharedPreference.AB_ACTIVE);
    }

    public void setABAllDevices(String allDevices) {
        putSharedPrefString(Constants.SharedPreference.AB_ALL_DEVICES, allDevices);
    }

    public String getABAllDevices() {
        return getSharedPrefString(Constants.SharedPreference.AB_ALL_DEVICES);
    }

    public void setABTestId(String userProperty){
        putSharedPrefString(Constants.SharedPreference.USER_PROPERTY, userProperty);
    }

    public String getABTestId() {
        return getSharedPrefString(Constants.SharedPreference.USER_PROPERTY);
    }

    public void setPromoActive(boolean isActive){
        putSharedPrefBoolean(Constants.SharedPreference.PROMO_ACTIVE, isActive);
    }

    public boolean isPromoActive() {
        return getSharedPrefBoolean(Constants.SharedPreference.PROMO_ACTIVE);
    }

    public void setPromoTitle(String title){
        putSharedPrefString(Constants.SharedPreference.PROMO_TITLE, title);
    }

    public String getPromoTitle() {
        return getSharedPrefString(Constants.SharedPreference.PROMO_TITLE);
    }

    public void setPromoType(String promoType){
        putSharedPrefString(Constants.SharedPreference.PROMO_TYPE, promoType);
    }

    public String getPromoType() {
        return getSharedPrefString(Constants.SharedPreference.PROMO_TYPE);
    }

    public void setPromoContent(String promoContent){
        putSharedPrefString(Constants.SharedPreference.PROMO_CONTENT, promoContent);
    }

    public String getPromoContent() {
        return getSharedPrefString(Constants.SharedPreference.PROMO_CONTENT);
    }

    public void setPromoMessage(String message){
        putSharedPrefString(Constants.SharedPreference.PROMO_MESSAGE, message);
    }

    public String getPromoMessage() {
        return getSharedPrefString(Constants.SharedPreference.PROMO_MESSAGE);
    }

    public void setPromoImage(String url){
        putSharedPrefString(Constants.SharedPreference.PROMO_IMAGE_URL, url);
    }

    public String getPromoImage() {
        return getSharedPrefString(Constants.SharedPreference.PROMO_IMAGE_URL);
    }

    public boolean isPromoShowed() {
        return getSharedPrefBoolean(Constants.SharedPreference.IS_PROMO_SHOW);
    }

    public void setPromoShowed(boolean isShow){
        putSharedPrefBoolean(Constants.SharedPreference.IS_PROMO_SHOW, isShow);
    }

    public boolean isPopupShowed() {
        return getSharedPrefBoolean(Constants.SharedPreference.IS_POPUP_SHOW);
    }

    public void setPopupShowed(boolean isShow){
        putSharedPrefBoolean(Constants.SharedPreference.IS_POPUP_SHOW, isShow);
    }

    /** CHECK USER CURRENT STATUS **/
    public void setLoginStatus() {
        if (getEmail().isEmpty() && getAccessToken().isEmpty()) {
            putSharedPrefString(Constants.SharedPreference.LOGIN_STATUS, Constants.LogInStatus.NOT_LOGIN);
        } else {
            putSharedPrefString(Constants.SharedPreference.LOGIN_STATUS, Constants.LogInStatus.LOGIN);
        }
    }

    public void setLoginGuestStatus() {
        putSharedPrefString(Constants.SharedPreference.LOGIN_STATUS, Constants.LogInStatus.GUEST);
    }

    public String getLoginStatus() {
        return getSharedPrefString(Constants.SharedPreference.LOGIN_STATUS);
    }

    public void setSumoPointClick(boolean isClick) {
        putSharedPrefBoolean(Constants.SharedPreference.SUMO_CLICK, isClick);
    }

    public boolean getSumoPointClick() {
        return getSharedPrefBoolean(Constants.SharedPreference.SUMO_CLICK);
    }

    public void setUserAddress(String address) {
        putSharedPrefString(Constants.SharedPreference.ADDRESS, address);
    }

    public String getUserAddress() {
        return getSharedPrefString(Constants.SharedPreference.ADDRESS);
    }

    public void setUserOccupation(String occupation) {
        putSharedPrefString(Constants.SharedPreference.OCCUPATION, occupation);
    }

    public String getUserOccupation() {
        return getSharedPrefString(Constants.SharedPreference.OCCUPATION);
    }

    public void setUserArea(String area) {
        putSharedPrefString(Constants.SharedPreference.AREA, area);
    }

    public String getUserArea() {
        return getSharedPrefString(Constants.SharedPreference.AREA);
    }

    public void setUserTotalBookingReview(int userTotalBookingReview) {
        putSharedPrefInt(Constants.SharedPreference.TOTAL_BOOKING_REVIEW, userTotalBookingReview);
    }

    public int getUserTotalBookingReview() {
        return getSharedPrefInt(Constants.SharedPreference.TOTAL_BOOKING_REVIEW);
    }

    public void setUserTotalUnreadNotification(int userTotalUnreadNotification) {
        putSharedPrefInt(Constants.SharedPreference.TOTAL_UNREAD_NOTIFICATION, userTotalUnreadNotification);
    }

    public int getUserTotalUnreadNotification() {
        return getSharedPrefInt(Constants.SharedPreference.TOTAL_UNREAD_NOTIFICATION);
    }

    public boolean isFirstTimeClickService() {
        return getSharedPrefBoolean(Constants.SharedPreference.FIRST_TIME_CLICK_SERVICE);
    }

    public void setFirstTimeClickService(boolean isShow){
        putSharedPrefBoolean(Constants.SharedPreference.FIRST_TIME_CLICK_SERVICE, isShow);
    }

    public void setUserLatitude(float latitude) {
        putSharedPrefFloat(Constants.SharedPreference.LATITUDE, latitude);
    }

    public void setUserLongitude(float longitude) {
        putSharedPrefFloat(Constants.SharedPreference.LONGITUDE, longitude);
    }

    public float getUserLatitude() {
        return getSharedPrefFloat(Constants.SharedPreference.LATITUDE);
    }

    public float getUserLongitude() {
        return getSharedPrefFloat(Constants.SharedPreference.LONGITUDE);
    }

    public void setFirstTimeCertification(boolean isChecked) {
        putSharedPrefBoolean(Constants.SharedPreference.CERTIFICATION_SERVICE_CHECKED, isChecked);
    }

    public boolean isFirstTimeCertification() {
        return getSharedPrefBoolean(Constants.SharedPreference.CERTIFICATION_SERVICE_CHECKED);
    }

    public void setValuation(boolean isValuation) {
        putSharedPrefBoolean(Constants.SharedPreference.IS_VALUATION, isValuation);
    }

    public boolean isSetValuation() {
        return getSharedPrefBoolean(Constants.SharedPreference.IS_VALUATION);
    }

    public void setChangeVehicle(boolean isChange) {
        putSharedPrefBoolean(Constants.SharedPreference.IS_CHANGE_VEHICLE, isChange);
    }

    public boolean isChangeVehicle() {
        return getSharedPrefBoolean(Constants.SharedPreference.IS_CHANGE_VEHICLE);
    }

    public void setRatingNotificationId(int notificationId){
        putSharedPrefInt(Constants.SharedPreference.RATING_NOTIFICATION_ID, notificationId);
    }

    public Integer getRatingNotificationId(){
        return getSharedPrefInt(Constants.SharedPreference.RATING_NOTIFICATION_ID);
    }

    public void setJubelMotoLink(String jubelMotoLink){
        putSharedPrefString(Constants.SharedPreference.JUBELMOTO_LINK, jubelMotoLink);
    }

    public String getJubelMotoLink(){
        return getSharedPrefString(Constants.SharedPreference.JUBELMOTO_LINK);
    }

    /*public void setUserData(User user) {
        setEmail(user.getEmail());
        setName(user.getName());
        setAccessToken(user.getAccessToken());
        setLoginStatus();
        setDisplayPict(user.getDisplayPic());
        setPhoneNumber(user.getVerifiedPhone());
        setUserTotalVehicle(user.getVehicle().size());
        setPassedIntroduction(true);
        getUserIdentifier(user.getEmail());
    }*/

    public void getUserIdentifier(String identifier) {
        byte[] encrpt= new byte[0];
        try {
            encrpt = identifier.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String base64 = Base64.encodeToString(encrpt, Base64.DEFAULT);
        Crashlytics.setUserIdentifier(base64);
    }

    /**
     * Reset all data when log out
     */

    public void setLogout(){
        setEmail(null);
        setName(null);
        setAccessToken(null);
        setLoginSocmed(false);
        setLoginBy("");
        setLoginStatus();
        setDisplayPict(null);
        setPhoneNumber(null);
        setUserTotalVehicle(0);
        setNotifBook(false);
        setUserTotalUnreadNotification(0);
        setPromoCode(null);
        Crashlytics.setUserIdentifier(null);
        setGoogleSignInAccount(null);
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Static Variable
     * */
    public void setPromoCode(String promoCode){
        this.promoCode = promoCode;
    }

    public String getPromoCode(){
        return promoCode;
    }

    public String getActivityForOtp() {
        return activityForOtp;
    }

    public void setActivityForOtp(String activityForOtp) {
        this.activityForOtp = activityForOtp;
    }

    public String getPhoneNumberForOtpProfile() {
        return phoneNumberForOtpProfile;
    }

    public void setPhoneNumberForOtpProfile(String phoneNumberForOtpProfile) {
        this.phoneNumberForOtpProfile = phoneNumberForOtpProfile;
    }

    public void setHomeRefresh(boolean isHomeRefresh) {
        this.isHomeRefresh = isHomeRefresh;
    }

    public boolean isHomeRefresh() {
        return isHomeRefresh;
    }

    public void setCheckedRating(boolean isCheckedRating) {
        this.isCheckedRating = isCheckedRating;
    }

    public boolean isCheckedRating() {
        return isCheckedRating;
    }

    public void setCheckedDistance(boolean isCheckedDistance) {
        this.isCheckedDistance = isCheckedDistance;
    }

    public boolean isCheckedDistance() {
        return isCheckedDistance;
    }

    public String getWorkshopLocation() {
        return workshopLocation;
    }

    public void setWorkshopLocation(String workshopLocation) {
        this.workshopLocation = workshopLocation;
    }

    public String getKabupatenIdFilter() {
        return kabupatenIdFilter;
    }

    public void setKabupatenIdFilter(String kabupatenIdFilter) {
        this.kabupatenIdFilter = kabupatenIdFilter;
    }

    public String getProvinsiIdFilter() {
        return provinsiIdFilter;
    }

    public void setProvinsiIdFilter(String provinsiIdFilter) {
        this.provinsiIdFilter = provinsiIdFilter;
    }
}
