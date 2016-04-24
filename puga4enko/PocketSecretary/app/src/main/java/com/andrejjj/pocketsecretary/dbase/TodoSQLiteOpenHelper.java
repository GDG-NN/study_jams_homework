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
     * @value Table for audio notes
     */
    private static final String AUDIO = "tbAudios";
    // tbAudios table column names
    private static final String AUDIO_ID = "_id";
    private static final String AUDIO_PATH = "path";

    /**
     * @value Table for clients
     */
    private static final String CLIENTS = "tbClients";
    // tbClients table column names
    private static final String CLIENTS_ID = "_id";
    private static final String CLIENTS_FIRST_NAME = "firstName";
    private static final String CLIENTS_SECOND_NAME = "secondName";
    private static final String CLIENTS_PATRONIC = "patronic";
    private static final String CLIENTS_PHONES = "phones";
    private static final String CLIENTS_EMAIL = "email";
    private static final String CLIENTS_SKYPE = "skype";
    private static final String CLIENTS_TEXT_NOTES = "additionalTextNote";
    private static final String CLIENTS_AUDIO_NOTES = "additionalAudioNote";

    /**
     * @value Table for events
     */
    private static final String EVENTS = "tbEvents";
    // tbEvents table column names
    private static final String EVENTS_ID = "_id";
    private static final String EVENTS_DATE = "date";
    private static final String EVENTS_TASK = "task";
    private static final String EVENTS_CLIENT = "client";
    private static final String EVENTS_TEXT_DESCRIPTION = "textDescription";
    private static final String EVENTS_AUDIO_DESCRIPTION = "audioDescription";

    /**
     * @value Table for phones
     */
    private static final String PHONES = "tbPhones";
    // tbPhones table column names
    private static final String PHONES_ID = "_id";
    private static final String PHONES_NUMBER = "number";

    /**
     * @value Table for tasks
     */
    private static final String TASKS = "tbTasks";
    // tbTasks table column names
    private static final String TASKS_ID = "_id";
    private static final String TASKS_NAME = "name";

    public TodoSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create tbAudios
        String sql = "CREATE TABLE IF NOT EXISTS " + AUDIO + " ( "
                + AUDIO_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AUDIO_PATH + " TEXT)";
        db.execSQL(sql);

        //create tbTasks
        sql = "CREATE TABLE IF NOT EXISTS " + TASKS + " ( "
                + TASKS_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TASKS_NAME + " TEXT)";
        db.execSQL(sql);

        //create tbPhones
        sql = "CREATE TABLE IF NOT EXISTS " + PHONES + " ( "
                + PHONES_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PHONES_NUMBER + " TEXT)";
        db.execSQL(sql);

        //create tbClients
        sql = "CREATE TABLE IF NOT EXISTS " + CLIENTS + " ( "
                + CLIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CLIENTS_FIRST_NAME + " TEXT, "
                + CLIENTS_SECOND_NAME + " TEXT, "
                + CLIENTS_PATRONIC + " TEXT, "
                + CLIENTS_PHONES + " INTEGER, "
                + CLIENTS_EMAIL + " TEXT, "
                + CLIENTS_SKYPE + " TEXT, "
                + CLIENTS_TEXT_NOTES + " TEXT, "
                + CLIENTS_AUDIO_NOTES + " INTEGER, "
                + "FOREIGN KEY(" + CLIENTS_PHONES + ") REFERENCES " +  PHONES + "(" + PHONES_ID + "), "
                + "FOREIGN KEY(" + CLIENTS_AUDIO_NOTES + ") REFERENCES " +  AUDIO + "(" + AUDIO_ID + "))";
        db.execSQL(sql);

        //create tbEvents
        sql = "CREATE TABLE IF NOT EXISTS " + EVENTS + " ( "
                + EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EVENTS_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, "
                + EVENTS_TASK + " INTEGER, "
                + EVENTS_CLIENT + " INTEGER, "
                + EVENTS_TEXT_DESCRIPTION + " TEXT, "
                + EVENTS_AUDIO_DESCRIPTION + " INTEGER, "
                + "FOREIGN KEY(" + EVENTS_TASK + ") REFERENCES " +  TASKS + "(" + TASKS_ID + "), "
                + "FOREIGN KEY(" + EVENTS_CLIENT + ") REFERENCES " +  CLIENTS + "(" + CLIENTS_ID + "), "
                + "FOREIGN KEY(" + EVENTS_AUDIO_DESCRIPTION + ") REFERENCES " +  AUDIO + "(" + AUDIO_ID + "))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + AUDIO);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + PHONES);
        db.execSQL("DROP TABLE IF EXISTS " + TASKS);

        // Create tables again
        onCreate(db);
    }


}
