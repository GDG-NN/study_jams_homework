package com.project.soul.todo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.soul.todo.database.DBHelper;
import com.project.soul.todo.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soul on 26.04.2016.
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);

        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper = AlarmHelper.getInstance();

        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        for (ModelTask task : tasks)
        {
            if(task.getDate() !=0)
            {
                alarmHelper.setAlarm(task);
            }
        }
    }
}
