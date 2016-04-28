package com.mixasr.listwishes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "listwishes.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        ListTable.onCreate(database);
        ListElementsTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ListTable.onUpgrade(database, oldVersion, newVersion);
        ListElementsTable.onUpgrade(database, oldVersion, newVersion);
    }
}

