package com.example.taskmanagerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.adapter.TaskAdapter;
import com.example.taskmanagerandroid.data.model.Task;
import com.example.taskmanagerandroid.service.TaskSynchronizeService;
import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String SELECT_DATE = "selectDate";
    private ArrayList<Task> tasks;
    private TaskAdapter adapter;

    private ListView taskList;
    private String selectDate;
    private CalendarView calendarView;
    private TextView notificationBannerUpdate;
    private TextView notificationBannerSynchronize;

    private Context contextTaskSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.contextTaskSync = this.getApplicationContext();
        String todayDate = new Date().toString();
        Date todayDate2 = new Date();
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        DateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy");
        String fromat = formatter1.format(todayDate2);
        fromat = fromat.charAt(0) == '0' ? fromat.substring(1) : fromat;
        this.selectDate = fromat;
        String[] datesForView = todayDate.split(" ");
        String dateForView = datesForView[0] + " " + datesForView[1] + " " + datesForView[2]; //Fixme use StringBuilder
        MaterialTextView editTextDate = (MaterialTextView) (findViewById(R.id.text_date_today));
        editTextDate.setText(dateForView);
        notificationBannerUpdate = (TextView) findViewById(R.id.notification_banner_update);
        notificationBannerSynchronize = (TextView) findViewById(R.id.notification_banner_synchronize);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                StringBuilder builder = new StringBuilder();
                builder.append(dayOfMonth)
                        .append('.')
                        .append(month + 1)
                        .append('.')
                        .append(year);
                selectDate = builder.toString();
                System.out.println("New selected date is " + selectDate);
                updateListTask();

            }
        });

        taskList = (ListView) findViewById(R.id.taskList);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SELECT_DATE, this.selectDate);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.selectDate = savedInstanceState.getString(SELECT_DATE);
        DateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = formatter1.parse(this.selectDate);
            this.calendarView.setDate(date.getTime());
        } catch (ParseException ex) {
            System.out.println("Error not selected");
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseAdapter adapterDatabase = new DatabaseAdapter(this);
        adapterDatabase.open();
        System.out.println("Get task for date: " + this.selectDate);
        ArrayList<Task> tasks = (ArrayList<Task>) adapterDatabase.getTaskByDate(this.selectDate);
        if (tasks == null) {
            tasks = new ArrayList<>(); // getTaskByDate can return null for that case
        } else {
            Collections.sort(tasks);
        }

        adapter = new TaskAdapter(this, R.layout.task_list_item, tasks);
        taskList.setAdapter(adapter);
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                Intent intent = new Intent(view.getContext(), ViewTask.class);
                intent.putExtra("id_task", task.getId());
                intent.putExtra("name_task", task.getName());
                intent.putExtra("date_task", task.getDate());
                intent.putExtra("time_task", task.getTime());
                intent.putExtra("description_task", task.getDescription());
                view.getContext().startActivity(intent);
            }
        });

        adapterDatabase.close();
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
        switch (id) {
            case R.id.action_add:
                Intent intent = new Intent(this, CreateEditTask.class);
                startActivity(intent);
                return true;
            case R.id.action_synchronize:
                this.synchronize();
                return true;
            case R.id.action_load_tasks:
                this.updateTasks();
            case R.id.open_track:
                System.out.println("Проложить маршрут");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateListTask() {
        DatabaseAdapter adapterDatabase = new DatabaseAdapter(this);
        adapterDatabase.open();
        System.out.println("Get task for date: " + this.selectDate);
        List<Task> tasks = adapterDatabase.getTaskByDate(this.selectDate);

        adapter.clear();

        if (tasks != null) {
            Collections.sort(tasks);
            adapter.addAll(tasks);
        }

        adapter.notifyDataSetChanged();

        adapterDatabase.close();
        System.out.println("Adapter is updated");
    }

    private boolean synchronize() {
        //Todo complete result for cache errors when send request on server
        notificationBannerSynchronize.setVisibility(View.VISIBLE);
        boolean result = false;
        System.out.println("Call synchronize");
        new ProgressTask().execute("synchronize");
        return result;
    }

    private void updateTasks() {
        notificationBannerUpdate.setVisibility(View.VISIBLE);
        System.out.println("Call update");
        new ProgressTask().execute("update");
    }


    public String getSelectDate() {
        return selectDate;
    }

    private class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... action) {
            String content = null;
            if (action != null && action.length > 0) {
                String currentAction = action[0];
                TaskSynchronizeService service = new TaskSynchronizeService(contextTaskSync);
                switch (currentAction) {
                    case "update":
                        content = service.updateTasksFromServer() > 0? currentAction: null;
                        return content;
                    case "synchronize":
                        content = service.synchronizeTasks()? currentAction: null;
                        return content;
                    case "test":
                        content = service.testConnection()? currentAction: null;
                        return content;
                }

            }


            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            //Todo complete work with notification
            notificationBannerUpdate.setVisibility(View.GONE);
            notificationBannerSynchronize.setVisibility(View.GONE);
            if(content != null) {
                System.out.println("[Debug] hide notification action is " + content);
                if(content.equals("update")){
                    System.out.println("[Debug] run updates list");
                    updateListTask();
                }
            }
            else {
                System.out.println("[Debug] don`t run updates list because content is null");
            }

        }
    }

}