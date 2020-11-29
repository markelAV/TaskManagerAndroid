package com.example.taskmanagerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateEditTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);

        Intent intent = getIntent();
        if(intent != null) {
            String name = intent.getStringExtra("name_task");
            String time = intent.getStringExtra("time_task");
            String description = intent.getStringExtra("description_task");
            System.out.println(name + " " + time + " " + description);
            EditText viewName = (EditText) findViewById(R.id.editTextTextPersonName);
            EditText viewTime = (EditText) findViewById(R.id.editTextDate);
            EditText viewDescription = (EditText) findViewById(R.id.editTextTextMultiLine);
            viewName.setText(name);
            viewTime.setText(time);
            viewDescription.setText(description);

        }
    }

    public void saveTask(View view) {
        System.out.println("call saveTask");
        //Todo complete save in local database
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}