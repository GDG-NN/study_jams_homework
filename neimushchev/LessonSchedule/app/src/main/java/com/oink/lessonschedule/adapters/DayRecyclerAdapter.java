package com.oink.lessonschedule.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oink.lessonschedule.activities.MainActivity;
import com.oink.lessonschedule.R;
import com.oink.lessonschedule.activities.SelectedDayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder> {

    private JSONArray jsonArray;
    private MainActivity mainActivity;

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayName;

        public ViewHolder(View v) {
            super(v);
            dayName = (TextView) v.findViewById(R.id.day_name);
        }
    }

    public DayRecyclerAdapter(MainActivity mainActivity, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.mainActivity = mainActivity;
    }

    @Override
    public DayRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_item, parent, false);

        //Attributes

        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, SelectedDayActivity.class);
                try {
                    intent.putExtra("jsonDay", jsonArray.getJSONObject(vh.getAdapterPosition()).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mainActivity.startActivity(intent);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject lessonObject = jsonArray.getJSONObject(position);
            holder.dayName.setText(lessonObject.getString(mainActivity.getString(R.string.json_day_name)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
}
