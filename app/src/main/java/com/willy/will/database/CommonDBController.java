package com.willy.will.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonDBController {

    private SQLiteDatabase readDatabase;
    private SQLiteDatabase writeDatabase;

    public CommonDBController() {
        readDatabase = DBAccess.getDbHelper().getReadableDatabase();
        writeDatabase = DBAccess.getDbHelper().getWritableDatabase();
    }

    public int getSeq(String tableName) {
        /** Set Column seq **/
        String[] columns = {
                "seq"
        };
        /* ~Set Column item_id */

        /** Set Clause (WHERE) **/
        String selection = "name = '" + tableName + "'";
        /* ~Set Clause (WHERE) */

        /** Read DB **/
        Cursor cursor = readDatabase.query(
                "sqlite_sequence",
                columns,
                selection, null, null, null, null);
        /* ~Read DB */

        /** Put data **/
        cursor.moveToNext();
        int seq = cursor.getInt(cursor.getColumnIndexOrThrow(columns[0]));
        /* ~Put data */

        return seq;
    }

}
