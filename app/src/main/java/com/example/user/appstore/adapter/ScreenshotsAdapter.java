package com.example.user.appstore.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.appstore.R;
import com.example.user.appstore.model.ApplicationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 10/4/2561.
 */

public class ScreenshotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

   private List<ScreenshotsAdapter> appScreenshots;

   public ScreenshotsAdapter(){
       appScreenshots = new ArrayList<>();
   }

    public ScreenshotsAdapter( List<ScreenshotsAdapter> screenshotsAdapters) {
        this.appScreenshots = screenshotsAdapters;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_screenshots, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position ) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
