package com.kanzankazu.itungitungan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Hutang implements Parcelable {
    private String hId;
    private int hutangRadioIndex;

    private String piutangId;
    private String piutangNama;
    private String piutangEmail;
    private Boolean piutangPersetujuanBaru;
    private Boolean piutangPersetujuanUbah;
    private Boolean piutangPersetujuanHapus;
    private String penghutangId;
    private String penghutangNama;
    private String penghutangEmail;
    private Boolean penghutangPersetujuanBaru;
    private Boolean penghutangPersetujuuanUbah;
    private Boolean penghutangPersetujuuanHapus;
    private String piutang_penghutang_id;

    private String hutangKeluargaId;
    private String hutangKeluargaNama;

    private String hutangNominal;
    private String hutangKeperluan;
    private String hutangCatatan;
    private String hutangPinjam;

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

    public static final Creator<Hutang> CREATOR = new Creator<Hutang>() {
        @Override
        public Hutang createFromParcel(Parcel in) {
            return new Hutang(in);
        }

        @Override
        public Hutang[] newArray(int size) {
            return new Hutang[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hId);
        dest.writeInt(hutangRadioIndex);
        dest.writeString(piutangId);
        dest.writeString(piutangNama);
        dest.writeString(piutangEmail);
        dest.writeByte((byte) (piutangPersetujuanBaru == null ? 0 : piutangPersetujuanBaru ? 1 : 2));
        dest.writeByte((byte) (piutangPersetujuanUbah == null ? 0 : piutangPersetujuanUbah ? 1 : 2));
        dest.writeByte((byte) (piutangPersetujuanHapus == null ? 0 : piutangPersetujuanHapus ? 1 : 2));
        dest.writeString(penghutangId);
        dest.writeString(penghutangNama);
        dest.writeString(penghutangEmail);
        dest.writeByte((byte) (penghutangPersetujuanBaru == null ? 0 : penghutangPersetujuanBaru ? 1 : 2));
        dest.writeByte((byte) (penghutangPersetujuuanUbah == null ? 0 : penghutangPersetujuuanUbah ? 1 : 2));
        dest.writeByte((byte) (penghutangPersetujuuanHapus == null ? 0 : penghutangPersetujuuanHapus ? 1 : 2));
        dest.writeString(piutang_penghutang_id);
        dest.writeString(hutangKeluargaId);
        dest.writeString(hutangKeluargaNama);
        dest.writeString(hutangNominal);
        dest.writeString(hutangKeperluan);
        dest.writeString(hutangCatatan);
        dest.writeString(hutangPinjam);
        dest.writeStringList(hutangBuktiGambar);
        dest.writeByte((byte) (hutangIsCicilan == null ? 0 : hutangIsCicilan ? 1 : 2));
        dest.writeString(hutangCicilanBerapaKali);
        dest.writeString(hutangCicilanBerapaKaliType);
        dest.writeInt(hutangCicilanBerapaKaliPosisi);
        dest.writeString(hutangCicilanNominal);
        dest.writeByte((byte) (hutangisBayarKapanSaja == null ? 0 : hutangisBayarKapanSaja ? 1 : 2));
        dest.writeString(hutangCicilanTanggalAkhir);
        dest.writeByte((byte) (isLunas == null ? 0 : isLunas ? 1 : 2));
        dest.writeString(CreateAt);
        dest.writeString(CreateBy);
        dest.writeString(UpdateAt);
        dest.writeString(UpdateBy);
    }

    public Hutang() {
    }

    protected Hutang(Parcel in) {
        hId = in.readString();
        hutangRadioIndex = in.readInt();
        piutangId = in.readString();
        piutangNama = in.readString();
        piutangEmail = in.readString();
        byte tmpPiutangPersetujuan = in.readByte();
        piutangPersetujuanBaru = tmpPiutangPersetujuan == 0 ? null : tmpPiutangPersetujuan == 1;
        penghutangId = in.readString();
        penghutangNama = in.readString();
        penghutangEmail = in.readString();
        byte tmpPenghutangPersetujuan = in.readByte();
        penghutangPersetujuanBaru = tmpPenghutangPersetujuan == 0 ? null : tmpPenghutangPersetujuan == 1;
        piutang_penghutang_id = in.readString();
        hutangKeluargaId = in.readString();
        hutangKeluargaNama = in.readString();
        hutangNominal = in.readString();
        hutangKeperluan = in.readString();
        hutangCatatan = in.readString();
        hutangPinjam = in.readString();
        hutangBuktiGambar = in.createStringArrayList();
        byte tmpHutangIsCicilan = in.readByte();
        hutangIsCicilan = tmpHutangIsCicilan == 0 ? null : tmpHutangIsCicilan == 1;
        hutangCicilanBerapaKali = in.readString();
        hutangCicilanBerapaKaliType = in.readString();
        hutangCicilanBerapaKaliPosisi = in.readInt();
        hutangCicilanNominal = in.readString();
        byte tmpHutangisBayarKapanSaja = in.readByte();
        hutangisBayarKapanSaja = tmpHutangisBayarKapanSaja == 0 ? null : tmpHutangisBayarKapanSaja == 1;
        hutangCicilanTanggalAkhir = in.readString();
        byte tmpIsLunas = in.readByte();
        isLunas = tmpIsLunas == 0 ? null : tmpIsLunas == 1;
        CreateAt = in.readString();
        CreateBy = in.readString();
        UpdateAt = in.readString();
        UpdateBy = in.readString();
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

    public String getPiutangEmail() {
        return piutangEmail;
    }

    public void setPiutangEmail(String piutangEmail) {
        this.piutangEmail = piutangEmail;
    }

    public Boolean getPiutangPersetujuanBaru() {
        return piutangPersetujuanBaru;
    }

    public void setPiutangPersetujuanBaru(Boolean piutangPersetujuanBaru) {
        this.piutangPersetujuanBaru = piutangPersetujuanBaru;
    }

    public Boolean getPiutangPersetujuanUbah() {
        return piutangPersetujuanUbah;
    }

    public void setPiutangPersetujuanUbah(Boolean piutangPersetujuanUbah) {
        this.piutangPersetujuanUbah = piutangPersetujuanUbah;
    }

    public Boolean getPiutangPersetujuanHapus() {
        return piutangPersetujuanHapus;
    }

    public void setPiutangPersetujuanHapus(Boolean piutangPersetujuanHapus) {
        this.piutangPersetujuanHapus = piutangPersetujuanHapus;
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

    public String getPenghutangEmail() {
        return penghutangEmail;
    }

    public void setPenghutangEmail(String penghutangEmail) {
        this.penghutangEmail = penghutangEmail;
    }

    public Boolean getPenghutangPersetujuanBaru() {
        return penghutangPersetujuanBaru;
    }

    public void setPenghutangPersetujuanBaru(Boolean penghutangPersetujuanBaru) {
        this.penghutangPersetujuanBaru = penghutangPersetujuanBaru;
    }

    public Boolean getPenghutangPersetujuuanUbah() {
        return penghutangPersetujuuanUbah;
    }

    public void setPenghutangPersetujuuanUbah(Boolean penghutangPersetujuuanUbah) {
        this.penghutangPersetujuuanUbah = penghutangPersetujuuanUbah;
    }

    public Boolean getPenghutangPersetujuuanHapus() {
        return penghutangPersetujuuanHapus;
    }

    public void setPenghutangPersetujuuanHapus(Boolean penghutangPersetujuuanHapus) {
        this.penghutangPersetujuuanHapus = penghutangPersetujuuanHapus;
    }

    public String getPiutang_penghutang_id() {
        return piutang_penghutang_id;
    }

    public void setPiutang_penghutang_id(String piutang_penghutang_id) {
        this.piutang_penghutang_id = piutang_penghutang_id;
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

    public String getHutangPinjam() {
        return hutangPinjam;
    }

    public void setHutangPinjam(String hutangPinjam) {
        this.hutangPinjam = hutangPinjam;
    }

    public List<String> getHutangBuktiGambar() {
        return hutangBuktiGambar;
    }

    public void setHutangBuktiGambar(List<String> hutangBuktiGambar) {
        this.hutangBuktiGambar = hutangBuktiGambar;
    }

    public Boolean getHutangIsCicilan() {
        return hutangIsCicilan;
    }

    public void setHutangIsCicilan(Boolean hutangIsCicilan) {
        this.hutangIsCicilan = hutangIsCicilan;
    }

    public String getHutangCicilanBerapaKali() {
        return hutangCicilanBerapaKali;
    }

    public void setHutangCicilanBerapaKali(String hutangCicilanBerapaKali) {
        this.hutangCicilanBerapaKali = hutangCicilanBerapaKali;
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

    public String getHutangCicilanNominal() {
        return hutangCicilanNominal;
    }

    public void setHutangCicilanNominal(String hutangCicilanNominal) {
        this.hutangCicilanNominal = hutangCicilanNominal;
    }

    public Boolean getHutangisBayarKapanSaja() {
        return hutangisBayarKapanSaja;
    }

    public void setHutangisBayarKapanSaja(Boolean hutangisBayarKapanSaja) {
        this.hutangisBayarKapanSaja = hutangisBayarKapanSaja;
    }

    public String getHutangCicilanTanggalAkhir() {
        return hutangCicilanTanggalAkhir;
    }

    public void setHutangCicilanTanggalAkhir(String hutangCicilanTanggalAkhir) {
        this.hutangCicilanTanggalAkhir = hutangCicilanTanggalAkhir;
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
