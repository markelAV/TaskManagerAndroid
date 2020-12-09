package com.example.taskmanagerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;

public class CreateEditTask extends AppCompatActivity {

    EditText viewName;
    EditText viewDate;
    EditText viewTime;
    EditText viewDescription;
    private String taskId = null;

    DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);

        this.viewName = (EditText) findViewById(R.id.edit_text_task_name);
        this.viewDate = (EditText) findViewById(R.id.edit_text_date);
        this.viewTime = (EditText) findViewById(R.id.edit_text_time);
        this.viewDescription = (EditText) findViewById(R.id.editTextTextMultiLine);

        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            taskId = extras.getString("id_task");
        }
        if (taskId != null) {
            String name = extras.getString("name_task");
            String time = extras.getString("time_task");
            String date = extras.getString("date_task");
            String description = extras.getString("description_task");
            System.out.println(name + " " + time + " " + description);

            viewName.setText(name);
            viewTime.setText(time);
            viewDate.setText(date);
            viewDescription.setText(description);

        }
    }

    public void saveTask(View view) {
        System.out.println("call saveTask");
        String nameTask = this.viewName.getText().toString();
        String date = this.viewDate.getText().toString();
        String description = this.viewDescription.getText().toString();
        System.out.println("[Debug] description " + description);
        String time = this.viewTime.getText().toString();
        Boolean complete = false; //Todo how feature in future
        Task task = new Task(nameTask, time, date, this.taskId, description, complete);

        adapter.open();
        if(this.taskId != null) {
            this.adapter.update(task);
        } else {
            adapter.insert(task);
        }
        adapter.close();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}