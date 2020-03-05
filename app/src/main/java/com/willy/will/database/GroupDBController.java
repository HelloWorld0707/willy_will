package com.willy.will.database;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.willy.will.R;
import com.willy.will.common.model.Group;

import java.util.ArrayList;

public class GroupDBController {
    private SQLiteDatabase readDatabase = null;
    private SQLiteDatabase writeDatabase = null;

    public GroupDBController() {
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    public ArrayList<Group> getAllGroups(ArrayList<Group> arrayList, Resources resources) {
        Cursor cursor = readDatabase.query(resources.getString(R.string.group_table), null, null, null, null, null,  resources.getString(R.string.group_name_column)+ " ASC");
        Group curGroup = null;
        while(cursor.moveToNext()) {
            curGroup = new Group();
            curGroup.setGroupId(cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_id_column))));
            curGroup.setGroupName(cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_name_column))));
            curGroup.setGroupColor(cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_color_column))));
            arrayList.add(curGroup);
        }
        return arrayList;
    }
}
