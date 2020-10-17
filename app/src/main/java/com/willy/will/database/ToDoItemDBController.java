package com.willy.will.database;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.common.model.ToDoItem;
import com.willy.will.search.model.SearchType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ToDoItemDBController {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private Resources resources;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;

    public ToDoItemDBController(Resources resources) {
        this.resources = resources;
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    public void insertDatesIntoCalendar(int itemId, String startDate, String endDate) throws ParseException {
        /** Set queries for inserting into _CALENDAR **/
        String calendarTableSql = "INSERT INTO " + resources.getString(R.string.calendar_table) + "(" +
                resources.getString(R.string.calendar_date_column) + ", " +
                resources.getString(R.string.item_id_column) +
                ") VALUES";

        String calendarDateStr;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(startDate));
        final int days = getDays(startDate, endDate);
        for(int i = 0; i < days; i++) {
            calendarDateStr = simpleDateFormat.format(calendar.getTime());

            calendarTableSql += "('" + calendarDateStr + "', " + itemId + "), ";

            calendar.add(Calendar.DATE, 1);
        }
        calendarTableSql = calendarTableSql.substring(0, calendarTableSql.lastIndexOf(", ")) + ";";
        /* ~Set queries for inserting into _CALENDAR */

        /** Write DB (INSERT) **/
        writeDatabase.execSQL(calendarTableSql);
        /* ~Write DB (INSERT) */
    }

    public void insertOneItem(int toDoId, int groupId, String itemName, int important,
                              String latitude, String longitude,
                              String startDate, String endDate,
                              String itemMemo, String userPlaceName) throws ParseException {
        String itemMemoColumn = resources.getString(R.string.item_memo_column);

        /** Set column names and values for inserting into _ITEM **/
        ContentValues contentValues = new ContentValues();
        contentValues.put(resources.getString(R.string.group_id_column), groupId);
        contentValues.put(resources.getString(R.string.item_name_column), itemName);
        contentValues.put(resources.getString(R.string.item_important_column), important);
        contentValues.put(resources.getString(R.string.latitude_column), latitude);
        contentValues.put(resources.getString(R.string.longitude_column), longitude);
        contentValues.putNull(resources.getString(R.string.done_date_column));
        contentValues.put(resources.getString(R.string.start_date_column), startDate);
        contentValues.put(resources.getString(R.string.end_date_column), endDate);
        contentValues.put(resources.getString(R.string.to_do_id_column), toDoId);
        contentValues.put(resources.getString(R.string.user_place_name_column), userPlaceName);
        if(itemMemo == null) {
            contentValues.putNull(itemMemoColumn);
        }
        else {
            contentValues.put(itemMemoColumn, itemMemo);
        }
        /* Set column names and values for inserting into _ITEM */

        /** Write DB (INSERT) **/
        long rowId = writeDatabase.insert(
                resources.getString(R.string.item_table),
                null,
                contentValues
        );
        Log.i("ToDoItemDBController", "Adding: Add item " + rowId + " to item table");
        /* ~Write DB (INSERT) */

        /** Set queries and write DB (INSERT) for inserting into _CALENDAR **/
        insertDatesIntoCalendar(getMaxItemId(), startDate, endDate);
        /* ~Set queries and write DB (INSERT) for inserting into _CALENDAR */
    }

    private int getDays(String startDate, String endDate) throws ParseException {
        // startDate, endDate 둘 다 0시 0분 0초 기준으로 계산하므로 그냥 차이를 구해도 오차 없음
        final long ONE_DAY = 24 * 60 * 60 * 1000;
        Date sdate = simpleDateFormat.parse(startDate);
        Date edate = simpleDateFormat.parse(endDate);
        int days = (int) (Math.abs(edate.getTime() - sdate.getTime()) / ONE_DAY + 1);
        return days;
    }

    public void insertItemsIntoItemAndCalendar(int toDoId, int groupId, String itemName,
                                              int important, String latitude, String longitude,
                                              String startDate, String endDate,
                                              String startCount, String endCount,
                                              ArrayList<String> checkedDays,
                                              String itemMemo, String userPlaceName) throws ParseException {
        if(itemMemo != null) {
            itemMemo = "'" + itemMemo + "'";
        }

        String itemTableSql = "INSERT INTO " + resources.getString(R.string.item_table) + "(" +
                resources.getString(R.string.group_id_column) + ", " +
                resources.getString(R.string.item_name_column) + ", " +
                resources.getString(R.string.item_important_column) + ", " +
                resources.getString(R.string.latitude_column) + ", " +
                resources.getString(R.string.longitude_column) + ", " +
                resources.getString(R.string.done_date_column) + ", " +
                resources.getString(R.string.start_date_column) + ", " +
                resources.getString(R.string.end_date_column) + ", " +
                resources.getString(R.string.to_do_id_column) + ", " +
                resources.getString(R.string.item_memo_column) + ", " +
                resources.getString(R.string.user_place_name_column) +
                ") VALUES";

        String calendarTableSql = "INSERT INTO " + resources.getString(R.string.calendar_table) + "(" +
                resources.getString(R.string.calendar_date_column) + ", " +
                resources.getString(R.string.item_id_column) +
                ") VALUES";

        String calendarDateStr;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(startCount));
        final int firstItemId = getMaxItemId();
        int itemId = firstItemId;
        final int days = getDays(startCount, endCount);
        for (int i = 0; i < days; i++) {
            calendarDateStr = simpleDateFormat.format(calendar.getTime());

            if (checkedDays.indexOf(convertDateToDay(calendarDateStr)) != -1) {
                itemTableSql += "(" +
                        groupId + ", " +
                        "'" + itemName + "', " +
                        "'" + important + "', " +
                        latitude + ", " +
                        longitude + ", " +
                        null + ", " +
                        "'" + startDate + "', " +
                        "'" + endDate + "', " +
                        toDoId + ", " +
                        itemMemo + ", " +
                        "'" + userPlaceName + "'" +
                        "), ";

                calendarTableSql += "('" + calendarDateStr + "', " + ++itemId + "), ";
            }

            calendar.add(Calendar.DATE, 1);
        }
        if(itemId > firstItemId) {
            itemTableSql = itemTableSql.substring(0, itemTableSql.lastIndexOf(", ")) + ";";
            calendarTableSql = calendarTableSql.substring(0, calendarTableSql.lastIndexOf(", ")) + ";";

            /** Write DB (INSERT) **/
            writeDatabase.execSQL(itemTableSql);
            writeDatabase.execSQL(calendarTableSql);
            /* ~Write DB (INSERT) */
        }
    }

    public void insertItems(int toDoId, int groupId, String itemName, int important,
                            String latitude, String longitude,
                            String startDate, String endDate,
                            String loopWeek, ArrayList<String> checkedDays,
                            String itemMemo, String userPlaceName) throws ParseException {
        /** Set queries and write DB (INSERT) for inserting into _ITEM and _CALENDAR **/
        insertItemsIntoItemAndCalendar(
                toDoId, groupId, itemName, important, latitude, longitude,
                startDate, endDate, startDate, endDate, checkedDays, itemMemo, userPlaceName
        );
        /* ~Set queries and write DB (INSERT) for inserting into _ITEM and _CALENDAR */

        /** Set column names and values for inserting into _LOOP_INFO **/
        ContentValues contentValues = new ContentValues();
        contentValues.put(resources.getString(R.string.to_do_id_column), toDoId);
        contentValues.put(resources.getString(R.string.loop_week_column), loopWeek);
        /* ~Set column names and values for inserting into _LOOP_INFO */

        /** Write DB (INSERT) **/
        long rowId = writeDatabase.insert(
                resources.getString(R.string.loop_info_table),
                null,
                contentValues
        );
        Log.i("ToDoItemDBController", "Adding: Add loop information " + rowId);
        /* ~Write DB (INSERT) */
    }

    private String convertDateToDay(String checkDate) {
        String loop_check_date=null;
        Date nDate = null;
        try {
            nDate = simpleDateFormat.parse(checkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum ) {
            case 1: loop_check_date = "일"; break;
            case 2: loop_check_date = "월"; break;
            case 3: loop_check_date = "화"; break;
            case 4: loop_check_date = "수"; break;
            case 5: loop_check_date = "목"; break;
            case 6: loop_check_date = "금"; break;
            case 7: loop_check_date = "토"; break;
        }
        return loop_check_date;
    }

    public void updateItems(int toDoId, int groupId, String itemName, int important,
                            String latitude, String longitude, String startDate, String endDate,
                            String itemMemo, String userPlaceName) {
        String itemMemoColumn = resources.getString(R.string.item_memo_column);
        String userPlaceNameColumn = resources.getString(R.string.user_place_name_column);

        ContentValues contentValues = new ContentValues();
        contentValues.put(resources.getString(R.string.group_id_column), groupId);
        contentValues.put(resources.getString(R.string.item_name_column), itemName);
        contentValues.put(resources.getString(R.string.item_important_column), important);
        contentValues.put(resources.getString(R.string.latitude_column), latitude);
        contentValues.put(resources.getString(R.string.longitude_column), longitude);
        contentValues.put(resources.getString(R.string.start_date_column), startDate);
        contentValues.put(resources.getString(R.string.end_date_column), endDate);
        if(itemMemo == null) {
            contentValues.putNull(itemMemoColumn);
        }
        else {
            contentValues.put(itemMemoColumn, itemMemo);
        }
        if(contentValues == null) {
            contentValues.putNull(userPlaceNameColumn);
        }
        else {
            contentValues.put(userPlaceNameColumn, userPlaceName);
        }

        int updatedRow = writeDatabase.update(
                resources.getString(R.string.item_table),
                contentValues,
                resources.getString(R.string.to_do_id_column) + " = " + toDoId, null
        );
        Log.i("ToDoItemDBController", "Update " + updatedRow + " items");
    }

    public void deleteDatesConditionally(String itemIdsStr, String startDate, String endDate) {
        /** Write DB (DELETE) **/
        int deletedRows = writeDatabase.delete(
                resources.getString(R.string.calendar_table),
                resources.getString(R.string.item_id_column) + " IN ( " + itemIdsStr + " ) AND " + String.format(resources.getString(R.string.deletion_condition_for_modification), startDate, endDate),
                null
        );
        Log.i("ToDoItemDBController", "Delete " + deletedRows + " calendar dates");
        /* ~Write DB (DELETE) */
    }

    public void deleteDatesConditionally(String itemIdsStr) {
        /** Write DB (DELETE) **/
        int deletedRows = writeDatabase.delete(
                resources.getString(R.string.calendar_table),
                resources.getString(R.string.item_id_column) + " IN ( " + itemIdsStr + " )",
                null
        );
        Log.i("ToDoItemDBController", "Delete " + deletedRows + " calendar dates");
        /* ~Write DB (DELETE) */
    }

    public void deleteItemsConditionally(int toDoId, String startDate, String endDate) {
        String itemIdsStr = getItemIdsStr(toDoId, startDate, endDate);
        if(itemIdsStr != null) {
            /** Write DB (DELETE) **/
            deleteDatesConditionally(itemIdsStr);

            int deletedRows = writeDatabase.delete(
                    resources.getString(R.string.item_table),
                    resources.getString(R.string.item_id_column) + " IN ( " + itemIdsStr + " )",
                    null
            );
            Log.i("ToDoItemDBController", "Delete " + deletedRows + " items");
            /* ~Write DB (DELETE) */
        }
    }

    public DateRange getDateRange(int toDoId) throws ParseException {
        String calendarDateColumn = resources.getString(R.string.calendar_date_column);

        String from = resources.getString(R.string.item_table) +
                        " INNER JOIN " + resources.getString(R.string.calendar_table) +
                        " USING (" + resources.getString(R.string.item_id_column) + ")";
        String[] columns = {
                "min(" + calendarDateColumn + ") AS MIN",
                "max(" + calendarDateColumn + ") AS MAX"
        };

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                from,
                columns,
                resources.getString(R.string.to_do_id_column) + " = " + toDoId,
                null, null, null, null
        );
        /* ~Read DB */

        /** Put data **/
        DateRange dateRange = new DateRange();
        cursor.moveToNext();
        dateRange.setMinValue(cursor.getString(cursor.getColumnIndexOrThrow(DateRange.MIN)));
        dateRange.setMaxValue(cursor.getString(cursor.getColumnIndexOrThrow(DateRange.MAX)));
        /* ~Put data */

        return dateRange;
    }

    public ArrayList<ToDoItem> searchToDoItems(ArrayList<ToDoItem> toDoItemList,
                                               SearchType searchType,
                                               String[] columns,
                                               String tempTable,
                                               String today) {
        /** Initialize to-do item list **/
        if(toDoItemList == null) {
            toDoItemList = new ArrayList<>();
        }
        /* ~Initialize to-do item list */

        /** Read DB **/
        // Set WHERE, HAVING
        String where = null;
        String having = null;
        if(searchType == SearchType.TO_CURRENT_DATE) {
            where = resources.getString(R.string.calendar_date_column) + " <= " + today;
            having = "max(" + resources.getString(R.string.calendar_date_column) + ")";
        }
        else if(searchType == SearchType.AFTER_CURRENT_DATE) {
            where = resources.getString(R.string.calendar_date_column) + " > " + today;
            having = "min(" + resources.getString(R.string.calendar_date_column) + ")";
        }
        else {
            Log.e("ToDoItemDBController", "Search: Wrong search type");
        }

        // Set GROUP BY
        String groupBy = resources.getString(R.string.to_do_id_column);

        Cursor cursor = readDatabase.query(
                tempTable, columns,
                where, null,
                groupBy, having,
                null);
        /* ~Read DB */

        /** Put data in ArrayList **/
        ToDoItem curToDoItem;
        while(cursor.moveToNext()) {
            curToDoItem = new ToDoItem();
            curToDoItem.setToDoId(cursor.getInt(cursor.getColumnIndexOrThrow(columns[0])));
            curToDoItem.setItemId(cursor.getInt(cursor.getColumnIndexOrThrow(columns[1])));
            curToDoItem.setDone(cursor.getInt(cursor.getColumnIndexOrThrow(columns[2])) == 1 ? true : false);
            curToDoItem.setRank(cursor.getInt(cursor.getColumnIndexOrThrow(columns[3])));
            curToDoItem.setName(cursor.getString(cursor.getColumnIndexOrThrow(columns[4])));
            curToDoItem.setColor(cursor.getString(cursor.getColumnIndexOrThrow(columns[5])));
            curToDoItem.setLoop(cursor.getInt(cursor.getColumnIndexOrThrow(columns[6])));
            curToDoItem.setEndDate(cursor.getString(cursor.getColumnIndexOrThrow(columns[7])));
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
        else if(!toDoItemList.isEmpty()) {
            toDoItemList.clear();
        }


        //Read DB All Item
        if(selectedGroup == -1){
            selectQuery =
                    "SELECT i.item_id, i.group_id, i.item_name, i.item_important, i.done_date," +
                    "i.start_date,i.end_date,i.to_do_id,i.item_memo,c.calendar_date,g.group_color,i.user_place_name, \n"+
                    "CASE WHEN to_do_id IN ( SELECT to_do_id FROM _LOOP_INFO ) THEN 1 ELSE 0 END AS lp," +
                    "CASE WHEN done_date IS NULL OR \'\' THEN 0 ELSE 1 END AS done\n" +
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
                            "i.start_date,i.end_date,i.to_do_id,i.item_memo,c.calendar_date,g.group_color,i.user_place_name, \n"+
                            "CASE WHEN to_do_id IN ( SELECT to_do_id FROM _LOOP_INFO ) THEN 1 ELSE 0 END AS lp," +
                            "CASE WHEN done_date IS NULL OR \"\" THEN 0 ELSE 1 END AS done\n" +
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
        String itemMemo = null;
        String userPlaceName = null;


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
            itemMemo = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_memo_column)));
            userPlaceName = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.user_place_name_column)));
            Log.d("checkQuery","******"+loop+"********");

            curToDoItem = new ToDoItem(itemId, groupId, doneDate, done, endDate, toDoId, rank, name, color, loop, itemMemo, userPlaceName);
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

    public int getToDoId(int itemId) {
        String toDoIdColumn = resources.getString(R.string.to_do_id_column);
        String itemTable = resources.getString(R.string.item_table);

        String sql;
        if(itemId == resources.getInteger(R.integer.max_to_do_id_request)) {
            sql = "SELECT max(" + resources.getString(R.string.to_do_id_column) + ") as " + toDoIdColumn +
                    " FROM " + itemTable;
        }
        else {
            sql = "SELECT " + toDoIdColumn +
                    " FROM " + itemTable +
                    " WHERE " + resources.getString(R.string.item_id_column) + " IS " + itemId;
        }

        /** Read DB **/
        Cursor cursor = readDatabase.rawQuery(sql, null);
        /* ~Read DB */

        /** Put data **/
        cursor.moveToNext();
        int toDoId = cursor.getInt(cursor.getColumnIndexOrThrow(toDoIdColumn));
        /* ~Put data */

        return toDoId;
    }

    public int getMaxItemId() {
        /** Set Column item_id **/
        String itemIdColumn = resources.getString(R.string.item_id_column);
        String[] columns = {
                "max(" + itemIdColumn + ") AS " + itemIdColumn
        };
        /* ~Set Column item_id */

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                resources.getString(R.string.item_table),
                columns,
                null, null, null, null, null);
        /* ~Read DB */

        /** Put data **/
        cursor.moveToNext();
        int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(itemIdColumn));
        /* ~Put data */

        return itemId;
    }

    public String getItemIdsStr(int toDoId, String startDate, String endDate) {
        /** Set Column item_id **/
        String itemIdColumn = resources.getString(R.string.item_id_column);
        String[] columns = {
                itemIdColumn
        };
        /* ~Set Column item_id */

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                resources.getString(R.string.item_table) + " INNER JOIN " + resources.getString(R.string.calendar_table) + " USING (" + itemIdColumn + ")",
                columns,
                resources.getString(R.string.to_do_id_column) + " = " + toDoId + " AND " + String.format(resources.getString(R.string.deletion_condition_for_modification), startDate, endDate),
                null, null, null, null);
        /* ~Read DB */

        /** Put data **/
        int[] itemIds = new int[cursor.getCount()];
        while(cursor.moveToNext()) {
            itemIds[cursor.getPosition()] = cursor.getInt(cursor.getColumnIndexOrThrow(itemIdColumn));
        }

        String itemIdsStr = null;
        if((itemIds != null) && (itemIds.length > 0)) {
            itemIdsStr = "";
            for (int itemId : itemIds) {
                itemIdsStr += (itemId + ", ");
            }
            itemIdsStr = itemIdsStr.substring(0, itemIdsStr.lastIndexOf(", "));
        }
        /* ~Put data */

        return itemIdsStr;
    }

}
