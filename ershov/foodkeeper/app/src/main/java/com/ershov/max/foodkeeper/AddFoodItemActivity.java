package com.ershov.max.foodkeeper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddFoodItemActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText inputDateText;
    private EditText daysBeforeText;
    private boolean flagForChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		nameText = (EditText) findViewById(R.id.nameText);

        inputDateText = (EditText) findViewById(R.id.inputDateText);
        inputDateText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddFoodItemActivity.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
                return false;
            }
        });
        inputDateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("") && FoodItem.isDateValid(s.toString())) {
                    if (flagForChange) {
                        flagForChange = false;
                        daysBeforeText.setText(FoodItem.fromDateToDuration(s.toString()));
                    } else flagForChange = true;
                }
            }
        });

        daysBeforeText = (EditText) findViewById(R.id.daysBeforeText);
        daysBeforeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (flagForChange) {
                        flagForChange = false;
                        inputDateText.setText(FoodItem.fromDurationToDate(s.toString()));
                    } else flagForChange = true;
                }
            }
        });
    }

    public void onClickFinishAdding(View view) {
        saveItem();
        Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickContinueAdding(View view) {
        saveItem();
        finish();
        startActivity(getIntent());
    }

    private void saveItem()  {
        try {
            if (FoodItem.isDateValid(inputDateText.getText().toString())) {
                FoodItem item = new FoodItem(inputDateText.getText().toString(), nameText.getText().toString());
                DB db = new DB(this);
                db.open();
                db.addProduct(item);
                Toast toast = Toast.makeText(this, R.string.toaster_msg_success, Toast.LENGTH_SHORT);
                toast.show();
                db.close();
            } else {
                Toast toast = Toast.makeText(this, R.string.toaster_msg_wrong_date, Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, R.string.toaster_msg_fail, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String day = (dayOfMonth < 10) ? "0"+ String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
            monthOfYear++;
            String month = (monthOfYear < 10) ? "0"+ String.valueOf(monthOfYear) : String.valueOf(monthOfYear);
            inputDateText.setText(new StringBuilder()
                    // Месяца начинаются с 0
                    .append(day).append("-").append(month).append("-")
                    .append(year).append(" "));
        }
    }


}
