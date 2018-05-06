package com.example.user.appstore.model;

/**
 * Created by User on 16/3/2561.
 */

public class UserModel {
    private String uid;
    private String email;
    private String name;
    private String profileUrl;
    private String level;

    public UserModel() {

    }


    public String getUid() {
        return uid;
    }

    public void setUid( String uid ) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl( String profileUrl ) {
        this.profileUrl = profileUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel( String level ) {
        this.level = level;
    }

    public UserModel( String uid, String email, String name, String profileUrl, String level ) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.profileUrl = profileUrl;
        this.level = level;
    }


}



