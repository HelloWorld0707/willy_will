package com.willy.will.main.model;


import android.graphics.drawable.Drawable;

public class ToDoItem {
    private Drawable rank;
    private String name;
    private String time;
    private Drawable routine;
    private boolean done; //checkbox용

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {return time;}

    public void setTime(String date) {
        this.time = date;
    }

    public Drawable getRoutine() {
        return routine;
    }

    public void setRoutine(Drawable routine) {
        this.routine = routine;
    }

    public Drawable getRank() {
        return rank;
    }

    public void setRank(Drawable rank) {
        this.rank = rank;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

