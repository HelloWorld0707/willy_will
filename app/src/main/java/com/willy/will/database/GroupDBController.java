package com.willy.will.database;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public ArrayList<Group> getAllGroups(ArrayList<Group> groupList) {
        if(groupList == null) {
            groupList = new ArrayList<>();
        }
        else if(!groupList.isEmpty()) {
            groupList.clear();
        }

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                resources.getString(R.string.group_table), null,
                null, null,
                null, null,
                resources.getString(R.string.group_id_column) + " ASC");
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

    public void insertGroup(Group newGroup) {
        /** Set column names and values **/
        ContentValues contentValues = new ContentValues();
        // temp
        Cursor cursor = readDatabase.rawQuery("SELECT (max(group_id) + 1) AS new_id FROM _GROUP", null);
        cursor.moveToNext();
        contentValues.put(resources.getString(R.string.group_id_column), cursor.getInt(cursor.getColumnIndexOrThrow("new_id")));
        contentValues.put(resources.getString(R.string.group_name_column), newGroup.getGroupName());
        contentValues.put(resources.getString(R.string.group_color_column), newGroup.getGroupColor());
        /* Set column names and values */

        /** Write DB (INSERT) **/
        long rowId = writeDatabase.insert(resources.getString(R.string.group_table), null, contentValues);
        Log.i("GroupDBController", "Adding: Add group " + rowId);
        /* ~Write DB (INSERT) */
    }

}
