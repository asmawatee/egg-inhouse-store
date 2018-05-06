package com.example.user.appstore.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.appstore.R;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.model.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by User on 21/3/2561.
 */

public class ApplicationActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Version;
    private TextView Download;
    private TextView Description;
    private TextView PackageId;
    private TextView VersianCode;
    private ImageView imageView;
    private TextView Date;

    private static final String TAG = "ApplicationActivity";

    public DatabaseReference myRef;
    public FirebaseDatabase database;

    public ApplicationModel appdatabase;

    SimpleDateFormat simpleDateFormat;
    String date ;
    Calendar calander;



//    public  RecyclerView  recyclerView;

    private  int GALLERY_INTENT = 2 ;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        Name = (TextView) findViewById(R.id.name);
        Version = (TextView) findViewById(R.id.version);
        Download = (TextView) findViewById(R.id.download);
        Description = (TextView) findViewById(R.id.description);
        PackageId = (TextView) findViewById(R.id.packageid);
        VersianCode = (TextView) findViewById(R.id.versianconde);

        Date = (TextView) findViewById(R.id.textdate);
        imageView = (ImageView) findViewById(R.id.imagview);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Application Detail");
        }


        String appId = getIntent().getStringExtra("id"); //รับค่าid ที่ส่ง จากหน้า HomeFrament

        if (appId == null) { //ตรวจสอบค่า ว่ามีค่ามั้ย
            Toast.makeText(this, "AppId is null", Toast.LENGTH_SHORT).show();
            return;
        }


        FirebaseDatabase.getInstance()
                .getReference("Applications")
                .child(appId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot ) {
//                        Log.d(TAG, "onDataChange: " + dataSnapshot.toString());

                        ApplicationModel model = dataSnapshot.getValue(ApplicationModel.class);
                        if (model == null) {
                            Toast.makeText(ApplicationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        model.setId(dataSnapshot.getKey());//ดึงรายละเอียดแอพของมา
                        Name.setText(model.getApp_name());
                        Version.setText("Version : " +model.getApp_version());
                        Download.setText(+model.getDownload()+" Download");
                        Description.setText(model.getDescription());
                        PackageId.setText(model.getPackage_id());
                        VersianCode.setText(model.getVersion_code());
                        //วันที่
                        simpleDateFormat = new SimpleDateFormat (" d MMM yyyy");
                        date = simpleDateFormat.format(model.getTimestamp());
                        Date.setText("Update : "+ date);
                        Glide.with(ApplicationActivity.this).load(model.getAppIconUrl()).into(imageView);

                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError ) {

                    }
                });

    }

}
