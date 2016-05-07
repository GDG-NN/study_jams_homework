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
    Cursor cur;
    SQLiteDatabase db;

    public DB(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }


    // Get counter added words
    public int getCountAddedWords() {

        db = dbHelper.getReadableDatabase();

        cur = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        int cnt = cur.getCount();

        Log.d(LOG_TAG, String.format("count add: %s", cnt));

        cur.close();

        return cnt;
    }
    // Get random word for checking
    public String[] getCheckWord() {

        String[] result = new String[0];
//      select  _id, word, trans from my_dict
//      where date_check <= datetime('now', '-1 minute')
//      ORDER BY RANDOM() LIMIT 1;
        String sqlQuery = "select ?, word, trans from my_dict where date_check <= datetime('now', '-1 minute') ORDER BY RANDOM() LIMIT 1";
        String idCol = DBHelper.KEY_ID;
        String wordCol = DBHelper.KEY_WORD;
        String transCol = DBHelper.KEY_TRANS;
        String tableName = DBHelper.TABLE_NAME;
        String dateCheckCol = DBHelper.KEY_DATE_CHECK;

        cur = db.rawQuery(sqlQuery, new String[]{"_id"});

        Log.d(LOG_TAG, String.format("cur: %s", cur));

        if (cur != null) {
            if (cur.moveToFirst()) {
                int idColInd = cur.getColumnIndex(idCol);
                int id = cur.getInt(idColInd);
                int wordColInd = cur.getColumnIndex(wordCol);
                String word = cur.getString(wordColInd);
                int transColInd = cur.getColumnIndex(transCol);
                String trans = cur.getString(transColInd);

                Log.d(LOG_TAG, String.format("id: %s, word: %s, trans: %s", id, word, trans));
            }
        }


        cur.close();

        return result;
    }
    // Get counter learned words
    public int getCountLearWords() {
        db = dbHelper.getReadableDatabase();

        String selectCase = DBHelper.KEY_COUNTER + " > ?";
        String[] selectColmn = new String[] { "COUNT("+ DBHelper.KEY_ID +") AS c_all" };
        String[] selectArg = new String[] {"1"};
        int cnt = 0;


        cur = db.query(DBHelper.TABLE_NAME, selectColmn,
                selectCase, selectArg, null, null, null);

        if (cur != null) {
            if (cur.moveToFirst()) {
                int countColIndex = cur.getColumnIndex("c_all");
                cnt = cur.getInt(countColIndex);
            }
        }

        Log.d(LOG_TAG, String.format("count lear: %s", cnt));

        cur.close();

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
        Log.d(LOG_TAG, String.format("row inserted: %s, %s, rowID = %s", word, trans, rowID));

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
