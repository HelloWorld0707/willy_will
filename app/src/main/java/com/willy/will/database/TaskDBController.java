package com.willy.will.database;

import android.content.ContentValues;
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
        String loopColumnQuery = resources.getString(R.string.loop_column_query);
        String endDateColumn = resources.getString(R.string.end_date_column);

        String query =
                "SELECT " +
                        itemIdColumn + ", " +
                        toDoIdColumn + ", " +
                        groupIdColumn + ", " +
                        groupNameColumn + ", " +
                        groupColorColumn + ", " +
                        itemNameColumn + ", " +
                        loopColumnQuery + ", " +
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
        int curToDoId = -1;
        Group curGroup = null;
        Task curTask = null;
        boolean loop = false;
        String dDayOrAchievement = null;
        while(cursor.moveToNext()) {
            curToDoId = cursor.getInt(cursor.getColumnIndexOrThrow(toDoIdColumn));

            curGroup = new Group(
                    cursor.getInt(cursor.getColumnIndexOrThrow(groupIdColumn)),
                    cursor.getString(cursor.getColumnIndexOrThrow(groupNameColumn)),
                    cursor.getString(cursor.getColumnIndexOrThrow(groupColorColumn))
            );

            loop = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.loop_column))) == 1 ? true : false ;

            if(loop) {
                dDayOrAchievement = getAchievementDays(
                        curToDoId,
                        simpleDateFormat.format(Calendar.getInstance().getTime())
                );
            }
            else {
                dDayOrAchievement = getDDay(
                        cursor.getString(cursor.getColumnIndexOrThrow(endDateColumn)),
                        Calendar.getInstance()
                );
            }

            curTask = new Task(
                    cursor.getInt(cursor.getColumnIndexOrThrow(itemIdColumn)),
                    curToDoId,
                    curGroup, cursor.getString(cursor.getColumnIndexOrThrow(itemNameColumn)),
                    dDayOrAchievement
            );

            taskList.add(curTask);
        }
        /* ~Put data in ArrayList */

        return taskList;
    }

    public String getAchievementDays(int toDoId, String today) {
        String achievementDays = null;

        String itemIdColumn = resources.getString(R.string.item_id_column);
        String doneDateColumn = resources.getString(R.string.done_date_column);

        String itemJoinCalendar = String.format(resources.getString(R.string.item_join_calendar_query), "'" + today + "'", toDoId);

        String calendarDate = String.format(resources.getString(R.string.strftime_function), resources.getString(R.string.calendar_date_column));

        String columnName = "con";

        String query =
                "SELECT ( " +
                    "( SELECT max(" + itemIdColumn + ") " +
                    "FROM " + itemJoinCalendar + " )" +
                " - " +
                    "( SELECT" +
                        " CASE WHEN c = 0" +
                            " THEN " +
                                "( SELECT " + itemIdColumn +
                                " FROM " + itemJoinCalendar +
                                " ORDER BY " + calendarDate + " ASC LIMIT 1 )" +
                                " - 1" +
                            " ELSE " + itemIdColumn +
                        " END " +
                    "FROM ( " +
                        "SELECT " + itemIdColumn + ", " + "count(" + itemIdColumn + ") AS c " +
                        "FROM " +
                            itemJoinCalendar +
                        " WHERE ( " + doneDateColumn + " IS NULL OR " + doneDateColumn + " = '' ) " +
                            "ORDER BY " + calendarDate + " DESC " +
                            "LIMIT 1 )" +
                        " )" +
                " ) AS " + columnName;

        /** Read DB **/
        Cursor cursor = readDatabase.rawQuery(query, null);
        /* ~Read DB */

        /** Put data **/
        int continuous = -1;
        if(cursor.moveToNext()) {
            continuous = cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
        }
        achievementDays = String.format(resources.getString(R.string.achievement_days), continuous);
        /* ~Put data */

        return achievementDays;
    }

    public String getDDay(String end_date, Calendar today) {
        String dDay = null;

        if (end_date != null && !end_date.equals("")) {
            final long ONE_DAY = 24 * 60 * 60 * 1000;
            try {
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                // Must be divided SEPARATELY
                long days = today.getTime().getTime() / ONE_DAY - simpleDateFormat.parse(end_date).getTime() / ONE_DAY;
                dDay = "D";
                if (days < 0L) {
                    dDay += days;
                } else if (days > 0L) {
                    dDay += ("+" + days);
                } else {
                    dDay += "-Day";
                }
            } catch (ParseException e) {
                e.printStackTrace();
                dDay = "";
            }
        } else {
            //dDayOrAchievement = null;
            dDay = "";
        }

        return dDay;
    }

    public void deleteTasks(String whereToDoIds, String whereItemIds) {
        /** Write DB (DELETE) **/
        // This ORDER is IMPORTANT
        int deletedRows = writeDatabase.delete(
                resources.getString(R.string.loop_info_table),
                whereToDoIds, null
        );
        Log.i("TaskDBController", "Deleting: Delete a loop information");
        deletedRows = writeDatabase.delete(resources.getString(R.string.calendar_table), whereItemIds, null);
        Log.i("TaskDBController", "Deleting: Delete " + deletedRows + " items of calendar");
        deletedRows = writeDatabase.delete(resources.getString(R.string.item_table), whereToDoIds, null);
        Log.i("TaskDBController", "Deleting: Delete " + deletedRows + " items");
        /* ~Write DB (DELETE) */
    }

    public void moveTasks(int groupId, String whereToDoIds) {
        /** Set the column name and the value **/
        ContentValues contentValues = new ContentValues();
        contentValues.put(resources.getString(R.string.group_id_column), groupId);
        /* ~Set the column name and the value */

        /** Write DB (UPDATE) **/
        int movedRows = writeDatabase.update(
                resources.getString(R.string.item_table),
                contentValues,
                whereToDoIds, null
        );
        Log.i("TaskDBController", "Moving: Move " + movedRows + " items");
        /* ~Write DB (UPDATE) */
    }

}
