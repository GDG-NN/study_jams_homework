package com.example.lilu.chronometr;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CycleActivity extends AppCompatActivity implements View.OnClickListener {
    DbHelper timerDB;
    TextView timer;
    EditText userEdit;
    long curPeriod;
    private Timer tTimer;
    private String tableName = "timeTable";
    private long progressTime_t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //buttons
        Button redButton = (Button) findViewById(R.id.BigRedButton);
        Button greenButton =(Button) findViewById(R.id.BigGreenButton);
        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);

        //working with database
        timerDB = new DbHelper(this);
        //Timer
        timer = (TextView) findViewById(R.id.timerTV);
        //timer.setTypeface(Typeface.createFromAsset(getAssets(), "DS-DIGI.TTF"));
        timerInitialiser();
        curPeriod = 0;
        runTimer();

        //user input
        userEdit =(EditText) findViewById(R.id.userActionET);

        whatActivityNeedShow();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BigRedButton:
                isUsefulTime("0");
                progressTime_t = 0;
                userEdit.setText("Action");
                break;
            case R.id.BigGreenButton:
                isUsefulTime("1");
                progressTime_t = 0;
                userEdit.setText("Action");
                break;
        }
    }

    private void whatActivityNeedShow(){
        SQLiteDatabase db = timerDB.getWritableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"_id"},"inProgress = ?",
                new String[]{Integer.toString(1)}, null, null, null);
        if (cursor.moveToFirst()) { //the day was not started
            return;
        }
        Intent intent = new Intent(CycleActivity.this, StartActivity.class);
        startActivity(intent);
        timerDB.close();
    }

    public void isUsefulTime(String pos){
        SQLiteDatabase db = timerDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description",userEdit.getText().toString());
        values.put("duration",getReadyString(progressTime_t));
        values.put("isPos",pos);
        values.put("inProgress", 0);
        db.update(tableName,values,"inProgress = ?", new String[]{"1"});
        //create new record in db
        values.clear();
        values.put("inProgress",1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = new Date();
            String datetime = dateFormat.format(date);
            values.put("startTimer", datetime);
            values.put("date",dateFormat1.format(date));
            values.put("startTime",dateFormat2.format(date));
            System.out.println("Current Date Time : " + datetime);
        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
        long rowId = db.insert(tableName,null,values);
        Cursor cursor = db.query(tableName, new String[]{"_id","startTimer","date",
                        "startTime","description","duration",},"inProgress = ?",
                new String[]{Integer.toString(0)}, null, null, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String startTime_str = cursor.getString(1);
            Log.i("LOG_TAG", "ROW vvvv " + id + " " + cursor.getString(2)
                    + " " + cursor.getString(3) + " " + cursor.getString(4)
                    + " " + cursor.getString(5));
        }
        timerDB.close();
    }

    private void timerInitialiser()
    {
        //ContentValues cv = new ContentValues();
        SQLiteDatabase db = timerDB.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"_id","startTimer"},"inProgress = ?",
                                new String[]{Integer.toString(1)}, null, null, null);
        if (cursor.moveToFirst()) {
            String startTime_str = cursor.getString(1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try{
                Date startDate = format.parse(startTime_str);
                Date curDate = new Date();
                progressTime_t = curDate.getTime() - startDate.getTime();//im ms
                Log.i("LOG_TAG", "Dif = " + progressTime_t);
                timer.setText(getReadyString(progressTime_t));

            } catch (ParseException e) {
                timer.setText("00:00:00");
                progressTime_t = 0;
                e.printStackTrace();
            }
        }
        timerDB.close();

    }

    private void runTimer(){
        tTimer = new Timer();
        tTimer.schedule(new TimerTask(){
            @Override
            public void run(){
               TimerMethod();
           };
        },0,1000);
    }
    private void TimerMethod(){
        this.runOnUiThread(timerTick);
    }
    private Runnable timerTick = new Runnable(){
        @Override
        public void run() {
            progressTime_t = progressTime_t + 1000;
            /*String readyTimer = (new SimpleDateFormat("HH:mm:ss")).format(new Date(
                    (curPeriod * 1000)+ progressTime_t));*/

            timer.setText(getReadyString(progressTime_t));
        }
    };
    public String getReadyString(long sometime){
        int[] time_arr = new int[3];//0 - hours, 1- minutes, 2 - seconds
        time_arr[0] = (int)sometime/3600000; //1 hour = 1 min * 60 = 60 * 60 * 1000 ms = 3 600 000 ms
        time_arr[1] = (int)((sometime%3600000)/60000);//1 min = 60 sec = 60*1000 ms = 60 000 ms
        time_arr[2] = (int)(((sometime%3600000)%60000)/1000);//1 sec = 1000 ms
        String readyTimer;
        readyTimer = "";
        //create string hh:mm:ss
        for(int i=0,j=0;i<3;i++,j++){
            if (time_arr[i] < 10)
                readyTimer = readyTimer + "0";
            readyTimer = readyTimer + time_arr[i];
            if (j<2)
                readyTimer = readyTimer + ":";
        }
        return readyTimer;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cycle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        int id = item.getItemId();
        switch(id) {
            case  R.id.cycle1:
                intent = new Intent(CycleActivity.this, ListActivity.class);
                startActivity(intent);
                return true;
            case R.id.cycle2:
                resetTimer();
                return true;
            case R.id.cycle3:
                finishDay();//finish activity is best of the best
                intent = new Intent(CycleActivity.this, StartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    void resetTimer(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");

        SQLiteDatabase db = timerDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Date date = new Date();
        cv.put("startTimer", dateFormat.format(date));
        cv.put("date",dateFormat1.format(date));
        cv.put("startTime",dateFormat2.format(date));
        db.update(tableName,cv,"inProgress = ?", new String[]{"1"});
        timerDB.close();

        progressTime_t=0;
    }
    public void finishDay () {
        SQLiteDatabase db = timerDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", userEdit.getText().toString());
        values.put("duration", getReadyString(progressTime_t));
        values.put("isPos", 1);
        values.put("inProgress", 0);
        db.update(tableName, values, "inProgress = ?", new String[]{"1"});
        timerDB.close();
    }
}
