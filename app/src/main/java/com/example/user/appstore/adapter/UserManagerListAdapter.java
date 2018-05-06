package com.example.user.appstore.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.user.appstore.R;
import com.example.user.appstore.model.UserModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 25/4/2561.
 */

public class UserManagerListAdapter extends RecyclerView.Adapter<UserManagerListAdapter.ItemViewHolder> {


    private CharSequence level[] = {"Super Admin", "Developer", "User", "Cancel"};

    private List<UserModel> userlist;

    public UserManagerListAdapter() {
        userlist = new ArrayList<>();
    }

    public UserManagerListAdapter( List<UserModel>userlist) {
        this.userlist= userlist;
    }

//    @Override
//    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
//        return new ViewHolder(v);
//
//    }

    public void setItems(List<UserModel> userModels) {
        this.userlist = userModels;
        notifyDataSetChanged();
    }


    @Override
    public ItemViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ItemViewHolder holder, int position ) {

        final UserModel userModel = userlist.get(position);

        holder.textName.setText(userModel.getName());
        holder.textEmail.setText(userModel.getEmail());
        holder.textLevel.setText(userModel.getLevel());
        Glide.with(holder.imageProfile.getContext())
                .load(userModel.getProfileUrl())
                .into(holder.imageProfile);
        holder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
//                Toast.makeText(holder.itemView.getContext(),"Selet image" ,Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
//                builder.setTitle("Selet");
                builder.setItems(level, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0: //Super Admin
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(userModel.getUid())
                                        .child("level").setValue("super-admin");

                             break;
                            case 1: //Developer
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(userModel.getUid())
                                        .child("level").setValue("developer");

                             break;
                            case 2: //User
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(userModel.getUid())
                                        .child("level").setValue("user");
                                break;
                            case 3: //Cancel
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textEmail;
        TextView textLevel;
        ImageView imageProfile;
        ImageView imageEdit;

        public ItemViewHolder( View itemView ) {
            super(itemView);

            textName = itemView.findViewById(R.id.textname);
            textEmail = itemView.findViewById(R.id.textemail);
            textLevel = itemView.findViewById(R.id.textlevel);
            imageProfile = itemView.findViewById(R.id.images);
            imageEdit = itemView.findViewById(R.id.create);
        }
    }

}

