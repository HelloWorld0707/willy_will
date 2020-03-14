package com.willy.will.detail.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Item implements Parcelable, Serializable {
    private int itemId;
    private String itemName;
    private int important;
    private String latitude;
    private String longitude;
    private String doneDate;
    private String startDate;
    private String endDate;
    private int todoId;
    private int groupId;
    private String groupName;
    private String groupColor;
    private int calenderId;
    private String calenderDate;
    private int loopId;
    private String loopWeek;
    private String roadAddressName;

    public Item(){}

    protected Item(Parcel in) {
        itemId = in.readInt();
        itemName = in.readString();
        important = in.readInt();
        latitude = in.readString();
        longitude = in.readString();
        doneDate = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        todoId = in.readInt();
        groupId = in.readInt();
        groupName = in.readString();
        groupColor = in.readString();
        calenderId = in.readInt();
        calenderDate = in.readString();
        loopId = in.readInt();
        loopWeek = in.readString();
        roadAddressName = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
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

    public int getCalenderId() {
        return calenderId;
    }

    public void setCalenderId(int calenderId) {
        this.calenderId = calenderId;
    }

    public String getCalenderDate() {
        return calenderDate;
    }

    public void setCalenderDate(String calenderDate) {
        this.calenderDate = calenderDate;
    }

    public int getLoopId() {
        return loopId;
    }

    public void setLoopId(int loopId) {
        this.loopId = loopId;
    }

    public String getLoopWeek() {
        return loopWeek;
    }

    public void setLoopWeek(String loopWeek) {
        this.loopWeek = loopWeek;
    }

    public String getRoadAddressName() { return roadAddressName; }

    public void setRoadAddressName(String roadAddressName) { this.roadAddressName = roadAddressName; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemId);
        dest.writeString(itemName);
        dest.writeInt(important);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(doneDate);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeInt(todoId);
        dest.writeInt(groupId);
        dest.writeString(groupName);
        dest.writeString(groupColor);
        dest.writeInt(calenderId);
        dest.writeString(calenderDate);
        dest.writeInt(loopId);
        dest.writeString(loopWeek);
        dest.writeString(roadAddressName);
    }
}
