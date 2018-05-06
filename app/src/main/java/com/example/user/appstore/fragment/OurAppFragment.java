package com.example.user.appstore.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.appstore.R;
import com.example.user.appstore.activity.ApplicationActivity;
import com.example.user.appstore.activity.EditAppActivity;
import com.example.user.appstore.activity.NewAppActivity;
import com.example.user.appstore.adapter.MyAdapterListApp;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/4/2561.
 */

public class OurAppFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapterListApp myAdapterGeneral;

    private UserModel modelUser;

    private CharSequence iCharSequence[] = {"Public", "Private", "Custom", "Cancel"};

    private static final String TAG = "OurAppFragment";

    public OurAppFragment() {
        /* Required empty public constructor */
    }

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_our_app, container, false);

    }


    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated(view, savedInstanceState);

        myAdapterGeneral = new MyAdapterListApp();
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerViewapp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapterGeneral);
        modelUser = new UserModel();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance()
                .getReference("Applications")
                .orderByChild("developerID")
                .equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot ) {
                        List<ApplicationModel> applicationModels = mapApplicationModel(dataSnapshot);
                        myAdapterGeneral.setItems(applicationModels);
                    }

                    @Override
                    public void onCancelled( DatabaseError databaseError ) {

                    }
                });

        myAdapterGeneral.setOnItemClickListener(new MyAdapterListApp.OnItemClickListener() {
            @Override
            public void onItemClick( String id ) {
//                Toast.makeText(getActivity(), "staa", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ApplicationActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });
        myAdapterGeneral.setOnPopupMenuClickListener(new MyAdapterListApp.OnPopupMenuClickListener() {

            @Override
            public void onDownloadApp( ApplicationModel applicationModel ) {

            }

            @Override
            public void onEditPermission( ApplicationModel applicationModel ) {
//                Toast.makeText(myAdapterGeneral.getItemCount()."mess", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onEdit( String id ) {
                Intent intent = new Intent(getActivity(), EditAppActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }

            @Override
            public void onDelete( int position, String id ) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Delete Application")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick( DialogInterface dialog, int which ) {
                                FirebaseDatabase.getInstance().getReference("Applications/" + id).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess( Void aVoid ) {
                                                myAdapterGeneral.removeItem(position);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure( @NonNull Exception e ) {
                                                Log.e(TAG, e.getLocalizedMessage());
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();


            }
        });
    }

    private List<ApplicationModel> mapApplicationModel( DataSnapshot dataSnapshot ) {
        List<ApplicationModel> applicationModels = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            ApplicationModel model = snapshot.getValue(ApplicationModel.class);
            try {
                model.setId(snapshot.getKey());
            } catch (Exception e) {

            }
            applicationModels.add(model);
        }
        return applicationModels;
    }


    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if (item.getItemId() == R.id.action_add_app) {
//            Toast.makeText(getActivity(), "okoko", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), NewAppActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
