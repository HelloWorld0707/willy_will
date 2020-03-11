package com.willy.will.database;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.common.model.ToDoItem;

import java.text.SimpleDateFormat;
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
                resources.getString(R.string.done_column_query),
                resources.getString(R.string.loop_column_query)
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
        String color = null;
        int loop = -1;
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
            color = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_color_column)));
            loop = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.loop_column)));

            curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name, color, loop);
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
            selectQuery =
                    "SELECT i.item_id, i.group_id, i.item_name, i.item_important, i.done_date," +
                    "i.start_date,i.end_date,i.to_do_id,c.calendar_date,g.group_color, \n"+
                    "CASE WHEN to_do_id IN ( SELECT to_do_id FROM _LOOP_INFO ) THEN 1 ELSE 0 END AS lp," +
                    "CASE done_date WHEN NULL OR \'\' THEN 0 ELSE 1 END AS done\n" +
                    "FROM _ITEM i, _CALENDAR c,_GROUP g \n" +
                    "WHERE i.item_id = c.item_id \n" +
                    "AND i.group_id = g.group_id \n"+
                    "AND date(c.calendar_date) = \""+currentDate+"\" \n"+
                    "ORDER BY done,i.item_important,i.item_name;";
        }

        //Read DB by selected group
        else {
            selectQuery =
                    "SELECT i.item_id, i.group_id, i.item_name, i.item_important, i.done_date," +
                            "i.start_date,i.end_date,i.to_do_id,c.calendar_date,g.group_color, \n"+
                            "CASE WHEN to_do_id IN ( SELECT to_do_id FROM _LOOP_INFO ) THEN 1 ELSE 0 END AS lp," +
                            "CASE done_date WHEN NULL OR \'\' THEN 0 ELSE 1 END AS done\n" +
                            "FROM _ITEM i, _CALENDAR c,_GROUP g \n" +
                            "WHERE i.item_id = c.item_id \n" +
                            "AND i.group_id = g.group_id \n"+
                            "AND date(c.calendar_date) = \""+currentDate+"\" \n" +
                            "AND i.group_id = \""+selectedGroup+"\"\n"+
                            "ORDER BY done,i.item_important,i.item_name;";
            //(Select loop_week from _LOOP_INFO l where i.to_do_id = l.to_do_id)(loop id)
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
        int loop = -1;
        String name = null;
        String color = null;


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
            color = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_color_column)));
            loop = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.loop_t_n)));
            Log.d("checkQuery","******"+loop+"********");


            curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name, color, loop);
            toDoItemList.add(curToDoItem);
        }
        /* ~Put data in ArrayList */

        return toDoItemList;
    }
    /* ~ToDoItem from DB */

    /** Update DoneDate */
    public void updateDB(int itemId,boolean activated){
        //set TodayDate
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(date.getTime());

        if(activated) {
            writeDatabase.execSQL("update _ITEM \n" +
                                  "set done_date=\"" + today + "\" \n" +
                                  "where item_id=\"" + itemId + "\"; ");
        }
        else{
            writeDatabase.execSQL("update _ITEM \n" +
                                  "set done_date = NULL \n" +
                                  "where item_id=\"" + itemId + "\"; ");
        }
    }
    /* ~Update DoneDate */
}
