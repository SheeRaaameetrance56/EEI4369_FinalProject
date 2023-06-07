package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

    // Splash screen
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);  //Delay of 3 seconds
                } catch (Exception e) {
                    Toast.makeText(IntroActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
                } finally {

                    Intent i = new Intent(IntroActivity.this,
                            ActivitySignLog.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

}