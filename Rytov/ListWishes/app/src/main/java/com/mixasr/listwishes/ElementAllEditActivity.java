package com.mixasr.listwishes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mixasr.listwishes.contentprovider.MyContentProvider;
import com.mixasr.listwishes.database.ListElementsTable;

public class ElementAllEditActivity extends Activity {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LIST_ID = "listid";

    private EditText mTitleText;
    private EditText mBodyText;
    private CheckBox mCheckBox;

    private Uri listUri;

    private Long list_id;

    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_element_edit);

        mTitleText = (EditText) findViewById(R.id.element_edit_summary);
        mBodyText = (EditText) findViewById(R.id.element_edit_description);
        mCheckBox = (CheckBox) findViewById(R.id.element_edit_done);
        Button confirmButton = (Button) findViewById(R.id.element_edit_button);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();

        listUri = (bundle == null) ? null : (Uri) bundle.
                getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_ELEMENT);

        if (extras != null) {
            listUri = extras.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_ELEMENT);

            fillData(listUri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private void fillData(Uri uri) {
        String[] projection = {ListElementsTable.COLUMN_TITLE, ListElementsTable.COLUMN_DESCRIPTION,
                ListElementsTable.COLUMN_DONE };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            mTitleText.setText(cursor.getString(cursor.
                    getColumnIndexOrThrow(ListElementsTable.COLUMN_TITLE)));
            mBodyText.setText(cursor.getString(cursor.
                    getColumnIndexOrThrow(ListElementsTable.COLUMN_DESCRIPTION)));
            mCheckBox.setChecked(cursor.getInt(cursor.
                    getColumnIndexOrThrow(ListElementsTable.COLUMN_DONE)) != 0);

            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_ELEMENT, listUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();
        int done = (mCheckBox.isChecked()) ? 1 : 0;

        if (description.length() == 0 && title.length() == 0) {
            return;
        }

        if(mSettings.contains(APP_PREFERENCES_LIST_ID)) {
            list_id = mSettings.getLong(APP_PREFERENCES_LIST_ID, -1);
        }

        ContentValues values = new ContentValues();
        values.put(ListElementsTable.COLUMN_TITLE, title);
        values.put(ListElementsTable.COLUMN_LIST_ID, list_id);
        values.put(ListElementsTable.COLUMN_DESCRIPTION, description);
        values.put(ListElementsTable.COLUMN_DONE, done);

        if (listUri == null) {
            listUri = getContentResolver().insert(MyContentProvider.CONTENT_URI_ELEMENT, values);
        } else {
            getContentResolver().update(listUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(ElementAllEditActivity.this, R.string.no_title,
                Toast.LENGTH_LONG).show();
    }
}
