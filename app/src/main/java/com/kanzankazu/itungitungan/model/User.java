package com.kanzankazu.itungitungan.model;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.UserPreference;
import com.kanzankazu.itungitungan.util.DateTimeUtil;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil;
import com.kanzankazu.itungitungan.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faisal Bahri on 2019-11-18.
 */
public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String key;
    private String uId;
    private String level;
    private String tokenAccess;
    private String tokenFcm;
    private String token;
    private String name;
    private String email;
    private String phone;
    private String photoUrl;
    private String photoDt;
    private Boolean isEmailVerified;
    private Boolean isSignIn;
    private String phoneCode;
    private String firstSignIn;
    private String lastSignIn;
    private String lastSignOut;
    private String bySignIn;

    protected User(Parcel in) {
        key = in.readString();
        uId = in.readString();
        level = in.readString();
        tokenAccess = in.readString();
        tokenFcm = in.readString();
        token = in.readString();
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        photoUrl = in.readString();
        photoDt = in.readString();
        byte tmpIsEmailVerified = in.readByte();
        isEmailVerified = tmpIsEmailVerified == 0 ? null : tmpIsEmailVerified == 1;
        byte tmpIsSignIn = in.readByte();
        isSignIn = tmpIsSignIn == 0 ? null : tmpIsSignIn == 1;
        phoneCode = in.readString();
        firstSignIn = in.readString();
        lastSignIn = in.readString();
        lastSignOut = in.readString();
        bySignIn = in.readString();
    }

    public User(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.uId = user.getUid();
    }

    public User(String uId, String name, String email){
        this.uId = uId;
        this.name = name;
        this.email = email;
    }

    public User() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(uId);
        parcel.writeString(level);
        parcel.writeString(tokenAccess);
        parcel.writeString(tokenFcm);
        parcel.writeString(token);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(photoUrl);
        parcel.writeString(photoDt);
        parcel.writeByte((byte) (isEmailVerified == null ? 0 : isEmailVerified ? 1 : 2));
        parcel.writeByte((byte) (isSignIn == null ? 0 : isSignIn ? 1 : 2));
        parcel.writeString(phoneCode);
        parcel.writeString(firstSignIn);
        parcel.writeString(lastSignIn);
        parcel.writeString(lastSignOut);
        parcel.writeString(bySignIn);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTokenAccess() {
        return tokenAccess;
    }

    public void setTokenAccess(String tokenAccess) {
        this.tokenAccess = tokenAccess;
    }

    public String getTokenFcm() {
        return tokenFcm;
    }

    public void setTokenFcm(String tokenFcm) {
        this.tokenFcm = tokenFcm;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoDt() {
        return photoDt;
    }

    public void setPhotoDt(String photoDt) {
        this.photoDt = photoDt;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Boolean getSignIn() {
        return isSignIn;
    }

    public void setSignIn(Boolean signIn) {
        isSignIn = signIn;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getFirstSignIn() {
        return firstSignIn;
    }

    public void setFirstSignIn(String firstSignIn) {
        this.firstSignIn = firstSignIn;
    }

    public String getLastSignIn() {
        return lastSignIn;
    }

    public void setLastSignIn(String lastSignIn) {
        this.lastSignIn = lastSignIn;
    }

    public String getLastSignOut() {
        return lastSignOut;
    }

    public void setLastSignOut(String lastSignOut) {
        this.lastSignOut = lastSignOut;
    }

    public String getBySignIn() {
        return bySignIn;
    }

    public void setBySignIn(String bySignIn) {
        this.bySignIn = bySignIn;
    }

}
