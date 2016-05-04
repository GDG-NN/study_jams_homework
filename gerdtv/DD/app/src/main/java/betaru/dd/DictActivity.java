package betaru.dd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import betaru.dd.DB.DB;

public class DictActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    Button btnAdd, btnRead, btnClear;
    EditText etWord, etTrans;
    TextView txvGerResult, txvRusResult, txvArticle;

    DB db;


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

        db = new DB(this);
    }


    @Override
    public void onClick(View v) {

        long rowID = 0;
        String chStatus, str = null;


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



        switch (v.getId()) {
            case R.id.btnAdd:
                // вставляем запись и получаем ее ID
                rowID = db.addWord(word, trans);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
        }

        if ((rowID != 0) || (rowID != -1)) {

            String art;

            // GER
            str = word;

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

            // GER
            txvGerResult.setText(str);
            txvGerResult.setEnabled(true);

            // RUS
            txvRusResult.setText(trans);
            txvRusResult.setEnabled(true);

            Toast.makeText(this, getResources().getString(R.string.fmsg_word_added), Toast.LENGTH_SHORT).show();

            //Clear Edit + set focus
            etWord.setText("");
            etWord.requestFocus();

            etTrans.setText("");
        } else
            Log.d(LOG_TAG, "Cursor is null");


        // Close db conn
        db.close();
    }


    private String validateText(String str, String langStr) {
        String errMsg = null;
        // GE Regexp
        String geStrReg = "^[a-zA-ZäöüÄÖÜß ]+$";

        // RU Regexp
        String ruStrReg = "^[А-яЁё ][-А-яЁё ]+$";


        if (str.length() == 0)
             errMsg = getResources().getString(R.string.emsg_edit_required);

        if (langStr == "GE")
            if (!str.matches(geStrReg)) {
                errMsg = getResources().getString(R.string.emsg_only_ger);
            }
        if (langStr == "RU")
            if (!str.matches(ruStrReg)) {
                errMsg = getResources().getString(R.string.emsg_only_rus);
            }

        return errMsg;
    }

}
