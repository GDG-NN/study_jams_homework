package com.andrejjj.pocketsecretary.dbase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andrejjj on 23.04.2016.
 */

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 * This is a database helper for ToDo and Contacts database
 */
public class TodoSQLiteOpenHelper extends SQLiteOpenHelper {
    /**
     * @value Database version: you should increase this number when application is updated
     */
    private static final int DATABASE_VERSION = 1;
    /**
     * @value Database name
     */
    private static final String DATABASE_NAME = "dbTODO";
    /**
     * @value Table for events
     */
    private static final String EVENTS = "tbEvents";
    /**
     * @value Table for clients
     */
    private static final String CLIENTS = "tbClients";
    /**
     * @value Table for tasks
     */
    private static final String TASKS = "tbTasks";

    /**
     * @value String for creating of database
     */
//    private static final String DATABASE_CREATE = "create table "
//            + TABLE_COMMENTS + "(" + COLUMN_ID
//            + " integer primary key autoincrement, " + COLUMN_COMMENT
//            + " text not null);";

    public TodoSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
