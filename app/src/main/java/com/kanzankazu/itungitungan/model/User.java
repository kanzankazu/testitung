package com.kanzankazu.itungitungan.model;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Faisal Bahri on 2019-11-18.
 */
@IgnoreExtraProperties
public class User {

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
    private Boolean isLogin;
    private String firstLogin;
    private String lastLogin;
    private String byLogin;

    public User(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.uId = user.getUid();
    }

    public User() {

    }

    /*FIREBASE DATABASE_FIREBASE START*/
    public static Boolean isUserDataExist(DatabaseReference database, User user) {
        final Boolean[] isExist = {false};
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.EMAIL)
                .equalTo(user.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            /*for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                user1[0] = userSnapshot.getValue(User.class);
                            }*/
                            isExist[0] = true;
                        } else {
                            isExist[0] = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        isExist[0] = true;
                    }
                });

        return isExist[0];

    }

    public static void setUser(DatabaseReference database, Activity mActivity, User user, FirebaseDatabaseUtil.valueListener mListener) {
        String primaryKey = database.child(Constants.DATABASE_FIREBASE.TABLE.USER).push().getKey();
        user.setKey(primaryKey);

        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(user.getuId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> mListener.onSuccess(mActivity.getString(R.string.message_database_save_success)))
                .addOnFailureListener(e -> mListener.onFailure(e.getMessage()));
    }

    public static void updateUser(DatabaseReference database, Activity mActivity, User user, FirebaseDatabaseUtil.valueListener mListener) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(user.getuId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> mListener.onSuccess(mActivity.getString(R.string.message_database_update_success)))
                .addOnFailureListener(e -> mListener.onFailure(e.getMessage()));
    }

    public static void removeUser(DatabaseReference database, Activity mActivity, String uid, FirebaseDatabaseUtil.valueListener listener) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(uid)
                .removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess(mActivity.getString(R.string.message_database_delete_success)));
    }

    public static User getUser(DatabaseReference database, String uId, Boolean isSingleCall) {
        final User[] user = {new User()};

        if (isSingleCall) {
            database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .child(uId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                user[0] = dataSnapshot.getValue(User.class);
                            } else {
                                user[0] = null;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            user[0] = null;
                        }
                    });
        } else {
            database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .child(uId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                user[0] = dataSnapshot.getValue(User.class);
                            } else {
                                user[0] = null;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            user[0] = null;
                        }
                    });

        }


        return user[0];

    }

    public static List<User> getUsers(DatabaseReference database, Boolean isSingleCall) {
        List<User> users = new ArrayList<>();

        if (isSingleCall) {
            database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
        return users;
    }

    public static void setFCMTokenUser(DatabaseReference database, String uid, String fcmToken, FirebaseDatabaseUtil.valueListener mListener) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(uid)
                .child(Constants.DATABASE_FIREBASE.ROW.TOKEN_FCM)
                .setValue(fcmToken)
                .addOnSuccessListener(aVoid -> mListener.onSuccess("FCM data berhasil disimpan"))
                .addOnFailureListener(e -> mListener.onFailure(e.getMessage()));
    }

    /*FIREBASE DATABASE_FIREBASE END*/
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

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getByLogin() {
        return byLogin;
    }

    public void setByLogin(String byLogin) {
        this.byLogin = byLogin;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("uId", uId);
        result.put("level", level);
        result.put("tokenAccess", tokenAccess);
        result.put("tokenFcm", tokenFcm);
        result.put("token", token);
        result.put("name", name);
        result.put("email", email);
        result.put("phone", phone);
        result.put("photoUrl", photoUrl);
        result.put("photoDt", photoDt);
        result.put("isEmailVerified", isEmailVerified);
        result.put("isLogin", isLogin);
        result.put("firstLogin", firstLogin);
        result.put("lastLogin", lastLogin);
        result.put("byLogin", byLogin);

        return result;
    }

}
