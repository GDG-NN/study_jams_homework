package betaru.dd.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by betaru on 02.05.16.
 */
public class DB {

    private static final String LOG_TAG = "my_tag";
    DBHelper dbHelper;
    Context context;
    Cursor cursor;
    SQLiteDatabase db;

    public DB(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }


    // Get counter added words
    public int getCountAddedWords() {

        db = dbHelper.getReadableDatabase();

        cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        int cnt = cursor.getCount();

        Log.d(LOG_TAG, "count add: " + cnt);

        cursor.close();

        return cnt;
    }
    // Add word to dict
    public long addWord(String word, String trans){
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_WORD, word);
        cv.put(DBHelper.KEY_TRANS, trans);

        // Insert and get rowID
        long rowID = db.insert(DBHelper.TABLE_NAME, null, cv);
        Log.d(LOG_TAG, "row inserted, " + word + ", " + trans + " ID = " + rowID);

        return rowID;
    }

    public void deleteWord(int id) {
        db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, DBHelper.KEY_ID + "=" + id, null);
    }

    // Close all connection
    public void close() {
        dbHelper.close();
        db.close();
    }

}
