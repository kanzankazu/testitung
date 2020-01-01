package com.kanzankazu.itungitungan.model;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.UserPreference;
import com.kanzankazu.itungitungan.util.DateTimeUtil;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil;
import com.kanzankazu.itungitungan.util.Utils;

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
    private Boolean isSignIn;

    private String firstSignIn;
    private String lastSignIn;
    private String lastSignOut;
    private String bySignIn;

    public User(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.email = user.getEmail();
        this.uId = user.getUid();
    }

    public User() {

    }

    /*FIREBASE DATABASE_FIREBASE START*/
    public static void isExistUser(DatabaseReference database, User user, FirebaseDatabaseUtil.ValueListenerTrueFalse listenerData) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.UID)
                .equalTo(user.getuId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistUser : " + dataSnapshot.getChildrenCount());
                        listenerData.isExist(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void isExistEmail(DatabaseReference database, String email, FirebaseDatabaseUtil.ValueListenerTrueFalse listenerTrueFalse) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistEmail : " + dataSnapshot.getChildrenCount());
                        listenerTrueFalse.isExist(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerTrueFalse.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void isExistPhone(DatabaseReference database, String phone, FirebaseDatabaseUtil.ValueListenerTrueFalse listenerData) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.PHONE)
                .equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistPhone : " + dataSnapshot.getChildrenCount());
                        listenerData.isExist(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void isExistPhone(DatabaseReference database, String phone, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.PHONE)
                .equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistPhone : " + dataSnapshot.getChildrenCount());
                        listenerData.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void setUser(DatabaseReference rootReference, Activity mActivity, User user, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        String primaryKey = rootReference.child(Constants.DATABASE_FIREBASE.TABLE.USER).push().getKey();
        user.setKey(primaryKey);

        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(user.getuId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    listenerString.onSuccess(mActivity.getString(R.string.message_database_save_success));
                    UserPreference.getInstance().setUid(user.getuId());
                    UserPreference.getInstance().setEmail(user.getEmail());
                    UserPreference.getInstance().setName(user.getName());
                    UserPreference.getInstance().setIsLogin(true);
                    Log.d("Lihat", "setUser User : " + UserPreference.getInstance().getUid());
                    Log.d("Lihat", "setUser User : " + UserPreference.getInstance().getEmail());
                    Log.d("Lihat", "setUser User : " + UserPreference.getInstance().getName());
                })
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void updateUser(DatabaseReference rootReference, Activity mActivity, User user, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(user.getuId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess(mActivity.getString(R.string.message_database_update_success)))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void removeUser(DatabaseReference rootReference, Activity mActivity, String uid, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(uid)
                .removeValue()
                .addOnSuccessListener(aVoid -> listenerString.onSuccess(mActivity.getString(R.string.message_database_delete_success)))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void logOutUser(DatabaseReference rootReference, Activity mActivity, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        String uid = UserPreference.getInstance().getUid();
        getUserByUid(rootReference, uid, new FirebaseDatabaseUtil.ValueListenerDataTrueFalse() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot, Boolean isExsist) {
                User user = dataSnapshot.getValue(User.class);
                user.setTokenFcm("");
                user.setSignIn(false);
                user.setLastSignOut(DateTimeUtil.getCurrentDate().toString());
                setUser(rootReference, mActivity, user, listenerString);
            }

            @Override
            public void onFailure(String message) {
                listenerString.onFailure(message);
            }
        });
    }

    public static void getUserByUid(DatabaseReference rootReference, String uId, FirebaseDatabaseUtil.ValueListenerDataTrueFalse listenerData) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User getUserByUid : " + dataSnapshot.getChildrenCount());
                        listenerData.onSuccess(dataSnapshot, dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void getUserByEmail(Activity activity, DatabaseReference database, String email, FirebaseDatabaseUtil.ValueListenerObject listenerData) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User getUserByEmail : " + dataSnapshot.getChildrenCount());

                        if (dataSnapshot.getChildrenCount() < 1) {
                            listenerData.onFailure(activity.getString(R.string.message_database_data_cant_find));
                        } else if (dataSnapshot.getChildrenCount() > 1) {
                            listenerData.onFailure(activity.getString(R.string.message_database_data_over));
                        } else {
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            User user = users.get(0);
                            if (user.getuId().equalsIgnoreCase(UserPreference.getInstance().getUid())) {
                                listenerData.onFailure(activity.getString(R.string.message_its_you));
                            } else {
                                listenerData.onSuccess(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void getUserByPhone(Activity activity, DatabaseReference database, String phone, FirebaseDatabaseUtil.ValueListenerObject listenerData) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.PHONE)
                .equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User getUserByPhone : " + dataSnapshot.getChildrenCount());

                        if (dataSnapshot.getChildrenCount() < 1) {
                            listenerData.onFailure(activity.getString(R.string.message_database_data_cant_find));
                        } else if (dataSnapshot.getChildrenCount() > 1) {
                            listenerData.onFailure(activity.getString(R.string.message_database_data_over));
                        } else {
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            User user = users.get(0);
                            if (user.getuId().equalsIgnoreCase(UserPreference.getInstance().getUid())) {
                                listenerData.onFailure(activity.getString(R.string.message_its_you));
                            } else {
                                listenerData.onSuccess(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void getUsers(DatabaseReference rootReference, Boolean isSingleCall, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        if (isSingleCall) {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers1 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        } else {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers2 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }*/

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void getUsers(DatabaseReference rootReference, Boolean isSingleCall, int limit, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        if (isSingleCall) {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .limitToFirst(limit)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers1 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        } else {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers2 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }*/

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void getUsers(DatabaseReference rootReference, Boolean isSingleCall, String s, int limit, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        if (isSingleCall) {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .limitToFirst(limit)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers1 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        } else {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers2 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }*/

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void setFCMTokenUser(DatabaseReference rootReference, String uid, String fcmToken, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference
                .child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(uid)
                .child(Constants.DATABASE_FIREBASE.ROW.TOKEN_FCM)
                .setValue(fcmToken)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess("FCM data berhasil disimpan"))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void setPhoneNumberUserValidate(Activity activity, DatabaseReference rootReference, String uid, String phone, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        isExistPhone(rootReference, phone, new FirebaseDatabaseUtil.ValueListenerData() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                    }

                    if (users.size() == 1) {
                        if (users.get(0) != null) {
                            if (users.get(0).getEmail().equalsIgnoreCase(UserPreference.getInstance().getEmail())) {
                                listenerString.onSuccess(activity.getString(R.string.message_phone_number_save_success));
                            } else {
                                listenerString.onFailure(activity.getString(R.string.message_phone_number_exist, Utils.replace(users.get(0).email)));
                            }
                        }
                    } else {
                        listenerString.onFailure(activity.getString(R.string.message_phone_number_exist_used_another_account));
                    }
                } else {
                    setPhoneNumberUser(rootReference, uid, phone, listenerString);
                }

            }

            @Override
            public void onFailure(String message) {
                listenerString.onFailure(message);
            }
        });
    }

    public static void setPhoneNumberUser(DatabaseReference rootReference, String uid, String phone, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.USER)
                .child(uid)
                .child(Constants.DATABASE_FIREBASE.ROW.PHONE)
                .setValue(phone)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess("No Hp data berhasil disimpan"))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
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

    public Boolean getSignIn() {
        return isSignIn;
    }

    public void setSignIn(Boolean signIn) {
        isSignIn = signIn;
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
        result.put("isSignIn", isSignIn);
        result.put("firstSignIn", firstSignIn);
        result.put("lastSignIn", lastSignIn);
        result.put("lastSignOut", lastSignOut);
        result.put("bySignIn", bySignIn);

        return result;
    }
}
