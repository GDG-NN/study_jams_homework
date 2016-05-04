package com.harman.ruingriv.sherlockedyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, ConditionListListener {

    public static SearchActivity INSTANCE;
    public final static String SEARCH_DATA = "SEARCH_DATA";
    private SearchListAdapter mSearchListAdapter;
    private Spinner mConditionSpinner;
    ListView mConditionListView;
    Button mAddConditionButton;
    Button mFindButton;
    EditText mConditionDetails;
    ImageView mDeleteItem;

    private int mConditionDetailsPosition = 0;
    private ArrayList<SearchItem> mSearchItems = new ArrayList<SearchItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
        setContentView(R.layout.search_activity);
        Spinner spinnerHistory = (Spinner) findViewById(R.id.history_spinner);
        ArrayAdapter<CharSequence> historyAdapter = ArrayAdapter.createFromResource(this,
                R.array.history_items_array, android.R.layout.simple_spinner_item);
        historyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHistory.setAdapter(historyAdapter);
        spinnerHistory.setSelection(0);
        spinnerHistory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mConditionListView = (ListView) findViewById(R.id.condition_list_view);

        mDeleteItem = (ImageView) findViewById(R.id.delete_item_new);
        mDeleteItem.setOnClickListener(this);
        mAddConditionButton = (Button) findViewById(R.id.add_condition_button);
        mAddConditionButton.setOnClickListener(this);
        mFindButton = (Button) findViewById(R.id.find_button);
        mFindButton.setOnClickListener(this);
        mConditionDetails = (EditText) findViewById(R.id.condition_details_new);

        mConditionSpinner = (Spinner) findViewById(R.id.condition_item_spinner_new);
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(getBaseContext(),
                R.array.condition_items_array, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mConditionSpinner.setAdapter(conditionAdapter);
        mConditionSpinner.setSelection(0);
        mConditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mConditionDetailsPosition = position;
                mConditionDetails.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                mConditionDetails.clearFocus();
            }
        });
        ArrayList<SearchItem> searchItems = new ArrayList<SearchItem>();
        mSearchListAdapter = new SearchListAdapter(getBaseContext(), searchItems, this);
        mConditionListView.setAdapter(mSearchListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_condition_button:
                String conditionItem = getResources().getStringArray(R.array.condition_items_array)[mConditionDetailsPosition];
                SearchItem item = new SearchItem(conditionItem, mConditionDetails.getText().toString(), mConditionDetailsPosition);
                mSearchListAdapter.add(item);
                mConditionSpinner.setSelection(0);
                break;
            case R.id.find_button:
                mSearchItems.clear();
                for (int i = 0; i < mSearchListAdapter.getCount(); i++) {
                    mSearchItems.add(mSearchListAdapter.getItem(i));
                }
                Intent intent = new Intent(SearchActivity.this, SearchResults.class);
                intent.putParcelableArrayListExtra(SEARCH_DATA, mSearchItems);
                startActivity(intent);
                break;
            case R.id.delete_item_new:
                mConditionDetails.setText("");
                mConditionDetails.setHint(R.string.condition);
                mConditionSpinner.setSelection(0);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_search:
                mSearchListAdapter.clear();
                mConditionSpinner.setSelection(0);
                mConditionDetails.getText().clear();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateList(SearchItem item) {
        mSearchItems.remove(item);
        mSearchListAdapter.remove(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("searchItems", mSearchItems);
    }
    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSearchItems = outState.getParcelableArrayList("searchItems");
    }
}
