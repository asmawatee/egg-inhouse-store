package com.example.user.appstore.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.appstore.model.ApplicationModel;
import com.example.user.appstore.R;
import com.example.user.appstore.model.UserPermissionModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 11/3/2561.
 */

public class MyAdapterListApp extends RecyclerView.Adapter<MyAdapterListApp.ViewHolder> {

    private List<ApplicationModel> applicationModelList;
    private OnItemClickListener onItemClickListener;
    private OnPopupMenuClickListener onPopupMenuClickListener;

    public DatabaseReference myRef;
    public FirebaseDatabase database;


//    private RecyclerView recyclerView;


    public MyAdapterListApp() {
        applicationModelList = new ArrayList<>();
    }

    public MyAdapterListApp( List<ApplicationModel> applicationModels ) {
        this.applicationModelList = applicationModels;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_general_app, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, final int position ) {

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final ApplicationModel applicationModel = applicationModelList.get(position);
            final String id = applicationModel.getId();


            holder.textViewName.setText(applicationModel.getApp_name());
            Glide.with(holder.itemView.getContext())
                    .load(applicationModel.getAppIconUrl())
                    .into(holder.imageViewImage);
            holder.textViewVersion.setText("Version : " + applicationModel.getApp_version());
            holder.imageViewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    PopupMenu popupMenu = new PopupMenu(holder.imageViewMenu.getContext(), holder.imageViewMenu, Gravity.START);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick( MenuItem item ) {
                            switch (item.getItemId()) {
                                case R.id.menu_item_edilper:
                                    if (onPopupMenuClickListener != null)
                                        onPopupMenuClickListener.onEditPermission(applicationModel);
                                    break;
                                case R.id.menu_item_edil:
                                    if (onPopupMenuClickListener != null)
                                        onPopupMenuClickListener.onEdit(id);
                                    break;
                                case R.id.menu_item_delete:
                                    if (onPopupMenuClickListener != null)
                                        onPopupMenuClickListener.onDelete(position, id);
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(applicationModel.getId());
                    }
                }
            });

            holder.textInstall.setText("Download : " + applicationModel.getDownload());
            holder.buttonInstallApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    onPopupMenuClickListener.onDownloadApp(applicationModel);
                }
            });
        }

    }


    public void setOnItemClickListener( OnItemClickListener onItemClickListener ) {
        this.onItemClickListener = onItemClickListener;

    }

    public void setApplicationModelList( List<ApplicationModel> applicationModelList ) {
        this.applicationModelList = applicationModelList;
        notifyDataSetChanged();

    }

    public interface OnItemClickListener {
        void onItemClick( String id );
    }


    @Override
    public int getItemCount() {
        return applicationModelList.size();
    }

    public void setItems( List<ApplicationModel> listItems ) {
        this.applicationModelList = listItems;
        notifyDataSetChanged();
    }


    public void setOnPopupMenuClickListener( OnPopupMenuClickListener onPopupMenuClickListener ) {
        this.onPopupMenuClickListener = onPopupMenuClickListener;
    }

    public void removeItem( int position ) {
        applicationModelList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewVersion;
        private ImageView imageViewImage;
        private ImageView imageViewMenu;
        private TextView textInstall;
        private Button buttonInstallApp;


        public ViewHolder( View itemView ) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textname);
            textViewVersion = itemView.findViewById(R.id.textversion);
            imageViewImage = itemView.findViewById(R.id.images);
            imageViewMenu = itemView.findViewById(R.id.menu);
            textInstall = itemView.findViewById(R.id.textdownload);
            buttonInstallApp = itemView.findViewById(R.id.btninstall);
        }
    }


//    public interface OnItemClickListener {
//
//        void onItemClick();
//    }

    public interface OnPopupMenuClickListener {

        void onEditPermission( ApplicationModel applicationModel );

        void onEdit( String id );

        void onDelete( int position, String id );

        void onDownloadApp(ApplicationModel applicationModel);
    }

}
