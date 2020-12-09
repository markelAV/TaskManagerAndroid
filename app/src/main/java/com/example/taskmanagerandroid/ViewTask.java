package com.example.taskmanagerandroid;

import android.content.Intent;
import android.os.Bundle;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewTask extends AppCompatActivity {

    DatabaseAdapter adapter;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter = new DatabaseAdapter(this);

        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id_task");
            String name = intent.getStringExtra("name_task");
            String date = intent.getStringExtra("date_task");
            String time = intent.getStringExtra("time_task");
            String description = intent.getStringExtra("description_task");
            task = new Task(name, time, date, id ,description, false);
            System.out.println(name + " " + time + " " + description);
            TextView viewName = (TextView) findViewById(R.id.id_text_task_name);
            TextView viewTime = findViewById(R.id.id_text_task_date);
            TextView viewDescription = findViewById(R.id.id_text_task_description);
            viewName.setText(name);
            viewTime.setText(time);
            viewDescription.setText(description);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_task_add :
                Intent intent = new Intent(this, CreateEditTask.class);
                startActivity(intent);
                return true;
            case R.id.action_task_edit:

                Intent intentEdit = new Intent(this, CreateEditTask.class);
                intentEdit.putExtra("id_task", task.getId());
                intentEdit.putExtra("name_task", task.getName());
                intentEdit.putExtra("date_task", task.getDate());
                intentEdit.putExtra("time_task", task.getTime());
                intentEdit.putExtra("description_task", task.getDescription());
                startActivity(intentEdit);
                return true;
            case R.id.action_task_delete:
                deleteTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTask() {
        if (task != null && task.getId() != null) {
            System.out.println("Delete Task");
            adapter.open();
            adapter.delete(task.getId());
            adapter.close();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}