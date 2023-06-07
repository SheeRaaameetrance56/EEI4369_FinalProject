 package com.ousl.final_project_eei4369_notetakingapp;

 import android.content.DialogInterface;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.ImageButton;
 import android.widget.Toast;

 import androidx.appcompat.app.AlertDialog;
 import androidx.appcompat.app.AppCompatActivity;

 public class ModifyNoteActivity extends AppCompatActivity {

     // Widgets
     private EditText modifyTitle, modifyDate, modifyLocation, modifyContent;
     private Button updateButton, deleteButton;
     private ImageButton locationButton;
     private long _id;
     private DB_Manager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);

        dbManager = new DB_Manager(this);
        dbManager.open();


        //Widget instantiation
        modifyTitle = findViewById(R.id.modify_note_title);
        modifyDate  = findViewById(R.id.modify_note_date);
        modifyLocation = findViewById(R.id.modify_note_location);
        modifyContent = findViewById(R.id.modify_note_content);

        updateButton = findViewById(R.id.modify_updateBtn);
        deleteButton = findViewById(R.id.modify_deleteBtn);
        locationButton = findViewById(R.id.locationBtn2);


        Intent intent = getIntent();
        String id = intent.getStringExtra("_id");
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String location = intent.getStringExtra("location");
        String content = intent.getStringExtra("content");

        _id = Long.parseLong(id);

        modifyTitle.setText(title);
        modifyDate.setText(date);
        modifyLocation.setText(location);
        modifyContent.setText(content);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyNoteActivity.this, MapsLocationActivity.class);
                startActivity(intent);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = modifyTitle.getText().toString();
                String Date = modifyDate.getText().toString();
                String Location = modifyLocation.getText().toString();
                String Content = modifyContent.getText().toString();

                dbManager.update(_id, Title, Date, Location, Content);
                this.returnHome();
                Toast.makeText(ModifyNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();

            }
            private void returnHome(){
                Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home_intent);

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ModifyNoteActivity.this)
                        .setTitle("Deleting note")
                        .setMessage("Are you sure you want to delete the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.delete(_id);
                                Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(home_intent);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }
}