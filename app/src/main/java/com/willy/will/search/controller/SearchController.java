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

    /*
    SELECT * FROM _ITEM WHERE
                    그룹 조건   (group_id = 0 OR group_id = 1 OR group_id = 2) AND
                    완료 조건   (strftime('%d', done_date) >= strftime('%d', '2020-02-02') AND strftime('%d', done_date) <= strftime('%d', '2020-02-24')) AND
                    반복 조건   (to_do_id NOT IN(SELECT to_do_id FROM _LOOP_INFO)) AND
                    시작 날짜   (strftime('%d', start_date) >= strftime('%d', '2020-02-01') AND strftime('%d', start_date) <= strftime('%d', '2020-02-09')) AND
                    종료 날짜   (strftime('%d', end_date) >= strftime('%d', '2020-02-01') AND strftime('%d', end_date) <= strftime('%d', '2020-02-08'));
     */

}
