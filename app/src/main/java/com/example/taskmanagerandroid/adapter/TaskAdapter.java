package com.example.taskmanagerandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerandroid.CreateEditTask;
import com.example.taskmanagerandroid.R;
import com.example.taskmanagerandroid.ViewTask;
import com.example.taskmanagerandroid.data.model.Task;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private LayoutInflater inflater;
    private  int layout;
    private ArrayList<Task> tasks;

    public TaskAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Task> tasks) {
        super(context, resource, tasks);
        this.tasks = tasks;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         //Todo complete and optimize
        final ViewHolder viewHolder;
        if ( convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Task task = tasks.get(position);

        viewHolder.nameView.setText(task.getName());
        viewHolder.id_taskView.setText(task.getId());
        viewHolder.timeView.setText(task.getTime());
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateEditTask.class);
               intent.putExtra("name_task", task.getName());
               intent.putExtra("time_task", task.getTime());
               intent.putExtra("description_task", task.getDescription());
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        final Button editButton;
        final TextView nameView, timeView, id_taskView;
        ViewHolder(View view){
            editButton = (Button) view.findViewById(R.id.editButton);
            nameView = (TextView) view.findViewById(R.id.nameView);
            timeView = (TextView) view.findViewById(R.id.timeView);
            id_taskView = (TextView) view.findViewById(R.id.id_taskView);
        }
    }
}


