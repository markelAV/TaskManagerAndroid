package com.example.taskmanagerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.taskmanagerandroid.adapter.TaskAdapter;
import com.example.taskmanagerandroid.data.model.Task;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Task> tasks;
    private ListView taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        String date = new Date().toString();
        MaterialTextView editTextDate = (MaterialTextView) (findViewById(R.id.text_date_today));
        editTextDate.setText(date);
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                System.out.println("Selected date is " + dayOfMonth +"." + month +"." + year);
                // Todo Complete
            }
        });
        if(tasks == null) {
            tasks = new ArrayList<>();
            tasks.add(new Task("NameTask1","12:10","1", "tesDesciptionTask1"));
            tasks.add(new Task("NameTask2","12:11","3", "tesDesciptionTask2"));
            tasks.add(new Task("NameTask3","12:13","4", "tesDesciptionTask3"));
            tasks.add(new Task("NameTask4","12:14","5", "tesDesciptionTask4"));
            tasks.add(new Task("NameTask5","12:15","6", "tesDesciptionTask5"));
        }

        taskList = (ListView) findViewById(R.id.taskList);
        TaskAdapter adapter = new TaskAdapter(this, R.layout.task_list_item, tasks);
        taskList.setAdapter(adapter);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), ViewTask.class);
                intent.putExtra("name_task", task.getName());
                intent.putExtra("time_task", task.getTime());
                intent.putExtra("description_task", task.getDescription());
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public void goToAddTask(View view) {
        System.out.println("call goToAddTask");
        Intent intent = new Intent(this, CreateEditTask.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_add :
                Intent intent = new Intent(this, CreateEditTask.class);
                startActivity(intent);
                return true;
            case R.id.action_synchronize:
                this.synchronize();
                return true;
            case R.id.open_track:
                System.out.println("Проложить маршрут");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean synchronize () {
        //Todo complete result for cache errors when send request on server
        boolean result = false;
        System.out.println("Call synchronize");
        return result;
    }
    public void goToViewTask(View view) {
        System.out.println("call goToViewTask");
        Intent intent = new Intent(this, ViewTask.class);
        MaterialTextView editTextTaskName = (MaterialTextView) (findViewById(R.id.id_text_task_name));
        MaterialTextView editTextTaskDate = (MaterialTextView) (findViewById(R.id.id_text_task_date));
        MaterialTextView editTextTaskDescription = (MaterialTextView) (findViewById(R.id.id_text_task_description));
        //editTextTaskName.setText(new String("Test name"));
        editTextTaskDate.setText(new Date().toString());
        editTextTaskDescription.setText(new String("Test descripton "));
        startActivity(intent);
    }
}