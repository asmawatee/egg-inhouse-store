package com.example.user.appstore.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.appstore.R;
import com.example.user.appstore.activity.NewAppActivity;
import com.example.user.appstore.model.ApplicationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21/3/2561.
 */

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ApplicationModel> applicationModelList;
    private OnItemClickListener onItemClickListener;

    public HomeListAdapter() {
        applicationModelList = new ArrayList<>();
    }

    public HomeListAdapter( List<ApplicationModel> applicationModels) {
        this.applicationModelList = applicationModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_index, parent, false);
        return new ApplicationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ApplicationListViewHolder) {
            ApplicationListViewHolder viewHolder = (ApplicationListViewHolder) holder;
            ApplicationModel model = applicationModelList.get(position);

            viewHolder.textViewName.setText(model.getApp_name());
            viewHolder.textViewVersion.setText(model.getVersion_code());
            viewHolder.textViewDownload.setText("Download :"+model.getDownload());
            Glide.with(viewHolder.itemView.getContext())
                    .load(model.getAppIconUrl())
                    .into(viewHolder.imageViewThumbnail);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(model.getId());
                    }
                }
            });
        }
    }

    public void setOnItemClickListener( OnItemClickListener onItemClickListener ) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return applicationModelList.size();
    }

    public void setApplicationModelList(List<ApplicationModel> applicationModelList) {
        this.applicationModelList = applicationModelList;
        notifyDataSetChanged();

    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }


    public static class ApplicationListViewHolder  extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewVersion;
        private ImageView imageViewThumbnail;
        private TextView textViewDownload;

        public ApplicationListViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textname);
            textViewVersion = itemView.findViewById(R.id.textversion);
            imageViewThumbnail = itemView.findViewById(R.id.images);
            textViewDownload =  itemView.findViewById(R.id.textdownload);
        }
    }

}
