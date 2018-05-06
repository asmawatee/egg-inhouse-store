package com.example.user.appstore.activity;



import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.appstore.R;
import com.example.user.appstore.model.ApplicationModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;


/**
 * Created by User on 4/4/2561.
 */

public class EditAppActivity extends AppCompatActivity {

    private static final String TAG = "EditAppActivity";

    private int GALLERY_INTENT = 2;

    private EditText editName;
    private EditText editVersion;
    private EditText editDescription;
    private EditText editPackageId;
    private EditText editVersianCode;
    private EditText editURL;
    private ImageView imageIcon;
    private Button saveEdit;

    private FirebaseDatabase database;
    public DatabaseReference myRef;

    //upload รูป
    private FirebaseStorage storage;
    private StorageReference storageReferencetorage;
    private ProgressDialog mProgressDialog;
    private Uri downloaUri;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_app);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Application");
        }


        editName = (EditText) findViewById(R.id.edit_name);
        editVersion = (EditText) findViewById(R.id.edit_version);
        editDescription = (EditText) findViewById(R.id.edit_descri);
        editPackageId = (EditText) findViewById(R.id.edit_package);
        editVersianCode = (EditText) findViewById(R.id.edit_versian);
        editURL = (EditText) findViewById(R.id.edit_url);
        imageIcon = (ImageView) findViewById(R.id.edit_imageicon);

        saveEdit = (Button) findViewById(R.id.save_edit);


        //ดึงid ของ app นั้นมา โดยตรวจสอบ id ว่า มีค่าว่างมั้ย
        String appId = getIntent().getStringExtra("id");

        if (appId == null) {
            Toast.makeText(this, "AppId is null", Toast.LENGTH_SHORT).show();
            return;
        }


        //แก้ไขข้อมูล
        FirebaseDatabase.getInstance()
                .getReference("Applications")
                .child(appId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot ) {
//                        Log.d("fff", dataSnapshot.toString());

                        ApplicationModel Model = dataSnapshot.getValue(ApplicationModel.class);
                        if (Model == null) {
                            Toast.makeText(EditAppActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Model.setId(dataSnapshot.getKey());  //ใส่ค่าidให้มัน
                        editName.setText(Model.getApp_name());
                        editVersion.setText(Model.getApp_version());
                        editDescription.setText(Model.getDescription());
                        editPackageId.setText(Model.getPackage_id());
                        editVersianCode.setText(Model.getVersion_code());
                        editURL.setText(Model.getApp_url());
                        Glide.with(EditAppActivity.this).load(Model.getAppIconUrl()).into(imageIcon);

                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError ) {

                    }


                });
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                SaveEdit();


            }
        });
        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                getContent();
            }
        });

        //บันทึกลงใน firebase

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Applications").child(appId);//การชี้ไปยังค่านั้น

        //upload รูป
        mProgressDialog = new ProgressDialog(this);
        storage = FirebaseStorage.getInstance();
        storageReferencetorage = storage.getReference();
    }

    private ApplicationModel createApplicationModel() {
        ApplicationModel model = new ApplicationModel();
        model.setApp_name(editName.getText().toString());
        model.setApp_version(editVersion.getText().toString());
        model.setDescription( editDescription.getText().toString());
        model.setPackage_id( editPackageId.getText().toString());
        model.setVersion_code( editVersianCode.getText().toString());
        model.setApp_url(editURL.getText().toString());
        model.setAppIconUrl(downloaUri.toString());
        model.setTimestamp(new Date().getTime()); //เวลา
        return model;

    }
    private void SaveEdit() {
        ApplicationModel model = createApplicationModel();
        myRef.setValue(model);
        finish();
    }
    //upload รูป
    private void getContent(){
        Intent i =new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK ){
            mProgressDialog.setTitle("Uploading Image.....");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath= storageReferencetorage.child((editName.getText().toString())).child(uri.getLastPathSegment()); //การเพิ่มข้อความลงใน Firebase

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                    mProgressDialog.dismiss();
                    downloaUri = taskSnapshot.getDownloadUrl();
                    Glide.with(EditAppActivity.this)
                            .load(downloaUri)
                            .apply(RequestOptions.centerCropTransform())
                            .into(imageIcon);

                  //  Toast.makeText(EditAppActivity.this, "Uploading Finished ...", Toast.LENGTH_LONG).show();
                }

            });
        }
    }

}

