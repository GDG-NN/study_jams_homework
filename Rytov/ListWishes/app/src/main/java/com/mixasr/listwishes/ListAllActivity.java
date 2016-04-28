package com.mixasr.listwishes;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.mixasr.listwishes.contentprovider.MyContentProvider;
import com.mixasr.listwishes.database.ListTable;

public class ListAllActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LIST_ID = "listid";

    SharedPreferences mSettings;

    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
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
            case EDIT_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                Intent i = new Intent(this, ListAllEditActivity.class);
                Uri listUri = Uri.parse(MyContentProvider.CONTENT_URI_LIST + "/" + info.id);
                i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE_LIST, listUri);
                startActivity(i);
                return true;
            case DELETE_ID:
                info = (AdapterContextMenuInfo) item.getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI_LIST + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createList() {
        Intent i = new Intent(this, ListAllEditActivity.class);
        startActivity(i);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ElementAllActivity.class);
        Uri listUri = Uri.parse(MyContentProvider.CONTENT_URI_ELEMENT + "/" + id);
        i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE_ELEMENT, listUri);

        Editor editor = mSettings.edit();
        editor.putLong(APP_PREFERENCES_LIST_ID, id);
        editor.apply();

        startActivity(i);
    }

    private void fillData() {
        String[] from = new String[] { ListTable.COLUMN_TITLE };

        int[] to = new int[] { R.id.list_label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.activity_list_row, null, from, to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, EDIT_ID, 0, R.string.menu_list_edit);
        menu.add(0, DELETE_ID, 0, R.string.menu_list_delete);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { ListTable.COLUMN_ID, ListTable.COLUMN_TITLE };
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI_LIST, projection, null, null, null);
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

    private void makeToast() {
        Toast.makeText(ListAllActivity.this, R.string.help_text_list,
                Toast.LENGTH_LONG).show();
    }
}
