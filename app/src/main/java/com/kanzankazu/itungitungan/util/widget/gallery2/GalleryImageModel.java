package com.kanzankazu.itungitungan.util.widget.gallery2;

import android.os.Parcel;
import android.os.Parcelable;

public class GalleryImageModel implements Parcelable {

    public static final Creator<GalleryImageModel> CREATOR = new Creator<GalleryImageModel>() {
        @Override
        public GalleryImageModel createFromParcel(Parcel in) {
            return new GalleryImageModel(in);
        }

        @Override
        public GalleryImageModel[] newArray(int size) {
            return new GalleryImageModel[size];
        }
    };
    private String id;
    private String like;
    private String account_id;
    private String total_comment;
    private int status_like;
    private String username;
    private String image_icon;
    private String image_icon_local;
    private String image_posted;
    private String image_posted_local;
    private String caption;
    private String event_id;
    private int number;

    public GalleryImageModel() {
    }

    protected GalleryImageModel(Parcel in) {
        id = in.readString();
        like = in.readString();
        account_id = in.readString();
        total_comment = in.readString();
        status_like = Integer.parseInt(in.readString());
        username = in.readString();
        caption = in.readString();
        image_posted = in.readString();
        image_icon = in.readString();
        image_posted_local = in.readString();
        image_icon_local = in.readString();
        event_id = in.readString();
        number = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(like);
        parcel.writeString(account_id);
        parcel.writeString(total_comment);
        parcel.writeString(String.valueOf(status_like));
        parcel.writeString(username);
        parcel.writeString(caption);
        parcel.writeString(image_posted);
        parcel.writeString(image_icon);
        parcel.writeString(image_posted_local);
        parcel.writeString(image_icon_local);
        parcel.writeString(event_id);
        parcel.writeInt(number);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(String total_comment) {
        this.total_comment = total_comment;
    }

    public int getStatus_like() {
        return status_like;
    }

    public void setStatus_like(int status_like) {
        this.status_like = status_like;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage_posted() {
        return image_posted;
    }

    public void setImage_posted(String image_posted) {
        this.image_posted = image_posted;
    }

    public String getImage_icon() {
        return image_icon;
    }

    public void setImage_icon(String image_icon) {
        this.image_icon = image_icon;
    }

    public String getImage_posted_local() {
        return image_posted_local;
    }

    public void setImage_posted_local(String image_posted_local) {
        this.image_posted_local = image_posted_local;
    }

    public String getImage_icon_local() {
        return image_icon_local;
    }

    public void setImage_icon_local(String image_icon_local) {
        this.image_icon_local = image_icon_local;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
