package com.ousl.final_project_eei4369_notetakingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.SupportMapFragment;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class AddNoteActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private Location lastLocation;
    double d_lat, d_long;
    String fetched_address = "";
    String fetched_admin = "";



    // Widgets
    private EditText noteTitle, noteDate, noteLocation, noteContent;
    private Button proceedButton;
    private ImageButton locationButton, photoButton, videoButton;
    private DB_Manager dbManager;

    // -----------------------------------on create method----------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Note");
        setContentView(R.layout.activity_add_note);

        // Location providers
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);

        // Widget instantiation
        noteTitle = findViewById(R.id.note_title);
        noteDate = findViewById(R.id.note_date);
        // Set date
        String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        noteDate.setText(date);

        noteLocation = findViewById(R.id.note_location);
        noteContent = findViewById(R.id.note_content);

        proceedButton = findViewById(R.id.proceedBtn);
//        cancelButton = findViewById(R.id.cancelBtn);
        locationButton = findViewById(R.id.locationBtn);
        photoButton = findViewById(R.id.photoBtn);
        videoButton = findViewById(R.id.videoBtn);

        // data base initialization
        dbManager = new DB_Manager(this);
        dbManager.open();

//        proceedButton.setOnClickListener(this);

        // proceed button
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = noteTitle.getText().toString();
                final String date = noteDate.getText().toString();
                final String location = noteLocation.getText().toString();
                final String content = noteContent.getText().toString();

                dbManager.insert(title, date, location, content);
                Intent main = new Intent(AddNoteActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);

            }
        });

        //  location button
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this, MapsLocationActivity.class);
                startActivity(intent);
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivity(intent);
            }
        });

//        noteLocation.setText("Location");

        // Calling permission
        checkLocationPermission();

        init();
    }

    // ---------------Location permission-----------------
    public void checkLocationPermission() {
        Log.d(TAG, "inside check location");

        if (ContextCompat.checkSelfPermission(AddNoteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddNoteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(AddNoteActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                ActivityCompat.requestPermissions(AddNoteActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(AddNoteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
        }
    }

    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d(TAG, "Location Settings are ok");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    Log.d(TAG, "Inside error ->"+statusCode);
                });
    }

    public void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(task -> {Log.d(TAG, "Stop location updates");});
    }

    private void receiveLocation(LocationResult locationResult){
        lastLocation = locationResult.getLastLocation();

        Log.d(TAG,"latitude "+ lastLocation.getLatitude());
        Log.d(TAG,"longitude "+ lastLocation.getLongitude());
        Log.d(TAG,"altitude "+ lastLocation.getAltitude());

        String s_lat = String.format(Locale.ROOT,"%.6f", lastLocation.getLatitude());
        String s_long = String.format(Locale.ROOT,"%.6f", lastLocation.getLongitude());
//        String s_alt = String.format(Locale.ROOT,"%.6f", lastLocation.getAltitude());

        d_lat = lastLocation.getLatitude();
        d_long = lastLocation.getLongitude();


        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(d_lat, d_long,1);

            fetched_address = addresses.get(0).getAddressLine(0);
            fetched_admin = addresses.get(0).getSubAdminArea();

            Log.d(TAG, ""+fetched_address);
            noteLocation.setText(fetched_address+" "+fetched_admin+"");
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void init(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                receiveLocation(locationResult);
            }
        };

//        locationRequest = LocationRequest.create()
//                .setInterval(5000)
//                .setFastestInterval(500)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setMaxWaitTime(100);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setMinUpdateIntervalMillis(500)
                .setMinUpdateDistanceMeters(1)
                .setWaitForAccurateLocation(true)
                .build();


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addAllLocationRequests(Collections.singleton(locationRequest));
        locationSettingsRequest = builder.build();
        startLocationUpdates();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}