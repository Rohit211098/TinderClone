package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {
    private EditText mEmail,mPassword,mName;
    private Button mRegistration;
    private String email,password,name;
    private RadioGroup radioGroup;



    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener stateListener ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mRegistration = findViewById(R.id.button_Registration);
        mName = findViewById(R.id.register_name);
        radioGroup =findViewById(R.id.radio_group);

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = auth.getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Registration.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setup();

            }
        });


    }

    private void setup() {

        int selectedId = radioGroup.getCheckedRadioButtonId();

        final RadioButton radioButton = findViewById(selectedId);

        if (radioButton.getText() == null){
            return;
        }

        name = mName.getText().toString().trim();

        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Email or Password cannot be empty",Toast.LENGTH_SHORT).show();
        }else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Registration.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"sign up error",Toast.LENGTH_SHORT).show();
                    }else {
                        String id = auth.getCurrentUser().getUid();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
                        Map userInfo = new HashMap();
                        userInfo.put("name",name);
                        userInfo.put("sex",radioButton.getText().toString());

                        db.updateChildren(userInfo);

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
