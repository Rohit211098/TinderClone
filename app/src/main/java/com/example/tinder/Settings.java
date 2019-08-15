package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    public static String TAG = "tag";


    private ImageView userImage;
    private EditText userName,userPhone;
    private Button conform,back;
    private DatabaseReference db;
    StorageReference storageReference;
    private FirebaseAuth auth;
    private String mName,mPhone,mProfilePic,userSex;
    FirebaseUser mUserId;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

         userSex = getIntent().getExtras().getString("userSex");
          userImage = findViewById(R.id.setting_image);
        userName = findViewById(R.id.setting_name);
        userPhone = findViewById(R.id.setting_phone);
        conform = findViewById(R.id.setting_conform);
        back = findViewById(R.id.setting_back);
        auth = FirebaseAuth.getInstance();
        mUserId = auth.getCurrentUser();


        db = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(mUserId.getUid());

        getUserInfo();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"in user image click");
                fileOpen();

            }
        });

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserSettings();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });


    }

    private void selectImage() {
        Log.e(TAG,"in select image");


        if (uriImage != null){
             storageReference = FirebaseStorage.getInstance().getReference().child("profileImages").child(mUserId.getUid());
            Bitmap bitmap =null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),uriImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url  = uri.toString();
                            Map userInfo = new HashMap();
                            userInfo.put("imageUrl",url);
                            db.updateChildren(userInfo);

//                            finish();
//                            return;

                        }
                    });
                    Toast.makeText(Settings.this,"upload sucessfull", Toast.LENGTH_SHORT).show();
                }
            });




        }else{
            finish();
        }




    }

    private void fileOpen(){
        Log.e(TAG,"in open image");
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"in activity result");

        if (requestCode == 1 && resultCode == Activity.RESULT_OK ){
            uriImage = data.getData();

             userImage.setImageURI(uriImage);
             Log.e(TAG,userImage+" user image uri");
            selectImage();



//            Glide.with(this).load(uri).apply(RequestOptions.circleCropTransform()).into(profilePicture);
//            onUplode();
        }
    }

    private void getUserInfo() {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String,Object> map = ( Map<String,Object>)dataSnapshot.getValue();
                    if (map.get("name")!=null){
                        mName = map.get("name").toString();

                        userName.setText(mName);
                    }
                    if (map.get("phone")!=null){
                        mPhone = map.get("phone").toString();

                        userPhone.setText(mPhone);
                    }
                    if (map.get("imageUrl")!=null){
                        mProfilePic = map.get("imageUrl").toString();

                        Glide.with(getApplicationContext()).load(mProfilePic).into(userImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveUserSettings() {
        mName = userName.getText().toString().trim();
        mPhone = userPhone.getText().toString().trim();

        if (mName.equals("") && mPhone.equals("")){
            Toast.makeText(getApplicationContext()," Feilds cannot be empty",Toast.LENGTH_SHORT).show();
        }else {
            Map userInfo = new HashMap();
            userInfo.put("name",mName);
            userInfo.put("phone",mPhone);
            db.updateChildren(userInfo).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(getApplicationContext()," Details updated ",Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            });
        }




    }



}
