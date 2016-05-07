package betaru.dd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import betaru.dd.DB.DB;

public class LessonActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        prepareLesson();

    }



    public void prepareLesson() {

        DB db;
        db = new DB(this);

//        String[] data = db.getCheckWord();


    }
}