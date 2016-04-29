package com.example.lilu.chronometr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lilu on 25.04.2016.
 */
class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 12);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        db.execSQL("create table timeTable ("
                + "_id integer primary key autoincrement,"
                + "date DATETIME,"
                + "startTime DATETIME,"
                + "startTimer text,"
                + "description text,"
                + "duration text,"
                + "isPos integer,"
                + "inProgress integer"+ ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("LOG_TAG", "Try drop table");
        db.execSQL("drop table timeTable;");
        db.execSQL("create table timeTable ("
                + "_id integer primary key autoincrement,"
                + "date DATETIME,"
                + "startTime DATETIME,"
                + "startTimer text,"
                + "description text,"
                + "duration text,"
                + "isPos integer,"
                + "inProgress integer"+ ");");

    }
}
