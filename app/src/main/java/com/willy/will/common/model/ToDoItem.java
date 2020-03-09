package com.willy.will.common.model;

import java.io.Serializable;

public class ToDoItem implements Serializable {
    private int itemId;
    private int groupId;
    private String locationX;
    private String locationY;
    private String doneDate;
    private String startDate;
    private String endDate;
    private int toDoId;
    private int rank;
    private String name;
    private boolean done;

    public ToDoItem() {
        itemId = -1;
        groupId = -1;
        locationX = null;
        locationY = null;
        doneDate = null;
        startDate = null;
        endDate = null;
        toDoId = -1;
        rank = -1;
        name = null;
        done = false;
    }

    public ToDoItem(int itemId, int groupId, String doneDate, boolean done, String endDate,
                    int toDoId, int rank, String name) {
        this.itemId = itemId;
        this.groupId = groupId;
        this.doneDate = doneDate;
        this.done = done;
        this.endDate = endDate;
        this.toDoId = toDoId;
        this.rank = rank;
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
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

    public int getToDoId() {
        return toDoId;
    }

    public void setToDoId(int toDoId) {
        this.toDoId = toDoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

