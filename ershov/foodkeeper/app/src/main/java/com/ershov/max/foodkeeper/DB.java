package com.ershov.max.foodkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Maxim Ershov on 24.04.2016.
 */
public class DB {

    private static final int DB_VERSION = 2;
    public static final String DB_NAME = "FoodKeeperDB";
    public static final String DB_TABLE = "foodkeeper";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BUY_DATE = "buyDate";
    public static final String COLUMN_EXPIRE_DATE = "expireDate";
    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text, " +
                    COLUMN_BUY_DATE + " text, " +
                    COLUMN_EXPIRE_DATE + " text" +
                    ");";

    private DBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    public DB(Context context) {
        mDbHelper = new DBHelper(context);
        mCtx = context;
    }


    public void open() {
        mDbHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close() {
        if (mDbHelper != null) mDbHelper.close();
        if (mDb != null) mDb.close();
    }

    public Cursor getAllItems() {
        return mDb.query(DB_TABLE, null, null, null, null, null, null);
    }

    public void addRec(String txt_name, String txt_buy_date, String txt_end_date) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, txt_name);
        cv.put(COLUMN_BUY_DATE, txt_buy_date);
        cv.put(COLUMN_EXPIRE_DATE, txt_end_date);
        mDb.insert(DB_TABLE, null, cv);
    }

    public void addProduct(FoodItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, item.getName());
        cv.put(COLUMN_BUY_DATE, FoodItem.dateToString(item.getBuyDate(), FoodItem.DATE_FORMAT));
        cv.put(COLUMN_EXPIRE_DATE, FoodItem.dateToString(item.getExpireDate(),FoodItem.DATE_FORMAT));
        mDb.insert(DB_TABLE, null, cv);
    }

    public void delRec(long id) {
        mDb.delete(DB_TABLE, COLUMN_ID + " = " + id, null);
    }


    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

            //тестовая запись в пустую таблицу
            /*ContentValues cv = new ContentValues();
            cv.put(COLUMN_NAME, "test_name");
            cv.put(COLUMN_BUY_DATE, "test_buy_date");
            cv.put(COLUMN_EXPIRE_DATE, "test_end_date");
            db.insert(DB_TABLE, null, cv);*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            this.onCreate(db);
        }
    }



}
