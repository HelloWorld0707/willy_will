package com.willy.will.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class GroupDBController {
    private DBAccess databaseAccess = null;
    private SQLiteDatabase readDatabase = null;

    public GroupDBController(Context context) {
        databaseAccess = new DBAccess(context);
    }
}
