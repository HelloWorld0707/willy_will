package com.willy.will.database;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.common.model.Group;
import com.willy.will.common.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TaskDBController {

    private SimpleDateFormat simpleDateFormat = null;

    private Resources resources;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;

    public TaskDBController(Resources resources) {
        this.resources = resources;
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public ArrayList<Task> getAllTasks(ArrayList<Task> taskList) {
        if(taskList == null) {
            taskList = new ArrayList<>();
        }
        else if(!taskList.isEmpty()) {
            taskList.clear();
        }

        String itemIdColumn = resources.getString(R.string.item_id_column);
        String toDoIdColumn = resources.getString(R.string.to_do_id_column);
        String groupIdColumn = resources.getString(R.string.group_id_column);
        String groupNameColumn = resources.getString(R.string.group_name_column);
        String groupColorColumn = resources.getString(R.string.group_color_column);
        String itemNameColumn = resources.getString(R.string.item_name_column);
        String endDateColumn = resources.getString(R.string.end_date_column);

        String query =
                "SELECT " +
                        itemIdColumn + ", " +
                        toDoIdColumn + ", " +
                        groupIdColumn + ", " +
                        groupNameColumn + ", " +
                        groupColorColumn + ", " +
                        itemNameColumn + ", " +
                        endDateColumn + " " +
                "FROM " + resources.getString(R.string.item_table) + " INNER JOIN " + resources.getString(R.string.group_table) + " USING (" + groupIdColumn + ") " +
                "GROUP BY " + toDoIdColumn + " HAVING max(" + itemIdColumn + ") " +
                "ORDER BY " +
                        groupIdColumn + ", " +
                        itemNameColumn;

        /** Read DB **/
        Cursor cursor = readDatabase.rawQuery(query, null);
        /* ~Read DB */

        /** Put data in ArrayList **/
        Group curGroup = null;
        Task curTask = null;
        String end_date = null;
        String dDayOrAchievement = null;
        while(cursor.moveToNext()) {
            curGroup = new Group(
                    cursor.getInt(cursor.getColumnIndexOrThrow(groupIdColumn)),
                    cursor.getString(cursor.getColumnIndexOrThrow(groupNameColumn)),
                    cursor.getString(cursor.getColumnIndexOrThrow(groupColorColumn))
            );

            end_date = cursor.getString(cursor.getColumnIndexOrThrow(endDateColumn));

            if(end_date != null && !end_date.equals("")) {
                try {
                    long days = ( Calendar.getInstance().getTime().getTime() - simpleDateFormat.parse(end_date).getTime() ) / (24 * 60 * 60 * 1000);
                    if(days < 0L) {
                        dDayOrAchievement = "D" +  days;
                    }
                    else if(days > 0L) {
                        dDayOrAchievement = "D+" +  days;
                    }
                    else {
                        dDayOrAchievement = "D-day";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    dDayOrAchievement = "";
                }
            }
            else {
                //dDayOrAchievement = null;
                dDayOrAchievement = "";
            }

            curTask = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemIdColumn)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(toDoIdColumn)),
                    curGroup, cursor.getString(cursor.getColumnIndexOrThrow(itemNameColumn)),
                    dDayOrAchievement
            );
            taskList.add(curTask);
        }
        /* ~Put data in ArrayList */

        return taskList;
    }

    public void deleteTasks(String whereToDoIds, String whereItemIds) {
        /** Write DB (Delete) **/
        // This ORDER is IMPORTANT
        int deletedRows =  writeDatabase.delete(resources.getString(R.string.loop_info_table), whereToDoIds, null);
        Log.i("TaskDBController", "Deleting: Delete a loop information");
        deletedRows = writeDatabase.delete(resources.getString(R.string.calendar_table), whereItemIds, null);
        Log.i("TaskDBController", "Deleting: Delete " + deletedRows + " items of calendar");
        deletedRows = writeDatabase.delete(resources.getString(R.string.item_table), whereToDoIds, null);
        Log.i("TaskDBController", "Deleting: Delete " + deletedRows + " items");
        /* ~Write DB (Delete) */
    }

}
