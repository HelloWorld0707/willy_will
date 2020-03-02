package com.willy.will.search.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Distance implements Parcelable {

    private int length = -1;
    private String text = null;
    private boolean use = false;

    public Distance(int length, String text, boolean use) {
        this.length = length;
        this.text = text;
        this.use = use;
    }

    protected Distance(Parcel in) {
        length = in.readInt();
        text = in.readString();
        use = in.readByte() != 0;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // The writing order is important
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int useInt = this.use ? 1 : 0;
        dest.writeInt(this.length);
        dest.writeString(this.text);
        dest.writeInt(useInt);
    }

    public static final Creator<Distance> CREATOR = new Creator<Distance>() {
        @Override
        public Distance createFromParcel(Parcel in) {
            return new Distance(in);
        }

        @Override
        public Distance[] newArray(int size) {
            return new Distance[size];
        }
    };
}
