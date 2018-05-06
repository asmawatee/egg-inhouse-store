package com.example.user.appstore.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.appstore.activity.ApplicationActivity;
import com.example.user.appstore.activity.ProfileActivity;
import com.example.user.appstore.adapter.HomeListAdapter;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public DatabaseReference database;
    private RecyclerView recyclerView;
    private HomeListAdapter homeListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance().getReference().child("Applications");
        database.keepSynced(true);

        homeListAdapter =new HomeListAdapter();


        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(homeListAdapter);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                List<ApplicationModel> applicationModels = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childDataSnapShot : dataSnapshot.getChildren()) {
                        ApplicationModel applicationModel = childDataSnapShot.getValue(ApplicationModel.class);
                        applicationModel.setId(childDataSnapShot.getKey());  //ใส่ค่าidให้มัน
                        applicationModels.add(applicationModel);
                    }
                }
                homeListAdapter.setApplicationModelList(applicationModels);
            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });

        //ดึงข้อมูลจาก firebase
        homeListAdapter.setOnItemClickListener(new HomeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
//                Toast.makeText(getActivity(), "akdoskdo", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ApplicationActivity.class);
                intent.putExtra("id", id); //การส่งค่า key ดโญณธฐูที่อญุ่ของ id นั้น
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.id_profile) {
//            Toast.makeText(this, "okoko", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


}
