package com.kanzankazu.itungitungan.model;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kanzankazu.itungitungan.Constants;
import com.kanzankazu.itungitungan.R;
import com.kanzankazu.itungitungan.UserPreference;
import com.kanzankazu.itungitungan.util.DateTimeUtils;
import com.kanzankazu.itungitungan.util.Firebase.FirebaseDatabaseUtil;

import java.util.ArrayList;
import java.util.List;

public class Hutang {
    private String hId;
    private int hutangRadioIndex;

    private String piutangId;
    private String piutangNama;
    private String piutangEmail;
    private Boolean piutangPersetujuan;
    private String penghutangId;
    private String penghutangNama;
    private String penghutangEmail;
    private Boolean penghutangPersetujuan;
    private String piutang_penghutang_id;

    private String hutangKeluargaId;
    private String hutangKeluargaNama;

    private String hutangNominal;
    private String hutangKeperluan;
    private String hutangCatatan;
    private String hutangBuktiGambar0;
    private String hutangBuktiGambar1;
    private List<String> hutangBuktiGambar;

    private Boolean hutangIsCicilan;
    private String hutangCicilanBerapaKali;
    private String hutangCicilanBerapaKaliType;
    private int hutangCicilanBerapaKaliPosisi;
    private String hutangCicilanNominal;
    private Boolean hutangisBayarKapanSaja;
    private String hutangCicilanTanggalAkhir;

    private Boolean isLunas;
    private List<HutangCicilan> subList;

    private String CreateAt;
    private String CreateBy;
    private String UpdateAt;
    private String UpdateBy;

    public Hutang() {
    }

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

        hutang.setCreateAt(DateTimeUtils.getCurrentDate());
        hutang.setCreateBy(UserPreference.getInstance().getUid());

