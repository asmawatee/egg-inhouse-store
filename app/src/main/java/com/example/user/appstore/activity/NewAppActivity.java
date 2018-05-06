package com.example.user.appstore.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.user.appstore.fragment.HomeFragment;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewAppActivity extends AppCompatActivity {

    private static final String TAG = "NewAppActivity";
//    private static final int PICK_SOMETHING = 2 ;

    private int GALLERY_INTENT = 2;

    private ImageView ImageIcon;
    private EditText TextName;
    private EditText TextVersion;
    private EditText TextDescription;
    private EditText TextBundle;
    private EditText TextBuild;
    private EditText TextUrl;


    private Button btnsave;
    private Button btnpermission;

    private Button btnImage;

    public DatabaseReference myRef;
    public FirebaseDatabase database;

    public ApplicationModel appModel;

//    private Uri filePath;

    private FirebaseStorage storage;
    private StorageReference storageReferencetorage;
    private ProgressDialog mProgressDialog;
    private Uri downloaUri;//รูปภาพ
    private Uri ScreenshotsUri; //รูปภาพที่มีหลายรูป

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_app);

        TextName = (EditText) findViewById(R.id.textname);
        TextVersion = (EditText) findViewById(R.id.textversion);
        TextDescription = (EditText) findViewById(R.id.textdescri);
        TextBundle = (EditText) findViewById(R.id.textbundle);
        TextBuild = (EditText) findViewById(R.id.textbuild);
        TextUrl = (EditText) findViewById(R.id.texturl);


        ImageIcon = (ImageView) findViewById(R.id.imageicon);
        btnImage = (Button) findViewById(R.id.btnimage);

        //Add linear layout manager as horizontal
        //TODO การทำ screenhots แบบหลื่น
        // new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        //หัวข้อ
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("New Application");
        }

        database = FirebaseDatabase.getInstance();
        appModel = new ApplicationModel();

        btnsave = (Button) findViewById(R.id.save);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                saveData();
            }
        });

        btnpermission = (Button) findViewById(R.id.editssion);
        btnpermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                permission();
            }
        });

        //Firebase Init
        storage = FirebaseStorage.getInstance();
        storageReferencetorage = storage.getReference();

        mProgressDialog = new ProgressDialog(this);

        ImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContent();
            }
        });


    }
    //การเลือกรูปในมือถือ
    private void getContent(){
        Intent i =new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, GALLERY_INTENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            mProgressDialog.setTitle("Uploading Image.....");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = storageReferencetorage.child((TextName.getText().toString())).child("icon").child(uri.getLastPathSegment()); //การเพิ่มข้อความลงใน Firebase


            filepath.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                            mProgressDialog.dismiss();
                            downloaUri = taskSnapshot.getDownloadUrl();
//                     ScreenshotsUri = taskSnapshot.getScreenshots();

                            Glide.with(NewAppActivity.this)
                                    .load(downloaUri)
                                    .apply(RequestOptions.centerCropTransform())
                                    .into(ImageIcon);

//                    Toast.makeText(NewAppActivity.this, "File Uploaded", Toast.LENGTH_LONG).show();
                        }

                    });

        }

    }


//เขียนลง firebase
    private ApplicationModel createApplicationModel() {
       ApplicationModel model = new ApplicationModel();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//การเรียกใช้ user มาตรวจสอบว่า ผู้ใช้นั้นได้ทำการ login ยัง โดยการประกาศตัวแปร คือ uesr แล้ว เรียก user ไปใช้
        model.setApp_name(TextName.getText().toString());
        model.setApp_version(TextVersion.getText().toString());
        model.setDescription(TextDescription.getText().toString());
        model.setPackage_id(TextBundle.getText().toString());
        model.setVersion_code(TextBuild.getText().toString());
        model.setApp_url(TextUrl.getText().toString());
        model.setAppIconUrl(downloaUri.toString());//รูปภาพ
//        model.setScreenshots(ScreenshotsUri.toString());
        model.setDownload(0);
        model.setTimestamp(new Date().getTime());//วัน
        model.setDeveloperID(user.getUid()); //เรียกใช้ user
//        model.getPhotoUrl().toString(), "user");
        return model;
    }

    private void saveData() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            ApplicationModel model = createApplicationModel();
            DatabaseReference newApplicationReference = FirebaseDatabase.getInstance()
                    .getReference("Applications")
                    .push().getRef();
            newApplicationReference.setValue(model);
            Map permission = new HashMap(); //็hashMap คือ การสร้างตัว key ,Value  โดยสามารถผ่านตัว value และ key ได้
            permission.put("general", true);
            newApplicationReference.child("permissions").updateChildren(permission);
        }
        finish(); //ปิดหน้านี้
    }

    private void permission() {
        Intent intent = new Intent(NewAppActivity.this, InvitePermission.class);
        startActivity(intent);
        finish();
    }


}
