package com.example.user.appstore.model;

import android.net.Uri;

/**
 * Created by User on 10/4/2561.
 */

public class ScreenShotModel {

    String id;
    Uri imagePath;

    public String getId() {
        return id;
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public void setImagePath( Uri imagePath ) {
        this.imagePath = imagePath;
    }
}
