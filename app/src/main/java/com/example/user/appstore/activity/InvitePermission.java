package com.example.user.appstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.example.user.appstore.adapter.CheckBoxAdapter;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.model.ScreenShotModel;
import com.example.user.appstore.model.UserModel;
import com.example.user.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvitePermission extends AppCompatActivity {
    private String app;
    public DatabaseReference database;


    private RecyclerView recyclerView;


    private CheckBoxAdapter checkBoxAdapter;

    private ApplicationModel applicationModel;


    //    private UserModel usermodel;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_permission);

       app = getIntent().getStringExtra("Application");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Invite Permission");

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        applicationModel = new ApplicationModel();
        checkBoxAdapter = new CheckBoxAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recylerViewapp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //ให้มันมีเส้น
        recyclerView.setAdapter(checkBoxAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        database = FirebaseDatabase.getInstance().getReference("Users");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                List<UserModel> userModels = mapUserModel(dataSnapshot);
                checkBoxAdapter.setItems(userModels);
            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });

        checkBoxAdapter.setOnItemClickListener(new CheckBoxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( String id ) {


            }
        });

    }

    private List<UserModel> mapUserModel( DataSnapshot dataSnapshot ) {
        List<UserModel> userModels = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            UserModel model = snapshot.getValue(UserModel.class);
            try {
                model.setUid(snapshot.getKey());
            } catch (Exception e) {

            }
            userModels.add(model);
        }
        return userModels;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if (item.getItemId() == R.id.select_all) {
            checkBoxAdapter.setCheckAll();
            return true;
        } else if (item.getItemId() == R.id.select_none) {
            checkBoxAdapter.setUnCheckAll();
            return  true;
        } else if (item.getItemId() == R.id.id_save) {
            List<UserModel> userModels = checkBoxAdapter.getUserPermissionCheckedSelect();

            Map privates = new HashMap();
            for (UserModel model : userModels) {
             privates.put(model.getUid(),true);
            }
            FirebaseDatabase.getInstance().getReference("Applications")
                    .child(app)
                    .child("permissions")
                    .setValue(privates);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.option_text_save, menu);
        getMenuInflater().inflate(R.menu.option_select,menu);
        return true;
    }



}


