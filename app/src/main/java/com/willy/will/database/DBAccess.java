package com.willy.will.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAccess extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBAccess(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE _CALENDER ( " +
                "calender_id INTEGER NOT NULL," +
                "calender_date TEXT NOT NULL," +
                "item_id INTEGER," +
                "PRIMARY KEY(calender_id) );"
        );

        db.execSQL("CREATE TABLE _GROUP(" +
                "group_id INTEGER NOT NULL," +
                "group_name TEXT NOT NULL," +
                "group_color TEXT NOT NULL," +
                "PRIMARY KEY(group_id) );"
        );

        db.execSQL("CREATE TABLE _ITEM(" +
                "item_id INTEGER NOT NULL," +
                "group_id INTEGER," +
                "item_name TEXT NOT NULL," +
                "item_important INTEGER," +
                "item_location_X TEXT," +
                "item_location_Y TEXT," +
                "done_date TEXT," +
                "start_day TEXT NOT NULL," +
                "end_day TEXT NOT NULL," +
                "to_do_id INTEGER," +
                "PRIMARY KEY(item_id) );"
        );

        db.execSQL("CREATE TABLE _LOOP_INFO (" +
                "loop_id INTEGER NOT NULL," +
                "to_do_id INTEGER NOT NULL," +
                "loop_week TEXT," +
                "PRIMARY KEY(loop_id, to_do_id) );"
        );
    }

    public SQLiteDatabase getDb(){ return this.getWritableDatabase(); }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}

