package com.levrite.washcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CityActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnNext;
    Spinner spnrCity;
    static final int[] idCity = {27612, 26063, 27459};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        btnNext = (Button) findViewById(R.id.btnNext);
        spnrCity = (Spinner) findViewById(R.id.spnrCity);
        btnNext.setOnClickListener(this);


        ArrayAdapter<?> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrCity.setAdapter(spinnerAdapter);

        checkCity();

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(CityActivity.this, MainActivity.class);
        int tempId = (int) spnrCity.getSelectedItemId();
        intent.putExtra("nameCity", spnrCity.getSelectedItem().toString());
        intent.putExtra("idCity", idCity[tempId]);
        startActivity(intent);

    }

    //Метод для одноразового показа CityActivity
    public void checkCity() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }
    }


}
