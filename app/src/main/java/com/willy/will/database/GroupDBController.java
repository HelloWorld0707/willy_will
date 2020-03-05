package com.willy.will.database;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.willy.will.R;
import com.willy.will.common.model.Group;

import java.util.ArrayList;

public class GroupDBController {

    private Resources resources;
    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;

    public GroupDBController(Resources resources) {
        this.resources = resources;
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    public ArrayList<Group> getAllGroups() {
        ArrayList<Group> groupList = new ArrayList<>();

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                resources.getString(R.string.group_table), null,
                null, null,
                null, null,
                resources.getString(R.string.group_name_column)+ " ASC");
        /* ~Read DB */

        /** Put data in ArrayList **/
        Group curGroup = null;
        int groupId = -1;
        String groupName = null;
        String groupColor = null;
        while(cursor.moveToNext()) {
            groupId = cursor.getInt(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_id_column)));
            groupName = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_name_column)));
            groupColor = cursor.getString(cursor.getColumnIndexOrThrow(resources.getString(R.string.group_color_column)));

            curGroup = new Group(groupId, groupName, groupColor);
            groupList.add(curGroup);
        }
        /* ~Put data in ArrayList */

        return groupList;
    }

}
