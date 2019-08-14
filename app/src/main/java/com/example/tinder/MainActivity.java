package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "tag";
    private User userClass;
    private CustomArrayAdapter arrayAdapter;
    private int i;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private Button logout;
   private DatabaseReference db = FirebaseDatabase.getInstance().getReference();
   ListView listView;
    String userSex,oppositeUserSex;
   List<User> rowItems;
   DatabaseReference userDB = db.child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,user.getUid());
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        checkUserSex();




        rowItems = new ArrayList<User>();


        arrayAdapter = new CustomArrayAdapter(this,R.layout.item,rowItems);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {

                User obj = (User) dataObject;
                String userID = obj.getUserID();
                userDB.child(oppositeUserSex).child(userID).child("Connections").child("no").child(user.getUid()).setValue(true);


                makeToast(MainActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                User obj = (User) dataObject;
                String userID = obj.getUserID();
                userDB.child(oppositeUserSex).child(userID).child("Connections").child("yes").child(user.getUid()).setValue(true);
                isConnectionMatch(userID);
                makeToast(MainActivity.this, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });



    }

    private void isConnectionMatch(String userID) {

        DatabaseReference currentUserConnections = userDB.child(userSex).child(user.getUid()).child("Connections").child("yes").child(userID);
        currentUserConnections.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.e(TAG,dataSnapshot.getKey());

                    makeToast(MainActivity.this, "Match!");
                    userDB.child(oppositeUserSex).child(dataSnapshot.getKey()).child("Connections").child("matches").child(user.getUid()).setValue("true");
                    userDB.child(userSex).child(user.getUid()).child("Connections").child("matches").child(dataSnapshot.getKey()).setValue("true");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }




    private void checkUserSex(){

        checkMale();
        checkFemale();






    }

    private void checkMale(){
        DatabaseReference maleDatabase = db.child("Users").child("Male");
        maleDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.e(TAG,dataSnapshot.getKey()+"  equalMale  "+user.getUid());
                if(dataSnapshot.getKey().equals(user.getUid())){

                    userSex ="Male";
                    oppositeUserSex = "Female";
                    displayOppositeSex();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void checkFemale(){
        DatabaseReference femaleDatabase = db.child("Users").child("Female");
        femaleDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.e(TAG,dataSnapshot.getKey()+"  equalFemale  "+user.getUid());
                if(dataSnapshot.getKey().equals(user.getUid())){

                    userSex ="Female";
                    oppositeUserSex = "Male";
                    displayOppositeSex();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    String userName;

    private void displayOppositeSex(){
        DatabaseReference oppositeUserDatabase = db.child("Users").child(oppositeUserSex);
        oppositeUserDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()&&!dataSnapshot.child("Connections").child("no").hasChild(user.getUid())&&!dataSnapshot.child("Connections").child("yes").hasChild(user.getUid())){
                   rowItems.add(new User(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString()));
                   arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void logout(){
        auth.signOut();
        Intent intent = new Intent(MainActivity.this,RegistrationAndLogin.class);
        startActivity(intent);
        finish();
    }




}
