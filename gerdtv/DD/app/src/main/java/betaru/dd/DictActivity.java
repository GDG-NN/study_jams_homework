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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etWord, etTrans;
    TextView txvGerResult, txvRusResult, txvArticle;

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
        txvArticle = (TextView) findViewById(R.id.txv_article);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v) {

        // переменные для query
        String[] columns = null;
        String selection = null;
        String selectionArgs = null;
        String chStatus;

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // курсор
        Cursor c = null;

        // получаем данные из полей ввода
        String word = etWord.getText().toString();
        chStatus = validateText(word, "GE");

        if ( chStatus != null) {
            etWord.setError(getResources().getString(R.string.txv_word) + " " + chStatus);
            return;
        }


        String trans = etTrans.getText().toString();
        chStatus = validateText(trans, "RU");

        if ( chStatus != null) {
            etTrans.setError(getResources().getString(R.string.txv_trans) + " " + chStatus);
            return;
        }


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
        }

        if (c != null) {
            if (c.moveToFirst()) {
                String str, art;
                // GER
                str = c.getString(c.getColumnIndex("word"));

                Pattern pattern = Pattern.compile("^(der|die|das)(.+)");
                Matcher matcher = pattern.matcher(str);
                if (matcher.find())
                {
                    art = matcher.group(1);
                    str = str.replace(art, "");
                    switch (art) {
                        case "der":
                            txvArticle.setText(art);
                            txvArticle.setTextColor(getResources().getColor(R.color.colorArticleDer));
                            break;
                        case "die":
                            txvArticle.setText(art);
                            txvArticle.setTextColor(getResources().getColor(R.color.colorArticleDie));
                            break;
                        case "das":
                            txvArticle.setText(art);
                            txvArticle.setTextColor(getResources().getColor(R.color.colorArticleDas));
                            break;
                    }

                }
                else
                {
                    txvArticle.setText("");
                    txvArticle.setEnabled(false);
                }

                txvGerResult.setText(str);
                txvGerResult.setEnabled(true);

                // RUS
                txvRusResult.setText(c.getString(c.getColumnIndex("trans")));
                txvRusResult.setEnabled(true);


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

    private String validateText(String str, String langStr) {
        String errMsg = null;
        // GE Regexp
        String geStrReg = "^[a-zA-Z ]+$";

        // RU Regexp
        String ruStrReg = "^[А-яЁё ][-А-яЁё ]+$";


        if (str.length() == 0)
             errMsg = getResources().getString(R.string.emsg_edit_required);
//        else if (!str.matches(geStrReg) || !str.matches(ruStrReg)) {
//            errMsg = getResources().getString(R.string.emsg_edit_inv_char);
//        }

        if (langStr == "GE")
            if (!str.matches(geStrReg)) {
                errMsg = "must be in German!";
            }
        if (langStr == "RU")
            if (!str.matches(ruStrReg)) {
                errMsg = "must be in Russian!";
            }


        return errMsg;
    }

}
