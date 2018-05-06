package com.example.user.appstore.model;

/**
 * Created by User on 14/3/2561.
 */

public class GroupModel {

    private String id;
    private String namegroup;
    private String members;


    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getNamegroup() {
        return namegroup;
    }

    public void setNamegroup(String namegroup) {
        this.namegroup = namegroup;
    }

    public String getMembers(){
        return members;
    }

    public void setMembers(String members){
        this.members = members;
    }

    public GroupModel() {
        this.namegroup = namegroup;
    }

}
