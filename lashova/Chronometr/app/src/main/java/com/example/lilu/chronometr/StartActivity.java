package com.example.lilu.chronometr;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    DbHelper timerDB;
    private String tableName = "timeTable";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(this);


        timerDB = new DbHelper(this);
        whatActivityNeedShow();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case  R.id.action_list:
                Intent intent = new Intent(StartActivity.this, ListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStart:
                dayIsStarted();
                break;
        }
    }

    private void whatActivityNeedShow(){
        SQLiteDatabase db = timerDB.getWritableDatabase();
        Cursor cursor = db.query(tableName, new String[]{"_id"},"inProgress = ?",
                new String[]{Integer.toString(1)}, null, null, null);
        if (cursor.moveToFirst()) { //the day was already started
            Intent intent = new Intent(StartActivity.this, CycleActivity.class);
            startActivity(intent);
        }
        timerDB.close();
    }

    private void dayIsStarted() {
        //insert note about new activity in db:
        addStartNoteInDB();
        //start usual activity
        Intent intent = new Intent(StartActivity.this, CycleActivity.class);
        startActivity(intent);
    }

    private void addStartNoteInDB() {
        //Create content object
        ContentValues cv = new ContentValues();
        //Connect to database
        SQLiteDatabase db = timerDB.getWritableDatabase();
        //current data and time will be created automatically when the row is added to database
        cv.put("inProgress",1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = new Date();
            cv.put("startTimer", dateFormat.format(date));
            cv.put("date",dateFormat1.format(date));
            cv.put("startTime",dateFormat2.format(date));
            System.out.println("Current Date Time : " + dateFormat.format(date) +
            "-" + dateFormat1.format(date) + "-"+dateFormat2.format(date) );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long rowId = db.insert(tableName,null,cv);
        timerDB.close();
    }
}
