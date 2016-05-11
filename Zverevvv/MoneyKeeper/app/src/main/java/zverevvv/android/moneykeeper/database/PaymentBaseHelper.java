package zverevvv.android.moneykeeper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zverevvv.android.moneykeeper.database.PaymentDbSchema.PaymentTable;

/**
 * Created by Vasily on 02.05.2016.
 */
public class PaymentBaseHelper extends SQLiteOpenHelper{
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "paymentBase.db";

    public PaymentBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PaymentTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                PaymentTable.Cols.UUID + ", " +
                PaymentTable.Cols.NAME + ", " +
                PaymentTable.Cols.DATE + ", " +
                PaymentTable.Cols.SUM + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
