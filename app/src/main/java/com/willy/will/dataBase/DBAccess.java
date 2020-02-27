package com.willy.will.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
                "calender_date TEXT NOT NULL," +
                "item_id INTEGER," +
                "PRIMARY KEY(calender_date, item_id) );"
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
                "item_location TEXT," +
                "done TEXT NOT NULL," +
                "PRIMARY KEY(item_id) );"
        );
        db.execSQL("CREATE TABLE _ROOF_INFO (" +
                "roof_id INTEGER NOT NULL," +
                "start_day INTEGER NOT NULL," +
                "end_day INTEGER NOT NULL," +
                "week INTEGER," +
                "PRIMARY KEY(roof_id) );"
        );

        db.execSQL("CREATE TABLE _ROOF_MAPPER (" +
                "item_id INTEGER NOT NULL," +
                "roof_id INTEGER NOT NULL," +
                "PRIMARY KEY(item_id, roof_id) )"
        );
    }

    public SQLiteDatabase getDB(){
        return getWritableDatabase();
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String create_at, String item, int price) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        // db.execSQL("INSERT INTO MONEYBOOK VALUES(null, '" + item + "', " + price + ", '" + create_at + "');");
        db.close();
    }

    public void update(String item, int price) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        // db.execSQL("UPDATE MONEYBOOK SET price=" + price + " WHERE item='" + item + "';");
        db.close();
    }

    public void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MONEYBOOK WHERE item='" + item + "';");
        db.close();
    }

    public List<String[]> getAll(String tableName) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String[]> result = new ArrayList<>();

        // Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        /*
        while (cursor.moveToNext()) {

            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getInt(2)
                    + "원 "
                    + cursor.getString(3)
                    + "\n";
        }
        */
        return result;
    }
}