        rootReference.child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                .child(hutang.gethId())
                .setValue(hutang)
                .addOnSuccessListener(aVoid -> listenerString.onSuccess(mActivity.getString(R.string.message_database_save_success)))
                .addOnFailureListener(e -> listenerString.onFailure(e.getMessage()));
    }

    public static void updateHutang(DatabaseReference rootReference, Activity mActivity, Hutang hutang, FirebaseDatabaseUtil.ValueListenerString listenerString) {

        hutang.setUpdateAt(DateTimeUtils.getCurrentDate());
        hutang.setUpdateBy(UserPreference.getInstance().getUid());

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

    public static void getHutangs(DatabaseReference rootReference, Boolean isSingleCall, FirebaseDatabaseUtil.ValueListenerData listenerData) {
        if (isSingleCall) {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                    .orderByChild(Constants.DATABASE_FIREBASE.ROW.HID)
                    .equalTo(UserPreference.getInstance().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Hutang> hutangs = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Hutang hutang = snapshot.getValue(Hutang.class);
                                hutangs.add(hutang);
                            }*/

                            listenerData.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listenerData.onFailure(databaseError.getMessage());
                        }
                    });
        } else {
            rootReference
                    .child(Constants.DATABASE_FIREBASE.TABLE.HUTANG)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Hutang> hutangs = new ArrayList<>();
                            /*for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Hutang hutang = snapshot.getValue(Hutang.class);
                                hutangs.add(hutang);
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

    public String getHutangKeluargaId() {
        return hutangKeluargaId;
    }

    public void setHutangKeluargaId(String hutangKeluargaId) {
        this.hutangKeluargaId = hutangKeluargaId;
    }

    public String getHutangKeluargaNama() {
        return hutangKeluargaNama;
    }

    public void setHutangKeluargaNama(String hutangKeluargaNama) {
        this.hutangKeluargaNama = hutangKeluargaNama;
    }

    public String gethId() {
        return hId;
    }

    public void sethId(String hId) {
        this.hId = hId;
    }

    public int getHutangRadioIndex() {
        return hutangRadioIndex;
    }

    public void setHutangRadioIndex(int hutangRadioIndex) {
        this.hutangRadioIndex = hutangRadioIndex;
    }

    public String getPiutangId() {
        return piutangId;
    }

    public void setPiutangId(String piutangId) {
        this.piutangId = piutangId;
    }

    public String getPiutangNama() {
        return piutangNama;
    }

    public void setPiutangNama(String piutangNama) {
        this.piutangNama = piutangNama;
    }

    public String getPenghutangId() {
        return penghutangId;
    }

    public void setPenghutangId(String penghutangId) {
        this.penghutangId = penghutangId;
    }

    public String getPenghutangNama() {
        return penghutangNama;
    }

    public void setPenghutangNama(String penghutangNama) {
        this.penghutangNama = penghutangNama;
    }

    public String getPiutangEmail() {
        return piutangEmail;
    }

    public void setPiutangEmail(String piutangEmail) {
        this.piutangEmail = piutangEmail;
    }

    public String getPenghutangEmail() {
        return penghutangEmail;
    }

    public void setPenghutangEmail(String penghutangEmail) {
        this.penghutangEmail = penghutangEmail;
    }

    public Boolean getPiutangPersetujuan() {
        return piutangPersetujuan;
    }

    public void setPiutangPersetujuan(Boolean piutangPersetujuan) {
        this.piutangPersetujuan = piutangPersetujuan;
    }

    public Boolean getPenghutangPersetujuan() {
        return penghutangPersetujuan;
    }

    public void setPenghutangPersetujuan(Boolean penghutangPersetujuan) {
        this.penghutangPersetujuan = penghutangPersetujuan;
    }

    public String getHutangCicilanNominal() {
        return hutangCicilanNominal;
    }

    public void setHutangCicilanNominal(String hutangCicilanNominal) {
        this.hutangCicilanNominal = hutangCicilanNominal;
    }

    public String getHutangCicilanBerapaKali() {
        return hutangCicilanBerapaKali;
    }

    public void setHutangCicilanBerapaKali(String hutangCicilanBerapaKali) {
        this.hutangCicilanBerapaKali = hutangCicilanBerapaKali;
    }

    public String getHutangCicilanTanggalAkhir() {
        return hutangCicilanTanggalAkhir;
    }

    public void setHutangCicilanTanggalAkhir(String hutangCicilanTanggalAkhir) {
        this.hutangCicilanTanggalAkhir = hutangCicilanTanggalAkhir;
    }

    public String getHutangNominal() {
        return hutangNominal;
    }

    public void setHutangNominal(String hutangNominal) {
        this.hutangNominal = hutangNominal;
    }

    public String getHutangKeperluan() {
        return hutangKeperluan;
    }

    public void setHutangKeperluan(String hutangKeperluan) {
        this.hutangKeperluan = hutangKeperluan;
    }

    public String getHutangCatatan() {
        return hutangCatatan;
    }

    public void setHutangCatatan(String hutangCatatan) {
        this.hutangCatatan = hutangCatatan;
    }

    public String getHutangBuktiGambar0() {
        return hutangBuktiGambar0;
    }

    public void setHutangBuktiGambar0(String hutangBuktiGambar0) {
        this.hutangBuktiGambar0 = hutangBuktiGambar0;
    }

    public String getHutangBuktiGambar1() {
        return hutangBuktiGambar1;
    }

    public void setHutangBuktiGambar1(String hutangBuktiGambar1) {
        this.hutangBuktiGambar1 = hutangBuktiGambar1;
    }

    public List<String> getHutangBuktiGambar() {
        return hutangBuktiGambar;
    }

    public void setHutangBuktiGambar(List<String> hutangBuktiGambar) {
        this.hutangBuktiGambar = hutangBuktiGambar;
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

    public Boolean getHutangIsCicilan() {
        return hutangIsCicilan;
    }

    public void setHutangIsCicilan(Boolean cicilan) {
        hutangIsCicilan = cicilan;
    }

    public Boolean getHutangisBayarKapanSaja() {
        return hutangisBayarKapanSaja;
    }

    public void setHutangisBayarKapanSaja(Boolean freeTimeToPay) {
        hutangisBayarKapanSaja = freeTimeToPay;
    }

    public Boolean getLunas() {
        return isLunas;
    }

    public void setLunas(Boolean lunas) {
        isLunas = lunas;
    }

    public List<HutangCicilan> getSubList() {
        return subList;
    }

    public void setSubList(List<HutangCicilan> subList) {
        this.subList = subList;
    }

    public String getPiutang_penghutang_id() {
        return piutang_penghutang_id;
    }

    public void setPiutang_penghutang_id(String piutang_penghutang_id) {
        this.piutang_penghutang_id = piutang_penghutang_id;
    }

    public String getHutangCicilanBerapaKaliType() {
        return hutangCicilanBerapaKaliType;
    }

    public void setHutangCicilanBerapaKaliType(String hutangCicilanBerapaKaliType) {
        this.hutangCicilanBerapaKaliType = hutangCicilanBerapaKaliType;
    }

    public int getHutangCicilanBerapaKaliPosisi() {
        return hutangCicilanBerapaKaliPosisi;
    }

    public void setHutangCicilanBerapaKaliPosisi(int hutangCicilanBerapaKaliPosisi) {
        this.hutangCicilanBerapaKaliPosisi = hutangCicilanBerapaKaliPosisi;
    }
}
