package com.example.user.appstore.model;

/**
 * Created by User on 27/2/2561.
 */

public class ApplicationModel {

    private String appIconUrl;
    private String app_name;
    private long download;
    private String app_version;
    private String description;
    private String package_id;
    private String version_code;
    private String app_url;
    private String id;

    private String screenshots;

    //    private String uid;
    private String developerID;
    private long timestamp;


    public String getApp_name() {
        return app_name;
    }

    public void setApp_name( String app_name ) {
        this.app_name = app_name;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version( String app_version ) {
        this.app_version = app_version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id( String package_id ) {
        this.package_id = package_id;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code( String version_code ) {
        this.version_code = version_code;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url( String app_url ) {
        this.app_url = app_url;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public void setAppIconUrl( String appIconUrl ) {
        this.appIconUrl = appIconUrl;

    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp( long timestamp ) {
        this.timestamp = timestamp;
    }

    public String getDeveloperID() {
        return developerID;
    }

    public void setDeveloperID( String developerID ) {
        this.developerID = developerID;
    }

    public long getDownload() {
        return download;
    }

    public void setDownload( long download ) {
        this.download = download;
    }

//    public  void setScreenshots(String screenshots){
//        this.screenshots = screenshots ;
//    }
//
//    public String getScreenshots(){
//        return screenshots;
//    }


}
