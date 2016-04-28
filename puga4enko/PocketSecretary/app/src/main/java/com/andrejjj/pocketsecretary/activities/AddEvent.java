package com.andrejjj.pocketsecretary.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andrejjj.pocketsecretary.R;

import java.util.Calendar;

/**
 * Created by Andrejjj on 25.04.2016.
 */

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 *          This is an Activity for adding event
 */
public class AddEvent extends AppCompatActivity {

    private EditText mEditTextDate;
    private EditText mEditTextTime;
    private EditText mEditTextDescription;
    private TextView mTextViewDate;
    private TextView mTextViewTime;


    Calendar dateAndTime = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        mEditTextDate = (EditText) findViewById(R.id.txtDate);
        mEditTextTime = (EditText) findViewById(R.id.txtTime);
        mEditTextDescription = (EditText) findViewById(R.id.txtDescription);
        mTextViewDate = (TextView) findViewById(R.id.lblDate);
        mTextViewTime = (TextView) findViewById(R.id.lblTime);

        updateLabel();
    }

    public void showDatePickerDialog(View v) {
        new DatePickerDialog( this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void showTimePickerDialog(View v) {
        new TimePickerDialog( this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE),
                true)
                .show();
    }

    private void updateLabel() {
        mEditTextDate
                .setText(DateUtils
                        .formatDateTime( this,
                                dateAndTime.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_TIME));
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateLabel();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniAnalytics:
                Intent intentAnalytics = new Intent(this, com.andrejjj.pocketsecretary.activities.Analytics.class);
                startActivity(intentAnalytics);
                return (true);
//            case R.id.mniOptions:
//                Intent intentOptions = new Intent(this, com.andrejjj.pocketsecretary.activities.Options.class);
//                startActivity(intentOptions);
//                return (true);
            case R.id.mniAbout:
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG)
                        .show();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
}
