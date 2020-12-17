package com.example.taskmanagerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;

public class CreateEditTask extends AppCompatActivity {

    private EditText viewName;
    private EditText viewDate;
    private EditText viewTime;
    private EditText viewDescription;
    private String taskId = null;
    private Location currentLocation;

    private final long LOCATION_REFRESH_TIME = 5000;
    private final float LOCATION_REFRESH_DISTANCE = 10;

    private DatabaseAdapter adapter;
    private LocationManager locationManager;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            currentLocation = location;
            System.out.println("[DEBUG] my location is " + location.toString()) ;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);

        currentLocation = null;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        }
        catch (SecurityException ex) {
            System.out.println("[ERROR] security " + ex.getMessage());
        }

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

    public void addLocation(View view) {
        StringBuilder stringBuilder = new StringBuilder(viewDescription.getText().toString());
        String currentLocation = getCurrentLocation();
        if(currentLocation != null) {
            stringBuilder.append("GPS: ").append(currentLocation);
        }

        viewDescription.setText(stringBuilder.toString());
    }

    //Fixme please use StringBuilder
    private String getCurrentLocation() {
        String location = null;
        if(this.currentLocation != null) {
            String[] locations = currentLocation.toString().split(" ");
            if(locations != null && locations.length > 3) {
                String[] cords = locations[1].split(",");
                if(cords != null && cords.length > 3) {
                    location = "[" + cords[0] + "." + cords[1] +", " +cords[2] + "." + cords[3] + "]"; // ГОСТ 5812-82
                }
            }
            String cord = currentLocation.toString();
        }
        return  location;
    }

}