package com.example.taskmanagerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;

public class CreateEditTask extends AppCompatActivity implements LocationListener {

    private EditText viewName;
    private EditText viewDate;
    private EditText viewTime;
    private EditText viewDescription;
    private String taskId = null;
    private Location currentLocation;

    private final long LOCATION_REFRESH_TIME = 5000;
    private final float LOCATION_REFRESH_DISTANCE = 10;

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private DatabaseAdapter adapter;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);

        currentLocation = null;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            System.out.println("Location not available");
            System.out.println("Location not available");
        }

        this.viewName = (EditText) findViewById(R.id.edit_text_task_name);
        this.viewDate = (EditText) findViewById(R.id.edit_text_date);
        this.viewTime = (EditText) findViewById(R.id.edit_text_time);
        this.viewDescription = (EditText) findViewById(R.id.editTextTextMultiLine);

        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
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

       /* if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }
        }*/


        /*try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, this);
        } catch (SecurityException ex) {
            System.out.println("[ERROR] security " + ex.getMessage());
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                System.out.println("Get Access location");
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
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
        if (this.taskId != null) {
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
        if (this.currentLocation != null) {
            String[] locations = currentLocation.toString().split(" ");
            if (locations != null && locations.length > 3) {
                String[] cords = locations[1].split(",");
                if (cords != null && cords.length > 3) {
                    location = "[" + cords[0] + "." + cords[1] + ", " + cords[2] + "." + cords[3] + "]"; // ГОСТ 5812-82
                }
            }
            String cord = currentLocation.toString();
        }
        return location;
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }
    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm);
        }
        return  true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
        System.out.println("[DEBUG] my location is " + location.toString());
    }
}