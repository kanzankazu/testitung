package com.kanzankazu.itungitungan.model;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class Hutang {
    String hId;

    String GiverId;
    String GiverNm;
    String ReceiverId;
    String ReceiverNm;

    Boolean approvalGiver;
    Boolean approvalReceiver;

    String installmentPrice;
    String installmentTime;
    String dueDt;
    String desc;

    String Notes;
    String CreateAt;
    String CreateBy;
    String UpdateAt;
    String UpdateBy;

    public static void isExistHutang(DatabaseReference database, Hutang hutang, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        database.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                .orderByChild(Constants.DATABASE_FIREBASE.ROW.HID)
                .equalTo(hutang.gethId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listenerData.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listenerData.onFailure(databaseError.getMessage());
                    }
                });
    }

    public static void setHutang(DatabaseReference rootReference, Activity mActivity, Hutang hutang, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        String primaryKey = rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG).push().getKey();
        hutang.sethId(primaryKey);

        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                .child(hutang.gethId())
                .setValue(hutang)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess(mActivity.getString(R.string.message_database_save_success)))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void updateHutang(DatabaseReference rootReference, Activity mActivity, Hutang hutang, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                .child(hutang.gethId())
                .setValue(hutang)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess(mActivity.getString(R.string.message_database_update_success)))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void removeHutang(DatabaseReference rootReference, Activity mActivity, String uid, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                .child(uid)
                .removeValue()
                .addOnSuccessListener(aVoid -> listenerString.onSuccess(mActivity.getString(R.string.message_database_delete_success)))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void getHutang(DatabaseReference rootReference, String uId, Boolean isSingleCall, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        if (isSingleCall) {
            rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                    .child(uId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        } else {
            rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                    .child(uId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        }
    }

    public static void getHutangs(DatabaseReference rootReference, Boolean isSingleCall, FirebaseDatabaseUtil.ValueListenerDatas listenerDatas) {
        if (isSingleCall) {
            rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Hutang> hutangs = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Hutang hutang = snapshot.getValue(Hutang.class);
                                hutangs.add(hutang);
                            }*/

                            listenerDatas.onSuccess(dataSnapshot.getChildren());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerDatas.onFailure(databaseError.getMessage());
                        }
                    });
        } else {
            rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Hutang> hutangs = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Hutang hutang = snapshot.getValue(Hutang.class);
                                hutangs.add(hutang);
                            }*/

                            listenerDatas.onSuccess(dataSnapshot.getChildren());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerDatas.onFailure(databaseError.getMessage());
                        }
                    });

        }
    }

    public static void setFCMTokenHutang(DatabaseReference rootReference, String uid, String fcmToken, FirebaseDatabaseUtil.ValueListenerString listenerString) {
        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                .child(uid)
                .child(Constants.DATABASE_FIREBASE.ROW.TOKEN_FCM)
                .setValue(fcmToken)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess("FCM data berhasil disimpan"))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }


    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public String getGiverId() {
        return GiverId;
    }

    public void setGiverId(String giverId) {
        GiverId = giverId;
    }

    public String getGiverNm() {
        return GiverNm;
    }

    public void setGiverNm(String giverNm) {
        GiverNm = giverNm;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getReceiverNm() {
        return ReceiverNm;
    }

    public void setReceiverNm(String receiverNm) {
        ReceiverNm = receiverNm;
    }

    public Boolean getApprovalGiver() {
        return approvalGiver;
    }

    public void setApprovalGiver(Boolean approvalGiver) {
        this.approvalGiver = approvalGiver;
    }

    public Boolean getApprovalReceiver() {
        return approvalReceiver;
    }

    public void setApprovalReceiver(Boolean approvalReceiver) {
        this.approvalReceiver = approvalReceiver;
    }

    public String getInstallmentPrice() {
        return installmentPrice;
    }

    public void setInstallmentPrice(String installmentPrice) {
        this.installmentPrice = installmentPrice;
    }

    public String getInstallmentTime() {
        return installmentTime;
    }

    public void setInstallmentTime(String installmentTime) {
        this.installmentTime = installmentTime;
    }

    public String getDueDt() {
        return dueDt;
    }

    public void setDueDt(String dueDt) {
        this.dueDt = dueDt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }
}
