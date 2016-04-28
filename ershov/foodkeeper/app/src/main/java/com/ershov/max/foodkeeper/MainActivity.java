package com.ershov.max.foodkeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DB db;
    SimpleCursorAdapter scAdapter;
    ListView productListView;
    Cursor cursor;
    AlertDialog.Builder confirmDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddFoodItemActivity.class);
                startActivity(intent);
            }
        });

        db = new DB(this);
        db.open();
        cursor = db.getAllItems();

        String[] dbLine = new String[] { DB.COLUMN_NAME, DB.COLUMN_EXPIRE_DATE };
        int[] viewLine = new int[] { R.id.itemNameText, R.id.itemDateText };

        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, dbLine, viewLine, 0);
        scAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == 3) {
                    TextView tv = (TextView) view;
                    String days = FoodItem.fromDateToDuration(cursor.getString(columnIndex));
                    tv.setText(days);
                    return true;
                }
                return false;
            }
        });

        productListView = (ListView) findViewById(R.id.productListView);
        if (productListView != null) {
            productListView.setAdapter(scAdapter);
        }

        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                delItemWithAlert(MainActivity.this, id);
                scAdapter.notifyDataSetChanged();
                return true;
            }
        });
        db.close();
    }

    private void delItemWithAlert(final Context context, final long id) {
        confirmDel = new AlertDialog.Builder(context);
        confirmDel.setTitle(R.string.del_alert_title);  // заголовок
        confirmDel.setMessage(R.string.del_alert_message); // сообщение
        confirmDel.setPositiveButton(R.string.del_alert_btn_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                db = new DB(context);
                db.open();
                db.delRec(id);
                db.close();
                finish();
                startActivity(getIntent());
            }
        });
        confirmDel.setNegativeButton(R.string.del_alert_btn_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        confirmDel.setCancelable(true);
        confirmDel.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });

        confirmDel.show();
    }

}
