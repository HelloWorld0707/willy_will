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

    public ArrayList<Task> getAllTasks(ArrayList<Task> taskList) throws ParseException {
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
        String doneDateColumn = resources.getString(R.string.done_date_column);
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
                        doneDateColumn + ", " +
                        endDateColumn +
                " FROM " + resources.getString(R.string.item_table) + " INNER JOIN " + resources.getString(R.string.group_table) + " USING (" + groupIdColumn + ")" +
                " GROUP BY " + toDoIdColumn + " HAVING max(" + itemIdColumn + ")" +
                " ORDER BY " +
                        groupIdColumn + ", " +
                        itemNameColumn;

        /** Read DB **/
        Cursor cursor = readDatabase.rawQuery(query, null);
        /* ~Read DB */

        /** Put data in ArrayList **/
        int curToDoId;
        Group curGroup;
        Task curTask;
        Calendar now = Calendar.getInstance();
        String curEndDate;
        boolean loop;
        String dDayOrAchievement;
        while(cursor.moveToNext()) {
            curToDoId = cursor.getInt(cursor.getColumnIndexOrThrow(toDoIdColumn));

            curGroup = new Group(
                    cursor.getInt(cursor.getColumnIndexOrThrow(groupIdColumn)),
                    cursor.getString(cursor.getColumnIndexOrThrow(groupNameColumn)),
                    cursor.getString(cursor.getColumnIndexOrThrow(groupColorColumn))
            );

            loop = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.loop_column))) == 1 ? true : false ;
            curEndDate = cursor.getString(cursor.getColumnIndexOrThrow(endDateColumn));
            if(loop) {
                dDayOrAchievement = getAchievement(
                        curToDoId,
                        curEndDate,
                        simpleDateFormat.format(now.getTime())
                );
            }
            else {
                dDayOrAchievement = getDDay(
                        cursor.getString(cursor.getColumnIndexOrThrow(doneDateColumn)),
                        curEndDate,
                        now
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

    public String getAchievement(int toDoId, String endDate, String today) throws ParseException {
        long endDateValue = simpleDateFormat.parse(endDate).getTime();
        long todayValue = simpleDateFormat.parse(today).getTime();

        if(endDateValue < todayValue) {
            return getAchievementRate(toDoId);
        }
        else {
            return getAchievementDays(toDoId, today);
        }
    }

    private String getAchievementRate(int toDoId) {
        String totalNumColumn = "total_num";
        String doneNumColumn = "done_num";

        String tempTable =
                "SELECT count()" +
                " FROM " + resources.getString(R.string.item_table) +
                " WHERE to_do_id = " + toDoId;
        String query =
                "SELECT " +
                    "( " + tempTable + " ) AS " + totalNumColumn + ", " +
                    "( " + tempTable + " AND " + resources.getString(R.string.done_date_column) + " IS NOT NULL ) AS " + doneNumColumn;

        /** Read DB **/
        Cursor cursor = readDatabase.rawQuery(query, null);
        /* ~Read DB */

        /** Put data **/
        double totalNum = 0;
        double doneNum = 0;
        if(cursor.moveToNext()) {
            totalNum = cursor.getDouble(cursor.getColumnIndexOrThrow(totalNumColumn));
            doneNum = cursor.getDouble(cursor.getColumnIndexOrThrow(doneNumColumn));
        }

        String achievementRate;
        if(totalNum > 0) {
            double rate = doneNum * 100 / totalNum;
            if(rate < 99) {
                rate = Math.ceil(rate);
            }
            else {
                rate = Math.floor(rate);
            }
            achievementRate = String.format(resources.getString(R.string.achievement_rate), (int) rate);
        }
        else {
            achievementRate = "";
        }
        /* ~Put data */

        return achievementRate;
    }

    private String getAchievementDays(int toDoId, String today) {
        String itemIdColumn = resources.getString(R.string.item_id_column);
        String calendarDateColumn = resources.getString(R.string.calendar_date_column);

        String beforeTodayQuery = String.format(resources.getString(R.string.before_today_query), "'" + today + "'");

        String query =
                "SELECT " +
                        itemIdColumn + ", " +
                        resources.getString(R.string.done_date_column) + ", " +
                        calendarDateColumn +
                        " FROM " + resources.getString(R.string.item_table) +
                        " INNER JOIN " + resources.getString(R.string.calendar_table) +
                        " USING (" + itemIdColumn + ")" +
                        " WHERE to_do_id = " + toDoId +
                        " AND " + beforeTodayQuery +
                        " ORDER BY " + calendarDateColumn;

        /** Read DB **/
        Cursor cursor = readDatabase.rawQuery(query, null);
        /* ~Read DB */

        /** Put data **/
        int daysBeforeToday = cursor.getCount();
        String achievementDays;
        if(daysBeforeToday > 0) {
            String tmpDonDate;
            int maxIndexOfNotDone = -1;
            while (cursor.moveToNext()) {
                tmpDonDate = cursor.getString(cursor.getColumnIndexOrThrow("done_date"));
                if ((tmpDonDate == null) || (tmpDonDate == "")) {
                    maxIndexOfNotDone = cursor.getPosition();
                }
            }

            int continuous;
            if (maxIndexOfNotDone == -1) {
                continuous = daysBeforeToday;
            } else {
                continuous = daysBeforeToday - (maxIndexOfNotDone + 1);
            }
            achievementDays = String.format(resources.getString(R.string.achievement_days), continuous);
        }
        else {
            achievementDays = String.format(resources.getString(R.string.achievement_days), 0);
        }
        /* ~Put data */

        return achievementDays;
    }

    public String getDDay(String doneDate, String endDate, Calendar today) {
        String dDay;

        if(doneDate != null) {
            dDay = resources.getString(R.string.done);
        }
        else {
            if(endDate != null && !endDate.equals("")) {
                final long ONE_DAY = 24 * 60 * 60 * 1000;
                try {
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    // Must be divided SEPARATELY
                    long days = today.getTime().getTime() / ONE_DAY - simpleDateFormat.parse(endDate).getTime() / ONE_DAY;
                    dDay = "D";
                    if(days < 0L) {
                        dDay += days;
                    } else if(days > 0L) {
                        dDay += ("+" + days);
                    } else {
                        dDay += "-Day";
                    }
                } catch(ParseException e) {
                    e.printStackTrace();
                    dDay = "";
                }
            } else {
                dDay = "";
            }
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
