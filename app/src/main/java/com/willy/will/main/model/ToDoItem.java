package com.willy.will.main.model;

public class ToDoItem {
    private int rank;
    private String name;
    private String time;
    private int routine;
    private boolean done; //checkbox용

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {return time;}

    public void setTime(String date) {
        this.time = date;
    }

    public int getRoutine() {
        return routine;
    }

    public void setRoutine(int routine) {
        this.routine = routine;
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

