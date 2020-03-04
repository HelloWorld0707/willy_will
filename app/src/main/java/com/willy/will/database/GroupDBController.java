package com.willy.will.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.willy.will.common.model.Group;

import java.util.ArrayList;

public class GroupDBController {
    private SQLiteDatabase readDatabase = null;
    private SQLiteDatabase writeDatabase = null;

    public GroupDBController() {
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    public ArrayList<Group> getAllGroups(ArrayList<Group> arrayList) {
        Cursor cursor = readDatabase.query("_GROUP", null, null, null, null, null, "group_name ASC");
        Group curGroup = null;
        while(cursor.moveToNext()) {
            curGroup = new Group();
            curGroup.setGroupId(cursor.getInt(cursor.getColumnIndexOrThrow("group_id")));
            curGroup.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow("group_name")));
            curGroup.setGroupColor(cursor.getString(cursor.getColumnIndexOrThrow("group_color")));
            arrayList.add(curGroup);
        }
        return arrayList;
    }
}
