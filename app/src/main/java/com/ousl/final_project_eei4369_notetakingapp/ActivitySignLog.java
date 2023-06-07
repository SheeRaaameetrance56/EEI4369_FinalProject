package com.ousl.final_project_eei4369_notetakingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class ActivitySignLog extends AppCompatActivity {

    // component variables
    Button sign_button, login_button;
    EditText text_name, text_email, text_password;
    CheckBox isRemember;
    TextView error_text_name, error_text_email, error_text_password;

    // Database class
    DataBaseHelper dataBaseHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_log);

        // component initialize
        sign_button = findViewById(R.id.btn_sign);
        login_button = findViewById(R.id.btn_login);

        text_name = findViewById(R.id.name_text);
        text_email = findViewById(R.id.email_text);
        text_password = findViewById(R.id.password_text);

        isRemember = findViewById(R.id.is_remember);

        error_text_name = findViewById(R.id.errorTextName);
        error_text_email = findViewById(R.id.errorTextEmail);
        error_text_password = findViewById(R.id.errorTextPassword);

        error_text_name.setTextColor(Color.parseColor("#008000"));
        error_text_name.setText("*Name doesn't required for login");
        dataBaseHelper = new DataBaseHelper(this);



        // Button methods
        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = text_name.getText().toString();
                String email = text_email.getText().toString();
                String password = text_password.getText().toString();
                boolean isRememberActivated = isRemember.isActivated();

                error_text_name.setTextColor(Color.parseColor("#FF0303"));

                // Check validation
                if(name.equals("") && email.equals("") && password.equals("")){
                    error_text_name.setText("*Name is required");
                    error_text_email.setText("*E-mail is required");
                    error_text_password.setText("*Password is required");
                    Toast.makeText(ActivitySignLog.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                }
                else if (name.equals("")) {
                    error_text_name.setText("*Name is required");
                }
                else if (email.equals("")) {
                    error_text_email.setText("*E-mail is required");
                }
                else if (password.equals("")) {
                    error_text_password.setText("*Password is required");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    error_text_email.setText("*Please enter valid email");
                }
                else if (password.length()<8) {
                    error_text_password.setText("*Password must contain at least 8 characters");
                } else {
                    boolean checkEmail = dataBaseHelper.checkEmail(email);
                    if(checkEmail == true) {
                        error_text_email.setText("*E-mail is currently exist");
                    }
                    else {
                        try {
                            // Passing data to the profile model class
                            ProfileModel profileModel = new ProfileModel(name, email, password, isRememberActivated);
                            dataBaseHelper.addProfile(profileModel);
                            Intent intent = new Intent(ActivitySignLog.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                            text_name.setText("");
                            text_email.setText("");
                            text_password.setText("");

                            error_text_name.setText("");
                            error_text_email.setText("");
                            error_text_password.setText("");
                        } catch (Exception e) {
                            Log.e(TAG, String.valueOf(e));
                            Toast.makeText(ActivitySignLog.this, "Error...! Cannot insert data" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }


//                ProfileModel pf = new ProfileModel(-1, text_name.getText().toString(), text_email.getText().toString(), text_password.getText().toString(), isRemember.isActivated());

            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = text_email.getText().toString();
                String password = text_password.getText().toString();
                Boolean checkEmailPassword = dataBaseHelper.checkEmailPassword(email, password);

                // check validations
                if(email.equals("")){
                    error_text_email.setText("*E-mail is required");
                }
                else if(password.equals("")){
                    error_text_password.setText("*Password is required");
                }
                else {
                    if (checkEmailPassword == true) {

                        Intent intent = new Intent(ActivitySignLog.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ActivitySignLog.this, "The Email or Password Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}