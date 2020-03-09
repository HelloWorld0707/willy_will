package com.willy.will.database;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.R;

import java.util.ArrayList;
import java.util.List;

public class DateDBController {
    private Resources resources;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;

    public DateDBController(Resources resources) {
        this.resources = resources;
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    // get Date as month
    public List<calendarItem> getMonthItemByDate(int yyyy, int mm, int dd){
        int itemId;
        String calendarDate;

        String month = (mm < 10)? "0"+mm : ""+mm;
        String day = (dd < 10)? "0"+dd : ""+dd;

        String targetDate = yyyy+"-"+month+"-"+day;
        String qry =
                "SELECT "+resources.getString(R.string.item_id_column)+","+resources.getString(R.string.calendar_date_column)+ " " +
                "FROM "+resources.getString(R.string.calendar_table)+" " +
                "WHERE "+resources.getString(R.string.calendar_date_column)+" ='"+targetDate+"';";

        Cursor cursor = readDatabase.rawQuery(qry, null);
        Log.d("checkQuery",qry);

        ArrayList<calendarItem> items = new ArrayList<calendarItem>();
        while(cursor.moveToNext()) {
            itemId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_id_column)));
            calendarDate = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.calendar_date_column)));

            items.add(new calendarItem(itemId, calendarDate));
        }
        return items;
    }

    public class calendarItem{
        private int item_id;
        private String calendarDate;
        public calendarItem(int item_id, String calendarDate){
            this.item_id = item_id;
            this.calendarDate = calendarDate;
        }

        public int getItemId(){
            return this.item_id;
        }

        public String getCalendarDate(){
            return this.calendarDate;
        }
    }

    /** get item n grroup info */
    public ItemNGroup getItemNGroupByItemId(int targetItemId){
        String qry =
                "SELECT " +
                        resources.getString(R.string.item_id_column)+","+
                        resources.getString(R.string.item_table)+"."+resources.getString(R.string.group_id_column)+","+
                        resources.getString(R.string.item_name_column)+","+
                        resources.getString(R.string.group_color_column)+","+
                        resources.getString(R.string.group_name_column)+ " " +
                "FROM " + resources.getString(R.string.item_table) + " INNER JOIN " + resources.getString(R.string.group_table) + " " +
                "ON " + resources.getString(R.string.item_table)+"."+resources.getString(R.string.group_id_column) + "=" + resources.getString(R.string.group_table) + "." + resources.getString(R.string.group_id_column) + " " +
                "WHERE " + resources.getString(R.string.item_id_column) + " = '" + targetItemId + "';";


        Cursor cursor = readDatabase.rawQuery(qry, null);
        Log.d("checkQuery",qry);

        ItemNGroup item = null;
        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_id_column)));
            int groupId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_id_column)));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.item_name_column)));
            String groupColor = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_color_column)));
            String groupName = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_name_column)));

            item = new ItemNGroup(itemId, groupId, itemName, groupColor, groupName);
        }
        return item;
    }

    public class ItemNGroup{
        private int item_id;
        private int group_id;
        private String item_name;
        private String group_color;
        private String group_name;

        public ItemNGroup(int item_id, int group_id, String item_name, String group_color, String group_name) {
            this.item_id = item_id;
            this.group_id = group_id;
            this.item_name = item_name;
            this.group_color = group_color;
            this.group_name = group_name;
        }

        public int getItem_id() {
            return item_id;
        }

        public int getGroup_id() {
            return group_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public String getGroup_color() {
            return group_color;
        }

        public String getGroup_name() {
            return group_name;
        }
    }
}
