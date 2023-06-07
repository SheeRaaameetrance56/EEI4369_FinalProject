package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    // class database manager (DB_Manager)
    private DB_Manager dbManager;


    // components
    private ListView listView;
    private TextView profileName;
    private Button profileBtn;
    private FloatingActionButton addNoteButton;

    // cursor adapter
    private SimpleCursorAdapter adapter;

    // Sensor managers
    private SensorManager sensorManager;
    private ProximitySensor mProximitySensor;

    // list model of fetching notes data
    final String[] from = new String[] {
            DataBaseHelper.NOTES_ID,
            DataBaseHelper.Title,
            DataBaseHelper.Date,
            DataBaseHelper.Location,
            DataBaseHelper.Content,
    };

    // list of set data fetched from database
    final int[] to = new int[] {R.id.noteId,R.id.noteTitle, R.id.noteDate, R.id.noteLocation, R.id.noteContent};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DB_Manager(this);
        dbManager.open();

        listView = findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty));
        addNoteButton = findViewById(R.id.noteAddingBtn);
        profileBtn = findViewById(R.id.btn_profile);

        Cursor cursor = dbManager.fetch();
        adapter = new SimpleCursorAdapter(this, R.layout.activity_note_view, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        mProximitySensor = new ProximitySensor(this);

        //on Click listener for new note
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // on Click listener for item view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView idTextView = view.findViewById(R.id.noteId);
                TextView titleTextView = view.findViewById(R.id.noteTitle);
                TextView dateTextView = view.findViewById(R.id.noteDate);
                TextView locationTextView = view.findViewById(R.id.noteLocation);
                TextView contentTextView = view.findViewById(R.id.noteContent);

//                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String date = dateTextView.getText().toString();
                String location = locationTextView.getText().toString();
                String content = contentTextView.getText().toString();

                Intent modifyIntent = new Intent(getApplicationContext(), ModifyNoteActivity.class);
                modifyIntent.putExtra("_id", String.valueOf(id));
                modifyIntent.putExtra("title", title);
                modifyIntent.putExtra("date", date);
                modifyIntent.putExtra("location", location);
                modifyIntent.putExtra("content", content);

                startActivity(modifyIntent);

            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);
                startActivity(intent);
            }
        });

    }

    // navigation back pressed
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exiting from app")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

/*
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == R.id.add_record){
            Intent add_mem = new Intent(this,AddNoteActivity.class);
            startActivity(add_mem);
        }


        return super.onOptionsItemSelected(item);
    }
*/


    // Proximity sensor
    @Override
    protected void onResume() {
        super.onResume();
        mProximitySensor.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProximitySensor.unregister();
        if (mProximitySensor.mWakeLock.isHeld()) {
            mProximitySensor.mWakeLock.release();
        }
    }


    public static class ProximitySensor implements SensorEventListener {
        private final Context mContext;
        PowerManager.WakeLock mWakeLock;

        public ProximitySensor(Context context) {
            mContext = context;
            PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "myapp:proximityScreenOff");
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                float distance = event.values[0];
                if (distance < event.sensor.getMaximumRange()) {
                    // Turn off the screen
                    mWakeLock.acquire();
                } else {
                    // Turn on the screen
                    if (mWakeLock.isHeld()) {
                        mWakeLock.release();
                    }
                }
            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Do nothing
        }

        public void register() {
            SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            if (proximitySensor != null) {
                sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }

        public void unregister() {
            SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            sensorManager.unregisterListener(this);
        }
    }
}