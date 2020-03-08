package com.willy.will.common.model;

public class Task {

    private int itemId;
    private int toDoId;
    private Group group;
    private String name;
    private String dDayOrAchievement;
    private boolean checked;

    public Task() {
        itemId = -1;
        toDoId = -1;
        group = null;
        name = null;
        dDayOrAchievement = null;
        checked = false;
    }

    public Task(int itemId, int toDoId, Group group, String name, String dDayOrAchievement) {
        this.itemId = itemId;
        this.toDoId = toDoId;
        this.group = group;
        this.name = name;
        this.dDayOrAchievement = dDayOrAchievement;
        this.checked = false;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getToDoId() {
        return toDoId;
    }

    public void setToDoId(int toDoId) {
        this.toDoId = toDoId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdDayOrAchievement() {
        return dDayOrAchievement;
    }

    public void setdDayOrAchievement(String dDayOrAchievement) {
        this.dDayOrAchievement = dDayOrAchievement;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
