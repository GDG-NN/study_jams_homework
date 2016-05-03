package betaru.dd.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by betaru on 02.05.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "my_dict";

    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "word";
    public static final String KEY_TRANS = "trans";
    public static final String KEY_COUNTER = "counter";
    public static final String KEY_DATE_CHECK = "date_check";

    private static final String DATABASE_NAME = "DDdb";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + " ("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_WORD + " text UNIQUE,"
                + KEY_TRANS + " text,"
                + KEY_COUNTER + " integer,"
                + KEY_DATE_CHECK + " DATETIME" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

}