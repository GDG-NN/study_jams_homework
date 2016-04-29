package com.project.soul.todo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.project.soul.todo.fragment.TaskFragment;
import com.project.soul.todo.model.Item;
import com.project.soul.todo.model.ModelSeparator;
import com.project.soul.todo.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Soul on 30.03.2016.
 */
public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Item> items;
    TaskFragment taskFragment;

    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    public boolean containsSeparatorFuture;

    public TaskAdapter(TaskFragment taskFragment) {
        this.taskFragment = taskFragment;
        items = new ArrayList<>();
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void updateTask(ModelTask newTask)
    {
        for(int i = 0; i < getItemCount();i++)
        {
            if(getItem(i).isTask()){
                ModelTask task = (ModelTask) getItem(i);
                if (newTask.getTimeStamp() == task.getTimeStamp()){
                    removeItem(i);
                    getTaskFragment().addTask(newTask, false);
                }
            }
        }
    }

    public void addItem(int location, Item item) {
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void removeItem(int location) {
        if (location >= 0 && location <= getItemCount() - 1) {
            items.remove(location);
            notifyItemRemoved(location);
            if (location - 1 >= 0 && location <= getItemCount() - 1) {
                if (!getItem(location).isTask() && !getItem(location - 1).isTask()) {
                    ModelSeparator separator = (ModelSeparator) getItem(location - 1);
                    checkSeparators(separator.getType());
                    items.remove(location - 1);
                    notifyItemRemoved(location - 1);
                }
            } else if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isTask()) {

                ModelSeparator separator = (ModelSeparator) getItem(getItemCount() -1);
                checkSeparators(separator.getType());
                int locationTemp = getItemCount() - 1;
                items.remove(locationTemp);
                notifyItemRemoved(locationTemp);
            }
        }
    }

    public void checkSeparators(int type) {
        switch (type) {
            case ModelSeparator.TYPE_OVERDUE:
                containsSeparatorOverdue = false;
                break;
            case ModelSeparator.TYPE_TODAY:
                containsSeparatorToday = false;
                break;
            case ModelSeparator.TYPE_TOMORROW:
                containsSeparatorTomorrow = false;
                break;

            case ModelSeparator.TYPE_FUTURE:
                containsSeparatorFuture = false;
                break;

        }

    }


    public void removeAllItems() {
        if (getItemCount() != 0) {
            items = new ArrayList<>();
            notifyDataSetChanged();
            containsSeparatorOverdue = false;
            containsSeparatorToday = false;
            containsSeparatorTomorrow = false;
            containsSeparatorFuture = false;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView date;
        protected CircleImageView priority;

        public TaskViewHolder(View itemView, TextView title, TextView date, CircleImageView priority) {

            super(itemView);
            this.title = title;
            this.date = date;
            this.priority = priority;
        }
    }

    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {


        protected TextView type;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    public TaskFragment getTaskFragment() {
        return taskFragment;
    }
}