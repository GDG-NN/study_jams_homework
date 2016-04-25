package com.oink.lessonschedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class ScheduleFragment extends android.support.v4.app.Fragment {

    private JSONArray jsonLessonsArray;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        try {
            jsonLessonsArray = new JSONArray(arguments.getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.lesson_recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.lesson_recycler_view);
        setupRecyclerView(recyclerView, layout);
        return layout;
    }

    private void setupRecyclerView(RecyclerView recyclerView, RelativeLayout layout) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        LessonRecyclerAdapter mAdapter = new LessonRecyclerAdapter(jsonLessonsArray, getResources());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        TextView emptyView = (TextView) layout.findViewById(R.id.empty_view);
        if (jsonLessonsArray.length() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
