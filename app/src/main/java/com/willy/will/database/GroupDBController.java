package com.willy.will.database;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.willy.will.R;
import com.willy.will.common.model.Group;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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
        contentValues.put(resources.getString(R.string.group_name_column), newGroup.getGroupName());
        contentValues.put(resources.getString(R.string.group_color_column), newGroup.getGroupColor());
        /* Set column names and values */

        /** Write DB (INSERT) **/
        long rowId = writeDatabase.insert(resources.getString(R.string.group_table), null, contentValues);
        Log.i("GroupDBController", "Adding: Add group " + rowId);
        /* ~Write DB (INSERT) */
    }

    public Queue<Integer> getToDoIdsByGroupId(int groupId) {
        Queue<Integer> toDoIds = new LinkedList<>();

        /** Set Column to_do_id **/
        String toDoIdColumn = resources.getString(R.string.to_do_id_column);
        String[] columns = {
                "DISTINCT " + toDoIdColumn
        };
        /* ~Set Column to_do_id */

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                resources.getString(R.string.item_table),
                columns,
                resources.getString(R.string.group_id_column) + " = " + groupId, null,
                null, null,
                null);
        /* ~Read DB */

        /** Put data in Queue **/
        while(cursor.moveToNext()) {
            toDoIds.offer(cursor.getInt(cursor.getColumnIndexOrThrow(toDoIdColumn)));
        }
        /* ~Put data in Queue */

        return toDoIds;
    }

    public void deleteGroup(int groupId) {
        /** Set WHERE **/
        String where = resources.getString(R.string.group_id_column) + " = " + groupId;
        /* ~Set WHERE */

        /** Write DB (DELETE) **/
        int deletedRows = writeDatabase.delete(resources.getString(R.string.group_table), where, null);
        Log.i("TaskDBController", "Deleting: Delete " + deletedRows + " group");
        /* ~Write DB (DELETE) */
    }

}
