package com.willy.will.search.controller;

import com.willy.will.main.model.ToDoItem;

import java.util.ArrayList;

public class SearchController {

    public ArrayList<ToDoItem> getToDoItems(ArrayList<ToDoItem> toDoList) {
        if(toDoList == null) {
            toDoList = new ArrayList<>();
        }
        else if(!toDoList.isEmpty()) {
            toDoList.clear();
        }

        //toDoListDB

        return toDoList;
    }

}
