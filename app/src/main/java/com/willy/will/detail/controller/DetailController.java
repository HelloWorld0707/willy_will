package com.willy.will.detail.controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.detail.model.Item;
import java.util.ArrayList;
import java.util.List;
import static com.willy.will.main.view.MainActivity.dbHelper;

public class DetailController {
    
    private SQLiteDatabase db;

    public DetailController(){
        db = dbHelper.getReadableDatabase();
    }


    /** get Item by itemId from DB **/
    public Item getToDoItemByItemId(int itemId){
        String selectQuery =
                "SELECT item_name, longitude, latitude, done_date, start_date, end_date, group_name, group_color, calendar_date, loop_week, item_id, item.to_do_id, item.item_important, item.group_id, item.item_memo, item.user_place_name " +
                "FROM " +
                    "(SELECT * " +
                    "FROM _ITEM i, _CALENDAR c, _GROUP g " +
                    "WHERE i.item_id = c.item_id " +
                    "AND i.group_id = g.group_id) AS item " +
                    "LEFT OUTER JOIN _LOOP_INFO l " +
                "ON item.to_do_id = l.to_do_id " +
                "WHERE item.item_id = "+itemId+";";

        Cursor cursor = db.rawQuery(selectQuery,null);
        Item item = new Item();

        if(cursor.moveToFirst()){
            do {
                item.setItemName(cursor.getString(0));
                item.setLongitude(cursor.getString(1));
                item.setLatitude(cursor.getString(2));
                item.setDoneDate(cursor.getString(3));
                item.setStartDate(cursor.getString(4));
                item.setEndDate(cursor.getString(5));
                item.setGroupName(cursor.getString(6));
                item.setGroupColor(cursor.getString(7));
                item.setCalenderDate(cursor.getString(8));
                item.setLoopWeek(cursor.getString(9));
                item.setItemId(cursor.getInt(10));
                item.setTodoId(cursor.getInt(11));
                item.setImportant(cursor.getInt(12));
                item.setGroupId(cursor.getInt(13));
                item.setItemMemo(cursor.getString(14));
                item.setUserPlaceName(cursor.getString(15));
            }while (cursor.moveToNext());
        }
        return item;
    }
    /*~ get Item by itemId from DB */



    /** get getloopItem by itemId from DB **/
    public List<Item> getloopItem(int itemId, String startOfWeek, String endOfWeek){
        List<Item> list = new ArrayList<>();

        String selectQuery =
                "SELECT i.done_date , c.calendar_date " +
                        "FROM _ITEM i, _CALENDAR c " +
                        "WHERE i.item_id = c.item_id " +
                        "AND  c.calendar_date BETWEEN \""+startOfWeek+"\" AND \""+endOfWeek+"\"" + //수정
                        "AND i.to_do_id = (SELECT to_do_id " +
                        "FROM _ITEM " +
                        "WHERE item_id="+itemId+");";

        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                Item item = new Item();
                item.setDoneDate(cursor.getString(0));
                item.setCalenderDate(cursor.getString(1));
                list.add(item);
            }while (cursor.moveToNext());
        }
        return list;
    }
    /*~ get getloopItem by itemId from DB */


    /** delete item from _CALENDAR, _ITEM, _LOOP_INFO by to_do_id  **/
    public void deleteItemByTodoId(int todoId){
        String deleteLoopInfoQuery = "DELETE FROM _LOOP_INFO WHERE to_do_id ="+todoId+";";
        String deleteCalendarQuery = "DELETE FROM _CALENDAR WHERE item_id in (SELECT item_id FROM _ITEM WHERE to_do_id="+todoId+");";
        String deleteItemQuery = "DELETE FROM _ITEM WHERE to_do_id ="+todoId+";";
        db.execSQL(deleteLoopInfoQuery);
        db.execSQL(deleteCalendarQuery);
        db.execSQL(deleteItemQuery);
    }
    /*~ delete item from _CALENDAR, _ITEM, _LOOP_INFO by to_do_id  */


    /** get calendar date by item id**/
    public String getCalendarDateByItemId(int itemId){
        String calendarDate = null;

        String selectQuery =
                "SELECT calendar_date " +
                        "FROM _ITEM i, _CALENDAR c\n" +
                        "WHERE i.item_id = c.item_id\n" +
                        "AND i.item_id =" + itemId + ";";

        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                calendarDate = cursor.getString(0);
            }while (cursor.moveToNext());
        }
        return calendarDate;
    }



}
