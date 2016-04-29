package com.project.soul.todo.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.soul.todo.R;
import com.project.soul.todo.adapter.DoneTaskAdapter;
import com.project.soul.todo.database.DBHelper;
import com.project.soul.todo.model.ModelTask;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends TaskFragment {




    public DoneTaskFragment() {
        // Required empty public constructor
    }


    OnTaskRestoreListener onTaskRestoreListener;
    public interface OnTaskRestoreListener
    {
        void onTaskRestore(ModelTask task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            onTaskRestoreListener = (OnTaskRestoreListener)activity;

        }
        catch (ClassCastException e)
        {
            throw  new ClassCastException(activity.toString()+ " must implement OnTaskRestoreListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvDoneTask);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DoneTaskAdapter(this);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void findTasks(String title) {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE + " AND " +
                        DBHelper.SELECTION_STATUS, new String[]{"%" + title + "%", Integer.toString(ModelTask.STATUS_DONE)},
                DBHelper.TASK_DATE_COLUMN));

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }

    }

    @Override
    public void addTaskFromDB() {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_DONE)},
                DBHelper.TASK_DATE_COLUMN));

        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    @Override
    public void addTask(ModelTask newTask, boolean saveToDB) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            adapter.addItem(position, newTask);

        } else {
            adapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.dbHelper.saveTask(newTask);
        }
    }
    @Override
    public void moveTask(ModelTask task) {
        if(task.getDate()!=0)
        {
            alarmHelper.setAlarm(task);
        }
        onTaskRestoreListener.onTaskRestore(task);

    }
}
