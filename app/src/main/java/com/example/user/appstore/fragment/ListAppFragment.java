package com.example.user.appstore.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.appstore.activity.ApplicationActivity;
import com.example.user.appstore.activity.EditAppActivity;
import com.example.user.appstore.activity.InvitePermission;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.adapter.MyAdapterListApp;
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

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListAppFragment extends Fragment {

    private DatabaseReference database;
    private RecyclerView recyclerView;
    private MyAdapterListApp myAdapterGeneral;

    //ปุ่มค้นหา
    private EditText mSearch;
    private Button btnInstall;

    private int countNummer;



    private static final String TAG = "GeneralAppFragment";

    private CharSequence iCharSequence[] ={"Public","Private","Custom","Cancel"};


    public ListAppFragment() {
        /* Required empty public constructor */
    }

    //เซ็นเมนู ให้ลูกปลา
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genral_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myAdapterGeneral = new MyAdapterListApp();
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerViewapp);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapterGeneral);

        database = FirebaseDatabase.getInstance().getReference("Applications");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot ) {
                List<ApplicationModel> applicationModels = mapApplicationModel(dataSnapshot);
                myAdapterGeneral.setItems(applicationModels);
            }

            @Override
            public void onCancelled( DatabaseError databaseError ) {

            }
        });

        //ปุ่มค้นหา
        mSearch = (EditText) view.findViewById(R.id.searching);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {
            }

            @Override
            public void onTextChanged( CharSequence searchText, int start, int before, int count ) {
                String querySearch = searchText.toString();
                FirebaseDatabase.getInstance().getReference("Applications")
                        .orderByChild("app_name")
                        .startAt(querySearch)
                        .endAt(querySearch + "\uf8ff")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot ) {
                                // Log.d(TAG, dataSnapshot.toString());
                                List<ApplicationModel> applicationModels = mapApplicationModel(dataSnapshot);
                                myAdapterGeneral.setItems(applicationModels);

                            }

                            @Override
                            public void onCancelled( DatabaseError databaseError ) {

                            }
                        });
            }

            @Override
            public void afterTextChanged( Editable s ) {


            }
        });


        //ดึงข้อมูลจาก firebase
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
            public void onEditPermission(ApplicationModel applicationModel) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Permission As");
                builder.setItems(iCharSequence, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        switch (which) {
                            case 0: // Public

                                Map publics = new HashMap();
                                publics.put("general", true);
                                FirebaseDatabase.getInstance().getReference("Applications")
                                        .child(applicationModel.getId())
                                        .child("permissions")
                                        .updateChildren(publics);

                                break;
                            case 1:// Private

                                Map privates = new HashMap();
                                privates.put(applicationModel.getDeveloperID(), true);
                                FirebaseDatabase.getInstance().getReference("Applications")
                                        .child(applicationModel.getId())
                                        .child("permissions")
                                        .updateChildren(privates);

                                break;
                            case 2: //Custom
                                Intent i = new Intent(getActivity(),InvitePermission.class);
                                i.putExtra("Application",applicationModel.getId());
                                startActivity(i);
                                break;
                            case 3: // Cancel
                                dialog.dismiss();
                                break;

                        }
                    }
                });
                AlertDialog dialog =  builder.create();
                dialog.show();
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
//                                                Log.e(TAG, e.getLocalizedMessage());
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onDownloadApp( ApplicationModel applicationModel ) {
                installApp(applicationModel);
            }
        });
    }

    private void installApp(ApplicationModel applicationModel){
        //Get old value from firebase
        long oldDownloadCount = applicationModel.getDownload();
        //Increment downloadCount
        long newDownloadCount = oldDownloadCount + 1;
        // Update new downloadCount to firebase
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Applications")
                .child(applicationModel.getId())
                .child("download")
                .setValue(newDownloadCount);

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(applicationModel.getApp_url()));
        startActivity(intent);
    }

        private List<ApplicationModel> mapApplicationModel(DataSnapshot dataSnapshot) {
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

    //เมนูลูกปลา
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_menu, menu);
//    }


    //การเพิ่มปุ่ดเพิ่ม
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_add_app) {
//           Toast.makeText(getActivity(), "okoko", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getActivity(),NewAppActivity.class);
//            startActivity(intent);
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }


}
