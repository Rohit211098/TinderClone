package com.example.tinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    private EditText mEmail,mPassword;
    private Button mRegistration;


    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mRegistration = findViewById(R.id.button_registeration);
    }
}
