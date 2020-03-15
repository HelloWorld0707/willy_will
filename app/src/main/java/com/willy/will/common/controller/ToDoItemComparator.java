package com.willy.will.common.controller;

import com.willy.will.common.model.ToDoItem;

import java.util.Comparator;

public class ToDoItemComparator implements Comparator<ToDoItem> {
    @Override
    public int compare(ToDoItem o1, ToDoItem o2) {
        int result;

        result = Integer.compare(o1.getDone() ? 1 : 0, o2.getDone() ? 1 : 0);
        if(result == 0) {
            result = Integer.compare(o1.getRank(), o2.getRank());
            if(result == 0) {
                result = o1.getName().compareTo(o2.getName());
            }
        }

        return result;
    }
}
