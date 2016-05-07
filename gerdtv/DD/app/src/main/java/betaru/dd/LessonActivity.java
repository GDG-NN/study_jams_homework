package betaru.dd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import betaru.dd.DB.DB;

public class LessonActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    Button btnCheck;
    EditText editText;
    TextView txvWordReq, txvWordCheck;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);



    }


}
