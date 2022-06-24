package com.example.testfinalproject;

import java.util.Date;

public class UserPackage {
    String uid;
    int downloads;
    Date dt;

    public UserPackage(String uid, int downloads, Date dt) {
        this.uid = uid;
        this.downloads = downloads;
        this.dt = dt;
    }

    public UserPackage(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
}
