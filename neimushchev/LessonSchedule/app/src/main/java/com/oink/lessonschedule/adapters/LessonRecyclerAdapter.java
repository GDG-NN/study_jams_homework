package com.oink.lessonschedule.adapters;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oink.lessonschedule.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LessonRecyclerAdapter extends RecyclerView.Adapter<LessonRecyclerAdapter.ViewHolder> {

    private JSONArray jsonArray;

    private String[] firstBuildingTime;
    private String[] sixthBuildingTime;
    private Resources resources;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lessonClassroom;
        public TextView lessonName;
        public TextView lessonTime;
        public TextView lessonAdditionalInfo;

        public ViewHolder(View v) {
            super(v);
            lessonName = (TextView) v.findViewById(R.id.lesson_name);
            lessonTime = (TextView) v.findViewById(R.id.lesson_time);
            lessonAdditionalInfo = (TextView) v.findViewById(R.id.lesson_additional_info);
            lessonClassroom = (TextView) v.findViewById(R.id.lesson_classroom);
        }
    }

    public LessonRecyclerAdapter(JSONArray jsonArray, Resources resources) {
        this.jsonArray = jsonArray;
        this.resources = resources;
        firstBuildingTime = resources.getStringArray(R.array.first_building_time);
        sixthBuildingTime = resources.getStringArray(R.array.sixth_building_time);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);

        v.setClickable(true);
        //Getting style attribute
        int[] attrs = {android.R.attr.selectableItemBackground};
        TypedArray ta = v.getContext().obtainStyledAttributes(R.style.AppTheme, attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            v.setForeground(ta.getDrawable(0));
        }
        else {
            v.setClickable(true);
            v.setBackground(ta.getDrawable(0));
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject lessonObject = jsonArray.getJSONObject(position);
            holder.lessonAdditionalInfo.setText(lessonObject.getString(resources.getString(R.string.json_lesson_additional_info)));
            holder.lessonClassroom.setText(lessonObject.getString(resources.getString(R.string.json_lesson_classroom)));
            holder.lessonName.setText(lessonObject.getString(resources.getString(R.string.json_lesson_name)));

            if (lessonObject.getString(resources.getString(R.string.json_lesson_additional_info)).equals("")) {
                holder.lessonAdditionalInfo.setVisibility(View.GONE);
            }
            if (lessonObject.getString(resources.getString(R.string.json_lesson_name)).equals("")) {
                holder.lessonName.setVisibility(View.GONE);
            }
            if (lessonObject.getString(resources.getString(R.string.json_lesson_classroom)).equals("")) {
                holder.lessonClassroom.setVisibility(View.GONE);
            }

            int lessonNumber = lessonObject.getInt(resources.getString(R.string.json_lesson_number));
            boolean inSixthBuilding = lessonObject.getBoolean(resources.getString(R.string.json_lesson_in_sixth_building));
            if (inSixthBuilding) {
                holder.lessonTime.setText(sixthBuildingTime[lessonNumber - 1]);
            }
            else {
                holder.lessonTime.setText(firstBuildingTime[lessonNumber - 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
}
