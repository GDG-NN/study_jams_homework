package com.andrejjj.pocketsecretary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andrejjj.pocketsecretary.R;

/**
 * @author Andrey S. Pugachenko
 * @version 0.0.1
 *          This is an Activity for adding contacts
 */
public class AddContact extends AppCompatActivity {

    private Button mButtonSave;
    private Button mButtonClear;
    private EditText mEditTextName;
    private EditText mEditTextAka;
    private EditText mEditTextSurname;
    private EditText mEditTextEmail;
    private EditText mEditTextSkype;
    private EditText mEditTextPhone;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        mButtonClear = (Button) findViewById(R.id.btnClear);
        mButtonSave = (Button) findViewById(R.id.btnSave);
        mEditTextName = (EditText) findViewById(R.id.txtName);
        mEditTextAka = (EditText) findViewById(R.id.txtAka);
        mEditTextSurname = (EditText) findViewById(R.id.txtSurname);
        mEditTextEmail = (EditText) findViewById(R.id.txtEmail);
        mEditTextSkype = (EditText) findViewById(R.id.txtSkype);
        mEditTextPhone = (EditText) findViewById(R.id.txtPhone);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                saveContact();
            }
        });

        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                clearAllFields();
            }
        });
    }

    private void clearAllFields() {
        mEditTextName.setText("");
        mEditTextAka.setText("");
        mEditTextSurname.setText("");
        mEditTextEmail.setText("");
        mEditTextSkype.setText("");
        mEditTextPhone.setText("");
    }

    private void saveContact() {
        //TODO adding contact;
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
}
