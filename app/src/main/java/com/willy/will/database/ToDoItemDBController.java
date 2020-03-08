package com.willy.will.database;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.common.model.ToDoItem;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class ToDoItemDBController {

    private Resources resources;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;

    public ToDoItemDBController(Resources resources) {
        this.resources = resources;
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    public ArrayList<ToDoItem> searchToDoItems(ArrayList<ToDoItem> toDoItemList, String tempTable) {
        /** Initialize to-do item list **/
        if(toDoItemList == null) {
            toDoItemList = new ArrayList<>();
        }
        /* ~Initialize to-do item list */

        /** Read DB **/
        // Set columns
        String[] columns = {
                "*",
                resources.getString(R.string.done_column),
                resources.getString(R.string.loop_column)
        };

        // Set a column for GROUP BY
        String groupBy = resources.getString(R.string.to_do_id_column);
        // Set HAVING
        String having = "max(" + resources.getString(R.string.item_id_column) + ")";

        // Set order
        String order = resources.getString(R.string.to_do_item_order);

        Cursor cursor = readDatabase.query(
                tempTable, columns,
                null, null,
                groupBy, having,
                order);
        /* ~Read DB */

        /** Put data in ArrayList **/
        ToDoItem curToDoItem = null;
        int itemId = -1;
        int groupId = -1;
        String doneDate = null;
        boolean done = false;
        String endDate = null;
        int toDoId = -1;
        int rank = -1;
        String name = null;
        while(cursor.moveToNext()) {
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_id_column)));
            groupId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_id_column)));
            doneDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.done_date_column)));
            if(doneDate != null) {
                done = true;
            }
            else {
                done = false;
            }
            endDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.end_date_column)));
            toDoId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.to_do_id_column)));
            rank = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_important_column)));
            name = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_name_column)));

            curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name);
            toDoItemList.add(curToDoItem);
        }
        /* ~Put data in ArrayList */

        return toDoItemList;
    }

    /** ToDoItem from DB */
    public ArrayList<ToDoItem> mainToDoItems(ArrayList<ToDoItem> toDoItemList,
                                             String currentDate, int selectedGroup) {
        SQLiteDatabase readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        String selectQuery;
        // Initialization of ArrayList

        if(toDoItemList == null) {
            toDoItemList = new ArrayList<>();
        }


        //Read DB All Item
        if(selectedGroup == -1){
            selectQuery = "SELECT *," +
                    "CASE WHEN to_do_id IN ( SELECT to_do_id FROM _LOOP_INFO ) " +
                    "THEN (Select loop_week from _LOOP_INFO l where i.to_do_id = l.to_do_id) ELSE 0 END AS loop," +
                    "CASE done_date WHEN NULL OR \'\' THEN 0 ELSE 1 END AS done\n" +
                    "FROM _ITEM i\n" +
                    "WHERE date(start_date) <= \""+currentDate+"\"\n"+
                    "AND date(end_date) >= \""+currentDate+"\" \n"+
                    "ORDER BY done,item_important,item_name;";
        }

        //Read DB by selected group
        else {
            selectQuery =
                    "SELECT *," +
                            "CASE WHEN to_do_id IN ( SELECT to_do_id FROM _LOOP_INFO ) " +
                            "THEN (Select loop_week from _LOOP_INFO l where i.to_do_id = l.to_do_id) ELSE 0 END AS loop," +
                            "CASE done_date WHEN NULL OR \'\' THEN 0 ELSE 1 END AS done\n" +
                            "FROM _ITEM i\n" +
                            "WHERE date(start_date) <= \""+currentDate+"\"\n"+
                            "AND date(end_date) >= \""+currentDate+"\" \n"+
                            "AND group_id = "+selectedGroup+"\n"+
                            "ORDER BY done,item_important,item_name;";
        }
        Cursor cursor = readDatabase.rawQuery(selectQuery, null);
        Log.d("checkQuery",selectQuery);

        /** Put data in ArrayList **/
        ToDoItem curToDoItem = null;
        int itemId = -1;
        int groupId = -1;
        String doneDate = null;
        boolean done = false;
        String endDate = null;
        int toDoId = -1;
        int rank = -1;
        int dayNum = -1;
        String loopday = null;
        String name = null;

        while(cursor.moveToNext()) {
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_id_column)));
            groupId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_id_column)));
            doneDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.done_date_column)));
            if(doneDate != null) {
                done = true;
            }
            else {
                done = false;
            }
            endDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.end_date_column)));
            toDoId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.to_do_id_column)));
            rank = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_important_column)));

            name = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_name_column)));
           loopday = cursor.getString(cursor.getColumnIndex(resources.getString(R.string.loop_t_n)));
/*
           if(loopday.length() != 0){
               //loopday와 day of week 비교
               //set day of week
               Date d = java.sql.Date.valueOf(currentDate);
               Calendar cal = Calendar.getInstance();
               cal.setTime(d);
               dayNum = cal.get(Calendar.DAY_OF_WEEK)-1;

               for(int i = 0; i<loopday.length();i++){

                   int looppos = (int)loopday.charAt(i);
                   //add item
                   if(looppos == 49 && i == dayNum){
                       curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name);
                       toDoItemList.add(curToDoItem);
                   }
               }
           }
           else {*/
               curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name);
               toDoItemList.add(curToDoItem);
//           }
        }
        /* ~Put data in ArrayList */

        return toDoItemList;
    }
    /* ~ToDoItem from DB */

}
