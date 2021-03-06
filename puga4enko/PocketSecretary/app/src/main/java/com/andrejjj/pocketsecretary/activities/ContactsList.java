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
 *          This is an Activity for options
 */
public class ContactsList extends AppCompatActivity {
    EditText mEditTextContacts;
    Button mButtonAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

        mEditTextContacts = (EditText) findViewById(R.id.txtContacts);
        mButtonAddContact = (Button) findViewById(R.id.btnAddContact);

        mButtonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intentAddContact = new Intent(getApplicationContext(), com.andrejjj.pocketsecretary.activities.AddContact.class);
                startActivity(intentAddContact);
            }
        });
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
