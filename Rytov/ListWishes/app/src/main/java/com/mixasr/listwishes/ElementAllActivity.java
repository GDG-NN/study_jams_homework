package com.mixasr.listwishes;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.mixasr.listwishes.contentprovider.MyContentProvider;
import com.mixasr.listwishes.database.ListElementsTable;

public class ElementAllActivity extends ListActivity implements LoaderManager.
        LoaderCallbacks<Cursor> {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LIST_ID = "listid";

    private Long list_id;

    private static final int DELETE_ID = Menu.FIRST + 1;

    SharedPreferences mSettings;

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements_list);
        this.getListView().setDividerHeight(2);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        fillData();
        registerForContextMenu(getListView());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createList();
                return true;
            case R.id.help:
                makeToast();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_ELEMENT + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createList() {
        Intent i = new Intent(this, ElementAllEditActivity.class);
        startActivity(i);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ElementAllEditActivity.class);
        Uri listUri = Uri.parse(MyContentProvider.CONTENT_URI_ELEMENT + "/" + id);
        i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE_ELEMENT, listUri);
        startActivity(i);
    }

    private void fillData() {
        String[] from = new String[] { ListElementsTable.COLUMN_TITLE, ListElementsTable.COLUMN_DONE };

        int[] to = new int[] { R.id.element_label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.activity_element_row, null, from, to, 0)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                Cursor c = getCursor();
                c.moveToPosition(position);
                int col = c.getColumnIndex(ListElementsTable.COLUMN_DONE);
                int is_checked = c.getInt(col);

                if (is_checked == 1) {
                    ImageView iv = (ImageView) v.findViewById(R.id.element_ImageView);
                    iv.setImageResource(R.drawable.checked);
                } else {
                    ImageView iv = (ImageView) v.findViewById(R.id.element_ImageView);
                    iv.setImageResource(R.drawable.notchecked);
                }
                return v;
            }
        };
        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_element_delete);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { ListElementsTable.COLUMN_ID, ListElementsTable.COLUMN_TITLE, ListElementsTable.COLUMN_DONE };

        if(mSettings.contains(APP_PREFERENCES_LIST_ID)) {
            list_id = mSettings.getLong(APP_PREFERENCES_LIST_ID, -1);
        }

        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI_ELEMENT, projection, "list_id=?", new String[] {""+list_id}, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() ==  MotionEvent.ACTION_DOWN) hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void makeToast() {
        Toast.makeText(ElementAllActivity.this, R.string.help_text_element,
                Toast.LENGTH_LONG).show();
    }
}
