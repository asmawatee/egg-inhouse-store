package com.example.user.appstore.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.appstore.R;
import com.example.user.appstore.model.UserModel;
import com.example.user.appstore.model.UserPermissionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/4/2561.
 */

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {

    private List<UserPermissionModel> userModelList;


    private OnItemClickListener onItemClickListener;

    public CheckBoxAdapter() {
        userModelList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_create, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position ) {

        UserPermissionModel userPermission = userModelList.get(position);

        holder.textViewName.setText(userPermission.getUser().getName());
        holder.textViewEmail.setText(userPermission.getUser().getEmail());
        Glide.with(holder.imageProfile.getContext())
                .load(userPermission.getUser().getProfileUrl())
                .into(holder.imageProfile);
        holder.checkBox.setChecked(userPermission.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
                if (userPermission.isChecked() == isChecked) return; //เป็นการ check ว่า ค่าที่ตี้นั้นไดเการ update หรือยัง ถ้า update แล้ว ให้ return นั้นออกมา
                userPermission.setChecked(isChecked);
                notifyDataSetChanged();

            }

        });

    }

    public List<UserModel> getUserPermissionCheckedSelect() {
        List<UserModel> userPermissionModels = new ArrayList<>();
        for (int i = 0; i < userModelList.size(); i++) {
            if (userModelList.get(i).isChecked()) {
                userPermissionModels.add(userModelList.get(i).getUser());
            }
        }
        return userPermissionModels;
    }

    public void onItemClickListener( OnItemClickListener onItemClickListener ) {
        this.onItemClickListener = onItemClickListener;

    }

    public void setOnItemClickListener( OnItemClickListener onItemClickListener ) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setCheckAll() {
        for (int i = 0; i < userModelList.size(); i++) {
            userModelList.get(i).setChecked(true);
        }
        notifyDataSetChanged();
    }

    public void setUnCheckAll() {
        for (int i = 0; i < userModelList.size(); i++) {
            userModelList.get(i).setChecked(false);
        }
        notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick( String id );
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void setItems( List<UserModel> listItems ) {
        for (int i = 0; i < listItems.size(); i++) { //แปลงค่าของ UserMode ให้ยุวใน UserPermissionModel
            this.userModelList.add(new UserPermissionModel(listItems.get(i)));
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView textViewName;
        private TextView textViewEmail;
        private ImageView imageProfile;
        private CheckBox checkBox;


        public ViewHolder( View itemView ) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textname);
            textViewEmail = itemView.findViewById(R.id.textemail);
            imageProfile = itemView.findViewById(R.id.images);
            checkBox = itemView.findViewById(R.id.checkbox_user);


        }
    }

}
