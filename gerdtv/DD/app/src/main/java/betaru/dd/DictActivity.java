package betaru.dd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DictActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etWord, etTrans;
    TextView txvGerResult, txvRusResult;

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


        etWord = (EditText) findViewById(R.id.etWord);
        etTrans = (EditText) findViewById(R.id.etTrans);

        txvGerResult = (TextView) findViewById(R.id.txv_ger_result);
        txvRusResult = (TextView) findViewById(R.id.txv_rus_result);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v) {

        // переменные для query
        String[] columns = null;
        String selection = null;
        String selectionArgs = null;

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // курсор
        Cursor c = null;

        // получаем данные из полей ввода
        String word = etWord.getText().toString();
        String trans = etTrans.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:
                Log.d(LOG_TAG, "--- Insert in mydict: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("word", word);
                cv.put("trans", trans);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("mydict", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                selection = "id = ?";
                selectionArgs = String.valueOf(rowID);
                c = db.query("mydict", null, selection, new String[]{selectionArgs}, null, null,
                        null);

                break;
//            case R.id.btnRead:
//                Log.d(LOG_TAG, "--- Rows in mydict: ---");
//                // делаем запрос всех данных из таблицы mydict, получаем Cursor
//                Cursor c = db.query("mydict", null, null, null, null, null, null);
//
//                // ставим позицию курсора на первую строку выборки
//                // если в выборке нет строк, вернется false
//                if (c.moveToFirst()) {
//
//                    // определяем номера столбцов по имени в выборке
//                    int idColIndex = c.getColumnIndex("id");
//                    int wordColIndex = c.getColumnIndex("word");
//                    int TransColIndex = c.getColumnIndex("trans");
//
//                    do {
//                        // получаем значения по номерам столбцов и пишем все в лог
//                        Log.d(LOG_TAG,
//                                "ID = " + c.getInt(idColIndex) +
//                                        ", word = " + c.getString(wordColIndex) +
//                                        ", trans = " + c.getString(TransColIndex));
//                        // переход на следующую строку
//                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
//                    } while (c.moveToNext());
//                } else
//                    Log.d(LOG_TAG, "0 rows");
//                c.close();
//                break;
//            case R.id.btnClear:
//                Log.d(LOG_TAG, "--- Clear mydict: ---");
//                // удаляем все записи
//                int clearCount = db.delete("mydict", null, null);
//                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
//                break;
        }

        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                // GER
                txvGerResult.setEnabled(true);
                txvGerResult.setText(c.getString(c.getColumnIndex("word")));

                // RUS
                txvRusResult.setEnabled(true);
                txvRusResult.setText(c.getString(c.getColumnIndex("trans")));

                //Clear Edit + set focus
                etWord.setText("");
                etWord.requestFocus();

                etTrans.setText("");

                Log.d(LOG_TAG, c.getString(c.getColumnIndex("word")));
            }
            c.close();
        } else
            Log.d(LOG_TAG, "Cursor is null");


        // закрываем подключение к БД
        dbHelper.close();
    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mydict ("
                    + "id integer primary key autoincrement,"
                    + "word text UNIQUE,"
                    + "trans text,"
                    + "counter integer,"
                    + "date_check DATETIME" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
