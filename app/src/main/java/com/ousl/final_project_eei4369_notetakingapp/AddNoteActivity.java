package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

public class AddNoteActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    // Widgets
    private EditText noteTitle, noteDate, noteLocation, noteContent;
    private Button proceedButton, cancelButton;
    private ImageButton locationButton;
    private DB_Manager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Note");
        setContentView(R.layout.activity_add_note);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(this);

        // Widget instantiation
        noteTitle = findViewById(R.id.note_title);
        noteDate = findViewById(R.id.note_date);
        noteLocation = findViewById(R.id.note_location);
        noteContent = findViewById(R.id.note_content);

        proceedButton = findViewById(R.id.proceedBtn);
//        cancelButton = findViewById(R.id.cancelBtn);
        locationButton = findViewById(R.id.locationBtn);

        dbManager = new DB_Manager(this);
        dbManager.open();

//        proceedButton.setOnClickListener(this);

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

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this, MapsLocationActivity.class);
                startActivity(intent);
            }
        });




    }
}