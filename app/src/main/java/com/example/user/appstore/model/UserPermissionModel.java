package com.example.user.appstore.model;

/**
 * Created by User on 27/4/2561.
 */

public class UserPermissionModel {

    private UserModel user;
    private boolean isChecked;


    public UserPermissionModel(UserModel user) {
        this.user = user;
        this.isChecked = false;
    }

    public UserModel getUser() {
        return user;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked( boolean checked ) {
        isChecked = checked;
    }
}
