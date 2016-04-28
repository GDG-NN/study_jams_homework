package com.mixasr.listwishes;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mixasr.listwishes.contentprovider.MyContentProvider;
import com.mixasr.listwishes.database.ListTable;

public class ListAllEditActivity extends Activity {
    private EditText mTitleText;
    private EditText mBodyText;

    private Uri listUri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_list_edit);

        mTitleText = (EditText) findViewById(R.id.list_edit_summary);
        mBodyText = (EditText) findViewById(R.id.list_edit_description);
        Button confirmButton = (Button) findViewById(R.id.list_edit_button);

        Bundle extras = getIntent().getExtras();

        listUri = (bundle == null) ? null : (Uri) bundle.
                getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_LIST);

        if (extras != null) {
            listUri = extras.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE_LIST);

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
        String[] projection = { ListTable.COLUMN_TITLE, ListTable.COLUMN_DESCRIPTION };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            mTitleText.setText(cursor.getString(cursor.
                    getColumnIndexOrThrow(ListTable.COLUMN_TITLE)));
            mBodyText.setText(cursor.getString(cursor.
                    getColumnIndexOrThrow(ListTable.COLUMN_DESCRIPTION)));

            cursor.close();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE_LIST, listUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String description = mBodyText.getText().toString();

        if (description.length() == 0 && title.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ListTable.COLUMN_TITLE, title);
        values.put(ListTable.COLUMN_DESCRIPTION, description);

        if (listUri == null) {
            listUri = getContentResolver().insert(MyContentProvider.CONTENT_URI_LIST, values);
        } else {
            getContentResolver().update(listUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(ListAllEditActivity.this, R.string.no_title,
                Toast.LENGTH_LONG).show();
    }
}
