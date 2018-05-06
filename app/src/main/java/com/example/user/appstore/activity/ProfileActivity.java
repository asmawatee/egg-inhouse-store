package com.example.user.appstore.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.appstore.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private ImageView photpImageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView levelTextView;


    public DatabaseReference myRef;
    public FirebaseDatabase database;
//    public UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        photpImageView = (ImageView) findViewById(R.id.photpImage);
        nameTextView = (TextView) findViewById(R.id.nametext);
        emailTextView = (TextView) findViewById(R.id.emailtext);
        levelTextView = (TextView) findViewById(R.id.textlevel);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Profile");
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //การดึกข้อมูล
        database = FirebaseDatabase.getInstance();


        if (user != null) {
            //String uid = ustText(uid);
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();


            myRef = database.getReference("Users").child(user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String level = dataSnapshot.child("level").getValue().toString();
                    levelTextView.setText("Level :" +level);
//                    Log.d("haaaa",dataSnapshot.child("level").getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            myRef.keepSynced(true);
            //String uid = user.getUid();
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(new RequestOptions().circleCrop())
                    .into(photpImageView);

            nameTextView.setText("Username :" +name);
            emailTextView.setText("Email :" +email);
            photpImageView.setImageURI(photoUrl);


        } else {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            //goLoginScreen();
        }
    }


    public void logout(View view) {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);// เคลียร์หน้าlogin ถ้าหน้านั้นมีคน login แล้ว
        startActivity(intent);
        finish();
    }

//        goLoginScreen();
//    }


    @Override
    public void onBackPressed() { //การเคลียร์หน้าแต่ล่ะหน้าให้มันไม่เห็นหน้าปัจุบัน
        if (isTaskRoot()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


