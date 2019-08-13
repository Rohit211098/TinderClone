package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private Button mlogin;
    private String email,password;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener stateListener ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        mlogin = findViewById(R.id.button_login);

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = auth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setup();

            }
        });
    }


    private void setup() {

        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Email or Password cannot be empty",Toast.LENGTH_SHORT).show();
        }else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"sign up error",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(stateListener);
    }
}
