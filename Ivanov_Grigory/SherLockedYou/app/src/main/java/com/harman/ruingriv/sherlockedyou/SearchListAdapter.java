package com.harman.ruingriv.sherlockedyou;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class SearchListAdapter extends ArrayAdapter<SearchItem> {

    private ConditionListListener mListener;
    private Context mContext;
    private LayoutInflater mInflater;


    public SearchListAdapter(Context context, ArrayList<SearchItem> items, ConditionListListener listener) {
        super(context, 0, items);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(SearchActivity.LAYOUT_INFLATER_SERVICE);
        this.mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final SearchItem item = getItem(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.condition_listview_item, parent, false);
            holder = new ViewHolder();
            holder.conditionItemSpinner = (Spinner) convertView.findViewById(R.id.condition_item_spinner);
            holder.conditionDetails = (EditText) convertView.findViewById(R.id.condition_details);
            holder.deleteRow = (ImageView) convertView.findViewById(R.id.delete_item);
            holder.deleteRow.setTag(position);
            holder.conditionDetails.setInputType(InputType.TYPE_CLASS_TEXT);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.condition_items_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.conditionItemSpinner.setAdapter(spinnerAdapter);
        holder.conditionItemSpinner.setSelection(item.getPosition());
        holder.conditionItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item.setSpinnerItem(mContext.getResources().getStringArray(R.array.condition_items_array)[position]);
                item.setPosition(position);
                holder.conditionDetails.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        holder.conditionDetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    final EditText caption = (EditText) v;
                    item.setConditionsList(caption.getText().toString());
                }
            }
        });
        holder.deleteRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.updateList(item);
            }
        });
        holder.conditionDetails.setText(item.getConditionsList());
        return convertView;
    }

    public static class ViewHolder {
        public Spinner conditionItemSpinner;
        public EditText conditionDetails;
        public ImageView deleteRow;

    }
}
