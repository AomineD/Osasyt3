package com.dagf.uweyt3.iptv;

import android.os.Parcel;
import android.os.Parcelable;

public class M3UItem implements Parcelable {

    //private String itemName;
    //private String itemUrl;
    //private String itemIcon;
    //private String itemDuration;

    private String tvName;
    private String tvURL;
    private String tvIcon;

    public M3UItem() {
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public String getTvURL() {
        return tvURL;
    }

    public void setTvURL(String tvURL) {
        this.tvURL = tvURL;
    }

    public String getTvIcon() {
        return tvIcon;
    }

    public void setTvIcon(String tvIcon) {
        this.tvIcon = tvIcon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tvName);
        dest.writeString(this.tvURL);
        dest.writeString(this.tvIcon);
    }

    protected M3UItem(Parcel in) {
        this.tvName = in.readString();
        this.tvURL = in.readString();
        this.tvIcon = in.readString();
    }

    public static final Parcelable.Creator<M3UItem> CREATOR = new Parcelable.Creator<M3UItem>() {
        @Override
        public M3UItem createFromParcel(Parcel source) {
            return new M3UItem(source);
        }

        @Override
        public M3UItem[] newArray(int size) {
            return new M3UItem[size];
        }
    };
}
