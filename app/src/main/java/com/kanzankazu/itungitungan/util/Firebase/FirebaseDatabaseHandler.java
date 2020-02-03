package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.UserPreference;
import com.kanzankazu.itungitungan.model.Hutang;
import com.kanzankazu.itungitungan.model.User;
import com.kanzankazu.itungitungan.util.DateTimeUtil;
import com.kanzankazu.itungitungan.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faisal Bahri on 2020-01-10.
 */
public class FirebaseDatabaseHandler extends FirebaseDatabaseUtil {
    /**
     * User
     */
    public static void isExistUser(User user, ValueListenerTrueFalse listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .orderByChild(Constants.FirebaseDatabase.ROW.UID)
                .equalTo(user.getUId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistUser : " + dataSnapshot.getChildrenCount());
                        listenerData.onSuccessExist(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureExist(databaseError.getMessage());
                    }
                });
    }

    public static void isExistEmail(String email, ValueListenerTrueFalse listenerTrueFalse) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .orderByChild(Constants.FirebaseDatabase.ROW.EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistEmail : " + dataSnapshot.getChildrenCount());
                        listenerTrueFalse.onSuccessExist(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerTrueFalse.onFailureExist(databaseError.getMessage());
                    }
                });
    }

    public static void isExistPhone(String phone, ValueListenerTrueFalse listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .orderByChild(Constants.FirebaseDatabase.ROW.PHONE)
                .equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistPhone : " + dataSnapshot.getChildrenCount());
                        listenerData.onSuccessExist(dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureExist(databaseError.getMessage());
                    }
                });
    }

    public static void isExistPhone(String phone, ValueListenerData listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .orderByChild(Constants.FirebaseDatabase.ROW.PHONE)
                .equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User isExistPhone : " + dataSnapshot.getChildrenCount());
                        listenerData.onSuccessData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureData(databaseError.getMessage());
                    }
                });
    }

    public static void setUser(Activity mActivity, User user, ValueListenerString listenerString) {
        String primaryKey = getRootRef().child(Constants.FirebaseDatabase.TABLE.USER).push().getKey();
        user.setKey(primaryKey);

        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(user.getUId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    listenerString.onSuccessString(mActivity.getString(R.string.message_database_save_success));
                    UserPreference.getInstance().setUid(user.getUId());
                    UserPreference.getInstance().setEmail(user.getEmail());
                    UserPreference.getInstance().setName(user.getName());
                    Log.d("Lihat", "setUser User : " + UserPreference.getInstance().getUid());
                    Log.d("Lihat", "setUser User : " + UserPreference.getInstance().getEmail());
                    Log.d("Lihat", "setUser User : " + UserPreference.getInstance().getName());
                })
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));
    }

    private static void setUserLogout(Activity mActivity, User user, ValueListenerString listenerString) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(user.getUId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> listenerString.onSuccessString(mActivity.getString(R.string.message_database_save_success)))
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));

    }

    public static void updateUser(Activity mActivity, User user, ValueListenerString listenerString) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(user.getUId())
                .setValue(user)
                .addOnSuccessListener(aVoid -> listenerString.onSuccessString(mActivity.getString(R.string.message_database_update_success)))
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));
    }

    public static void removeUser(Activity mActivity, String uid, ValueListenerString listenerString) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(uid)
                .removeValue()
                .addOnSuccessListener(aVoid -> listenerString.onSuccessString(mActivity.getString(R.string.message_database_delete_success)))
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));
    }

    public static void logOutUser(Activity mActivity, ValueListenerString listenerString) {
        String uid = UserPreference.getInstance().getUid();
        getUserByUid(uid, new FirebaseDatabaseUtil.ValueListenerDataTrueFalse() {
            @Override
            public void onSuccessDataExist(DataSnapshot dataSnapshot, Boolean isExsist) {
                User user = dataSnapshot.getValue(User.class);
                user.setTokenFcm("");
                user.setSignIn(false);
                user.setLastSignOut(DateTimeUtil.getCurrentDate().toString());
                setUserLogout(mActivity, user, listenerString);
            }

            @Override
            public void onFailureDataExist(String message) {
                listenerString.onFailureString(message);
            }
        });
    }

    public static void getUserByUid(String uId, ValueListenerDataTrueFalse listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(uId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User getUserByUid : " + dataSnapshot.getChildrenCount());
                        listenerData.onSuccessDataExist(dataSnapshot, dataSnapshot.exists());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureDataExist(databaseError.getMessage());
                    }
                });
    }

    public static void getUserByEmail(Activity activity, String email, ValueListenerObject listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .orderByChild(Constants.FirebaseDatabase.ROW.EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User getUserByEmail : " + dataSnapshot.getChildrenCount());

                        if (dataSnapshot.getChildrenCount() < 1) {
                            listenerData.onFailureData(activity.getString(R.string.message_database_data_cant_find));
                        } else if (dataSnapshot.getChildrenCount() > 1) {
                            listenerData.onFailureData(activity.getString(R.string.message_database_data_over));
                        } else {
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            User user = users.get(0);
                            if (user.getUId().equalsIgnoreCase(UserPreference.getInstance().getUid())) {
                                listenerData.onFailureData(activity.getString(R.string.message_its_you));
                            } else {
                                listenerData.onSuccessData(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureData(databaseError.getMessage());
                    }
                });
    }

    public static void getUserByPhone(Activity activity, String phone, ValueListenerObject listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .orderByChild(Constants.FirebaseDatabase.ROW.PHONE)
                .equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Lihat", "onDataChange User getUserByPhone : " + dataSnapshot.getChildrenCount());

                        if (dataSnapshot.getChildrenCount() < 1) {
                            listenerData.onFailureData(activity.getString(R.string.message_database_data_cant_find));
                        } else if (dataSnapshot.getChildrenCount() > 1) {
                            listenerData.onFailureData(activity.getString(R.string.message_database_data_over));
                        } else {
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            User user = users.get(0);
                            if (user.getUId().equalsIgnoreCase(UserPreference.getInstance().getUid())) {
                                listenerData.onFailureData(activity.getString(R.string.message_its_you));
                            } else {
                                listenerData.onSuccessData(user);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureData(databaseError.getMessage());
                    }
                });
    }

    public static void getUsers(Boolean isSingleCall, ValueListenerData listenerData) {
        if (isSingleCall) {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers1 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }

                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        } else {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers2 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }*/

                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void getUsers(Boolean isSingleCall, int limit, ValueListenerData listenerData) {
        if (isSingleCall) {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
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

                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        } else {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("Lihat", "onDataChange User getUsers2 : " + dataSnapshot.getChildrenCount());
                            List<User> users = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                users.add(user);
                            }*/

                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void setFCMTokenUser(String uid, String fcmToken, ValueListenerString listenerString) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(uid)
                .child(Constants.FirebaseDatabase.ROW.TOKEN_FCM)
                .setValue(fcmToken)
                .addOnSuccessListener(aVoid -> listenerString.onSuccessString("FCM data berhasil disimpan"))
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));
    }

    public static void setPhoneNumberUserValidate(Activity activity, String uid, String phone, ValueListenerString listenerString) {
        isExistPhone(phone, new FirebaseDatabaseUtil.ValueListenerData() {
            @Override
            public void onSuccessData(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                    }

                    if (users.size() == 1) {
                        if (users.get(0) != null) {
                            if (users.get(0).getEmail().equalsIgnoreCase(UserPreference.getInstance().getEmail())) {
                                listenerString.onSuccessString(activity.getString(R.string.message_phone_number_save_success));
                            } else {
                                listenerString.onFailureString(activity.getString(R.string.message_phone_number_exist, Utils.replace(users.get(0).getEmail())));
                            }
                        }
                    } else {
                        listenerString.onFailureString(activity.getString(R.string.message_phone_number_exist_used_another_account));
                    }
                } else {
                    setPhoneNumberUser(uid, phone, listenerString);
                }

            }

            @Override
            public void onFailureData(String message) {
                listenerString.onFailureString(message);
            }
        });
    }

    public static void setPhoneNumberUser(String uid, String phone, ValueListenerString listenerString) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.USER)
                .child(uid)
                .child(Constants.FirebaseDatabase.ROW.PHONE)
                .setValue(phone)
                .addOnSuccessListener(aVoid -> listenerString.onSuccessString("No Hp data berhasil disimpan"))
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));
    }

    /**
     * HUTANG
     */
    public static void isExistHutang(Hutang hutang, ValueListenerData listenerData) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                .orderByChild(Constants.FirebaseDatabase.ROW.HID)
                .equalTo(hutang.getHId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listenerData.onSuccessData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailureData(databaseError.getMessage());
                    }
                });
    }

    public static void setHutang(Activity mActivity, Hutang hutang, ValueListenerStringSaveUpdate listener) {
        String primaryKey = getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG).push().getKey();
        hutang.setHId(primaryKey);

        hutang.setCreateAt(DateTimeUtil.getCurrentDate().toString());
        hutang.setCreateBy(UserPreference.getInstance().getUid());

        getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                .child(hutang.getHId())
                .setValue(hutang)
                .addOnSuccessListener(aVoid -> listener.onSuccessSaveUpdate(mActivity.getString(R.string.message_database_save_success)))
                .addOnFailureListener(e -> listener.onFailureSaveUpdate(e.getMessage()));
    }

    public static void updateHutang(Activity mActivity, Hutang hutang, ValueListenerStringSaveUpdate listenerString) {

        hutang.setUpdateAt(DateTimeUtil.getCurrentDate().toString());
        hutang.setUpdateBy(UserPreference.getInstance().getUid());

        getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                .child(hutang.getHId())
                .setValue(hutang)
                .addOnSuccessListener(aVoid -> listenerString.onSuccessSaveUpdate(mActivity.getString(R.string.message_database_update_success)))
                .addOnFailureListener(e -> listenerString.onFailureSaveUpdate(e.getMessage()));
    }

    public static void removeHutang(Activity mActivity, String hid, ValueListenerString listenerString) {
        getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                .child(hid)
                .removeValue()
                .addOnSuccessListener(aVoid -> listenerString.onSuccessString(mActivity.getString(R.string.message_database_delete_success)))
                .addOnFailureListener(e -> listenerString.onFailureString(e.getMessage()));
    }

    public static void getHutang(String uId, Boolean isSingleCall, ValueListenerData listenerData) {
        if (isSingleCall) {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                    .child(uId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        } else {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                    .child(uId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void getHutangs(Boolean isSingleCall, ValueListenerData listenerData) {
        if (isSingleCall) {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Hutang> hutangs = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HUTANG hutang = snapshot.getValue(HUTANG.class);
                                hutangs.add(hutang);
                            }*/

                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });
        } else {
            getRootRef().child(Constants.FirebaseDatabase.TABLE.HUTANG)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Hutang> hutangs = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                HUTANG hutang = snapshot.getValue(HUTANG.class);
                                hutangs.add(hutang);
                            }*/

                            listenerData.onSuccessData(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailureData(databaseError.getMessage());
                        }
                    });

        }
    }
}
