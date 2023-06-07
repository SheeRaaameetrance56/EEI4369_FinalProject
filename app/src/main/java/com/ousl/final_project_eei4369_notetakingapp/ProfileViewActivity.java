package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileViewActivity extends AppCompatActivity {

    Button Btn_Logout;

    private SimpleCursorAdapter adapter;

    final String[] from= new String[]{
            DataBaseHelper.SIGN_ID,
            DataBaseHelper.NAME,
            DataBaseHelper.EMAIL
    };

    final int[] to = new int[]{R.id.textId, R.id.textName, R.id.textEmail};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Btn_Logout = findViewById(R.id.Btn_logout);
        Btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileViewActivity.this, ActivitySignLog.class);
                startActivity(intent);
                finish();
            }
        });

//        ProfileData profileData = new ProfileData();
//        String email = profileData.getEmail();
        Cursor cursor = DB_Manager.fetchProfile(this);

        // profile view on adapter to profile layout
        adapter = new SimpleCursorAdapter(this, R.layout.profile_layout,cursor,from,to);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}