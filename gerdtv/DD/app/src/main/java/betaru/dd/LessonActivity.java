package betaru.dd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import betaru.dd.DB.DB;

public class LessonActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    String[] data;
    Integer chId;
    String chWord;
    String chTrans;

    TextView txvWordReq;
    EditText etCheckRes;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        db = new DB(this);

        txvWordReq = (TextView) findViewById(R.id.txv_word_req);
        etCheckRes = (EditText) findViewById(R.id.etWordChk);

        prepareLesson();
    }

    public void prepareLesson() {


        String[] data = db.getCheckWord();

        chId = Integer.valueOf(data[0]);
        chWord = data[1];
        chTrans = data[2];

        txvWordReq.setText(chWord);
    }

    @Override
    public void onClick(View v) {


        // получаем данные из полей ввода
        String word = etCheckRes.getText().toString();

        if (word.equals(chTrans)) {
            db.upCounterWord(chId);
            Toast.makeText(this, getResources().getString(R.string.tmsg_check_true), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.emsg_check_wrong), Toast.LENGTH_LONG).show();
        }

        etCheckRes.setText("");
        prepareLesson();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf(LOG_TAG, "onDestroy");
        db.close();
    }
}
