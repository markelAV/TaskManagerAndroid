package com.example.taskmanagerandroid;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ViewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name_task");
            String time = intent.getStringExtra("time_task");
            String description = intent.getStringExtra("description_task");
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
                TextView viewName = (TextView) findViewById(R.id.id_text_task_name);
                TextView viewTime = findViewById(R.id.id_text_task_date);
                TextView viewDescription = findViewById(R.id.id_text_task_description);

                String name = viewName.getText().toString();
                String date = viewTime.getText().toString();
                String description = viewDescription.getText().toString();

                Intent intentEdit = new Intent(this, CreateEditTask.class);
                intentEdit.putExtra("name_task", name);
                intentEdit.putExtra("time_task", date);
                intentEdit.putExtra("description_task", description);
                startActivity(intentEdit);
                return true;
            case R.id.action_task_delete:
                deleteTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTask() {
        //Todo add modal window for approve delete task
        System.out.println("Delete Task");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}