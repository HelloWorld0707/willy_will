package com.willy.will.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable {

    private int groupId;
    private String groupName;
    private String groupColor;

    public Group() {
        groupId = -1;
        groupName = null;
        groupColor = null;
    }

    public Group(int groupId, String groupName, String groupColor) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupColor = groupColor;
    }

    protected Group(Parcel in) {
        groupId = in.readInt();
        groupName = in.readString();
        groupColor = in.readString();
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(String groupColor) {
        this.groupColor = groupColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // The writing order is important
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.groupId);
        dest.writeString(this.groupName);
        dest.writeString(this.groupColor);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

}
