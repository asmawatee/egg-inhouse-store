package com.example.user.appstore.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.appstore.R;
import com.example.user.appstore.adapter.MyAdapterListApp;
import com.example.user.appstore.adapter.UserManagerListAdapter;
import com.example.user.appstore.model.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by User on 23/4/2561.
 */

public class UserManagerFragment extends Fragment {


    public DatabaseReference userRef;
    public FirebaseDatabase database;

    private RecyclerView recyclerView;
    private UserManagerListAdapter userlistAdapter;

    public UserManagerFragment() {
        /* Required empty public constructor */
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_manager, container, false);
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);


        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("Users");
        userRef.keepSynced(true);

        userlistAdapter= new UserManagerListAdapter();
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //ให้มันมีเส้น
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(userlistAdapter);

        FirebaseDatabase.getInstance()
                .getReference("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot ) {
                        List<UserModel> userModels = new ArrayList<>();
                        for (DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {
                            UserModel userModel= childDataSnapshot.getValue(UserModel.class);
                            userModels.add(userModel);
                        }
                        userlistAdapter.setItems(userModels);
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError ) {

                    }
                });


    }


}
