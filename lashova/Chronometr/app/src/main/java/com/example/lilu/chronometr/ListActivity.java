package com.example.lilu.chronometr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ListView listView;
    private String tableName = "timeTable";
    SimpleCursorAdapter scAdapter;
    DbHelper timerDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timerDB = new DbHelper(this);
        listView =  (ListView)findViewById(R.id.listView);
        fillList();
    }
    private void fillList()
    {
        Cursor cursor;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        List<String> result_array = new ArrayList();
        String tmpStr;
        SQLiteDatabase db = timerDB.getReadableDatabase();
        Log.i("LOG_TAG", "MAIN LISt");
        String[] dbLine = new String[] { "startTime","description","duration"};
        int[] viewLine = new int[] { R.id.itemStartTime, R.id.itemDesc, R.id.itemDur};
        cursor = db.query(tableName, new String[]{"isPos","_id","startTime","description","duration"},
                "date = ? and inProgress = ?",new String[]{dateformat.format(date),"0"}, null, null, null);

        scAdapter = new SimpleCursorAdapter(this, R.layout.itemdate, cursor, dbLine, viewLine, 0);

        scAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                TextView tv = (TextView) view;
                if (cursor.getInt(0) == 1) {
                    tv.setBackgroundColor(0xffd0ee75);
                    return false;
                }
                else{
                    tv.setBackgroundColor(0xfffb8484);
                    return false;
                }
            }
        }

        );
        listView.setAdapter(scAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case  R.id.main:
                Intent intent = new Intent(ListActivity.this, CycleActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
