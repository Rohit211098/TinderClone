package com.example.tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistrationAndLogin extends AppCompatActivity {

    private TextView loign,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_and_login);

        loign = findViewById(R.id.login);
        register = findViewById(R.id.register);


        loign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationAndLogin.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationAndLogin.this,Registration.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
