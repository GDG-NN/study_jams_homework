package betaru.dd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import betaru.dd.DB.DB;

public class MainActivity extends AppCompatActivity {


    private static final String LOG_TAG = "my_tag";
//    TextView txvWordAddCount;
//    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        db = new DB(this);
//        int wordAdd = db.getCountAddedWords();

//        Log.d(LOG_TAG, "words added = " + wordAdd);
//        txvWordAddCount = (TextView) findViewById(R.id.txv_word_add_counter);
//        txvWordAddCount.setText(String.valueOf(wordAdd));



//        db.close();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Don't repeat at home and production code :)
        Log.wtf(LOG_TAG, "onStart");

        updateStats();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.wtf(LOG_TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf(LOG_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf(LOG_TAG, "onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.wtf(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf(LOG_TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.wtf(LOG_TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf(LOG_TAG, "onDestroy");
    }

    public void updateStats() {
        TextView txvWordAddCount;
        DB db;

        db = new DB(this);
        int wordAdd = db.getCountAddedWords();

//        Log.d(LOG_TAG, "words added = " + wordAdd);
        txvWordAddCount = (TextView) findViewById(R.id.txv_word_add_counter);
        txvWordAddCount.setText(String.valueOf(wordAdd));

        db.close();
    }

    public void openDict(View v) {
        Intent intent = new Intent(this, DictActivity.class);
        startActivity(intent);
    }

    public void openLes(View v) {
        Intent intent = new Intent(this, LessonActivity.class);
        startActivity(intent);
    }
}