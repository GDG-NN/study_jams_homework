package com.andrejjj.pocketsecretary.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andrejjj.pocketsecretary.R;

import java.util.Calendar;

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 *          This is an Activity for adding event
 */
public class AddEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String[] items = {"Meeting", "Call", "Work", "Home", "Other"};
    Calendar dateAndTime = Calendar.getInstance();
    private EditText mEditTextDate;
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
    private EditText mEditTextDescription;
    private TextView mTextViewSelection;
    private TextView mTextViewDate;
    private TextView mTextViewTime;
    private Button mButtonSave;
    private Button mButtonClear;
    private Button mButtonAddAudio;
    private Spinner mSpinnerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        mEditTextDate = (EditText) findViewById(R.id.txtDate);
        mEditTextDescription = (EditText) findViewById(R.id.txtDescription);
        mTextViewSelection = (TextView) findViewById(R.id.lblSelection);
        mTextViewDate = (TextView) findViewById(R.id.lblDate);
        mTextViewTime = (TextView) findViewById(R.id.lblTime);
        mButtonClear = (Button) findViewById(R.id.btnClear);
        mButtonSave = (Button) findViewById(R.id.btnSave);
        mButtonAddAudio = (Button) findViewById(R.id.btnAddAudio);
        mSpinnerType = (Spinner) findViewById(R.id.lstType);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveAllData();
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mEditTextDate.setText("");
                mEditTextDescription.setText("");
                mTextViewSelection.setText("");
                mTextViewDate.setText("");
                mTextViewTime.setText("");
            }
        });

        mButtonAddAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //TODO adding audio
            }
        });

        if (mSpinnerType != null) {
            mSpinnerType.setOnItemSelectedListener(this);
        }

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerType.setAdapter(aa);


        updateLabel();
    }

    private void saveAllData() {
        //TODO saving all data
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
            case R.id.mniContacts:
                Intent intentContacts = new Intent(this, com.andrejjj.pocketsecretary.activities.ContactsList.class);
                startActivity(intentContacts);
                return (true);
            case R.id.mniAbout:
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG)
                        .show();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
        mTextViewSelection.setText(items[position]);
    }

    @Override
    public void onNothingSelected(final AdapterView<?> parent) {
        mTextViewSelection.setText("");
    }
}
