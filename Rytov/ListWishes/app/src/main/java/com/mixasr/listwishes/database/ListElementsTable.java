package com.mixasr.listwishes.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ListElementsTable {

    public static final String TABLE_ELEMENT = "element";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIST_ID = "list_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DONE = "done";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ELEMENT
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_LIST_ID + " integer not null, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_DONE + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int newVersion, int oldVersion) {
        Log.w(ListTable.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ELEMENT);
        onCreate(database);
    }
}
